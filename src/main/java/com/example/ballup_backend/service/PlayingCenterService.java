package com.example.ballup_backend.service;

import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.ballup_backend.dto.req.center.CreateCenterRequest;
import com.example.ballup_backend.dto.req.center.UpdateCenterRequest;
import com.example.ballup_backend.dto.res.center.CardPlayingCenterResponse;
import com.example.ballup_backend.dto.res.center.PlayingCenterResponse;
import com.example.ballup_backend.dto.res.slot.PlayingSlotResponse;
import com.example.ballup_backend.entity.PlayingCenterEntity;
import com.example.ballup_backend.entity.PlayingCenterEntity.PlayingCenterType;
import com.example.ballup_backend.entity.PlayingCenterImageEntity;
import com.example.ballup_backend.entity.PlayingSlotEntity;
import com.example.ballup_backend.entity.UserEntity;
import com.example.ballup_backend.repository.BookingRepository;
import com.example.ballup_backend.repository.PlayingCenterImageRepository;
import com.example.ballup_backend.repository.PlayingCenterRepository;
import com.example.ballup_backend.repository.PlayingSlotRepository;
import com.example.ballup_backend.repository.UnavailableSlotRepository;
import com.example.ballup_backend.repository.UserRepository;
import com.example.ballup_backend.specification.PlayingCenterSpecification;

import jakarta.transaction.Transactional;

@Service
public class PlayingCenterService {

    @Autowired
    private PlayingCenterRepository playingCenterRepository;

    @Autowired
    private PlayingSlotRepository playingSlotRepository;

    @Autowired
    private PlayingCenterImageRepository playingCenterImageRepository;

    @Autowired
    private UnavailableSlotRepository unavailableSlotRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public PlayingCenterEntity createPlayingCenter(CreateCenterRequest request) {
        UserEntity owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        PlayingCenterEntity playingCenter = PlayingCenterEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .address(request.getAddress())
                .owner(owner)
                .type(request.getCenterType())
                .build();
        PlayingCenterEntity savedCenter = playingCenterRepository.save(playingCenter);

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            List<PlayingCenterImageEntity> images = request.getImages().stream()
                    .map(image -> PlayingCenterImageEntity.builder()
                            .center(savedCenter)
                            .image(image)
                            .build())
                    .collect(Collectors.toList());
            playingCenterImageRepository.saveAll(images);
        }

        return playingCenter;
    }

    public List<PlayingCenterResponse> getCentersByOwnerId(Long ownerId) {
        UserEntity owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
    
        List<PlayingCenterEntity> centers = playingCenterRepository.findByOwner(owner);
    
        return centers.stream().map(center -> {
            List<String> imageUrls = playingCenterImageRepository.findByCenter(center).stream()
                    .map(PlayingCenterImageEntity::getImage)
                    .collect(Collectors.toList());
        List<PlayingSlotResponse> slotResponses = playingSlotRepository.findSlotsByCenter(center).stream()
        .map(slot -> PlayingSlotResponse.builder()
                .id(slot.getId())
                .name(slot.getName())
                .primaryPrice(slot.getPrimaryPrice())
                .nightPrice(slot.getNightPrice())
                .build())
        .collect(Collectors.toList());

            return PlayingCenterResponse.builder()
                .id(center.getId())
                .name(center.getName())
                .description(center.getDescription())
                .address(center.getAddress())
                .imageUrls(imageUrls)
                .slots(slotResponses)
                .build();
        }).collect(Collectors.toList());
    }
    

    public PlayingCenterResponse getCenterInfo(Long id) {
        PlayingCenterEntity playingCenter = playingCenterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playing center not found"));

        List<String> imageUrls = playingCenterImageRepository.findByCenter(playingCenter).stream()
                .map(PlayingCenterImageEntity::getImage)
                .collect(Collectors.toList());
        List<PlayingSlotResponse> slotResponses = playingSlotRepository.findByPlayingCenter(playingCenter).stream()
        .map(slot -> PlayingSlotResponse.builder()
                .id(slot.getId())
                .name(slot.getName())
                .nightPrice(slot.getNightPrice())
                .primaryPrice(slot.getPrimaryPrice())
                .build()) 
        .collect(Collectors.toList()); 
        

        return PlayingCenterResponse.builder()
                .id(playingCenter.getId())
                .name(playingCenter.getName())
                .description(playingCenter.getDescription())
                .address(playingCenter.getAddress())
                .imageUrls(imageUrls)
                .slots(slotResponses)
                .build();
    }

    public List<PlayingSlotEntity> getPlayingSlotByCenterId(Long id) {
        PlayingCenterEntity playingCenter = playingCenterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playing center not found"));

        return playingSlotRepository.findByPlayingCenter(playingCenter);
    }

    

    public List<CardPlayingCenterResponse> getCenterByCriteria( String name, String address, Long fromDateTime, Long toDateTime, String sortBy, String sortDirection, String sport) {
 
        // 1. Lọc theo thời gian trước
        List<Long> excludedCenters = new ArrayList<>();
        if (fromDateTime != null && toDateTime != null) {
                System.out.println("kgjdfhghj");
                Timestamp fromTimestamp = new Timestamp(fromDateTime);
                Timestamp toTimestamp = new Timestamp(toDateTime);

                // Lấy danh sách slotId bị chiếm trong khoảng thời gian
                List<Long> unavailableSlotIds = unavailableSlotRepository.findUnavailableSlots(fromTimestamp, toTimestamp);
                System.out.println(unavailableSlotIds);

            if (!unavailableSlotIds.isEmpty()) {
                // Lấy danh sách centerId có slot bị chiếm
                Map<Long, List<Long>> centerSlotMap = playingSlotRepository.findCentersBySlotIds(unavailableSlotIds)
                        .stream()
                        .map(result -> new AbstractMap.SimpleEntry<>((Long) result[0], (Long) result[1]))
                        .collect(Collectors.groupingBy(
                                Map.Entry::getKey,
                                Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                        ));
                // Loại bỏ center có tất cả slot bị chiếm
                excludedCenters = centerSlotMap.entrySet().stream()
                        .filter(entry -> entry.getValue().containsAll(unavailableSlotIds)) 
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());
            }
        }
        // 2. Lọc theo tên và address
        Specification<PlayingCenterEntity> spec = Specification.where(null);
        if (!excludedCenters.isEmpty()) {
            spec = spec.and(PlayingCenterSpecification.excludeCenters(excludedCenters));
        }
        if (name != null && !name.isEmpty()) {
            spec = spec.and(PlayingCenterSpecification.filterByName(name));
        }
        if (address != null && !address.isEmpty()) {
            spec = spec.and(PlayingCenterSpecification.filterByAddress(address));
        }
        if (sport != null && !sport.isEmpty()) {
                PlayingCenterType sportEnum = PlayingCenterType.valueOf(sport.toUpperCase());
                spec = spec.and(PlayingCenterSpecification.filterBySport(sportEnum));
        }

        List<PlayingCenterEntity> playingCenters = playingCenterRepository.findAll(spec);

        return playingCenters.stream().map(center -> {
                PlayingSlotEntity firstSlot = playingSlotRepository.findByPlayingCenter(center).stream().findFirst().orElse(null);
                String image = playingCenterImageRepository.findByCenter(center).get(0).getImage();
    
                return CardPlayingCenterResponse.builder()
                    .id(center.getId())
                    .name(center.getName())
                    .address(center.getAddress())
                    .type(center.getType())
                    .bookingCount(bookingRepository.countByPlayingCenter(center))
                    .primaryPrice(firstSlot != null ? firstSlot.getPrimaryPrice() : null)
                    .nightPrice(firstSlot != null ? firstSlot.getNightPrice() : null)
                    .image(image)
                    .build();
            }).collect(Collectors.toList());
        
    }

        @Transactional
        public void updatePlayingCenter(Long id, UpdateCenterRequest updatedCenter) {
        PlayingCenterEntity existingCenter = playingCenterRepository.getReferenceById(id);

        if (updatedCenter.getName() != null) {
                existingCenter.setName(updatedCenter.getName());
        }
        if (updatedCenter.getDescription() != null) {
                existingCenter.setDescription(updatedCenter.getDescription());
        }
        if (updatedCenter.getAddress() != null) {
                existingCenter.setAddress(updatedCenter.getAddress());
        }
        if (updatedCenter.getType() != null) {
                existingCenter.setType(updatedCenter.getType());
        }

        if (updatedCenter.getImages() != null && !updatedCenter.getImages().isEmpty()) {
                playingCenterImageRepository.deleteByPlayingCenter(existingCenter);
        
                List<PlayingCenterImageEntity> images = updatedCenter.getImages().stream()
                        .map(image -> PlayingCenterImageEntity.builder()
                                .center(existingCenter)
                                .image(image)
                                .build())
                        .collect(Collectors.toList());
        
                playingCenterImageRepository.saveAll(images);
            }
        }               


}
