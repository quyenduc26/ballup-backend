package com.example.ballup_backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.ballup_backend.dto.req.center.CreateCenterRequest;
import com.example.ballup_backend.dto.res.center.PlayingCenterResponse;
import com.example.ballup_backend.entity.PlayingCenterEntity;
import com.example.ballup_backend.entity.PlayingCenterImageEntity;
import com.example.ballup_backend.entity.PlayingSlotEntity;
import com.example.ballup_backend.entity.UserEntity;
import com.example.ballup_backend.repository.PlayingCenterImageRepository;
import com.example.ballup_backend.repository.PlayingCenterRepository;
import com.example.ballup_backend.repository.PlayingSlotRepository;
import com.example.ballup_backend.repository.UnavailableSlotRepository;
import com.example.ballup_backend.repository.UserRepository;
import com.example.ballup_backend.specification.PlayingCenterSpecification;

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

    public PlayingCenterEntity createPlayingCenter(CreateCenterRequest request) {
        UserEntity owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        PlayingCenterEntity playingCenter = PlayingCenterEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .address(request.getAddress())
                .owner(owner)
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
    
            return PlayingCenterResponse.builder()
                    .name(center.getName())
                    .description(center.getDescription())
                    .address(center.getAddress())
                    .imageUrls(imageUrls)
                    .build();
        }).collect(Collectors.toList());
    }
    

    public PlayingCenterResponse getCenterInfo(Long id) {
        PlayingCenterEntity playingCenter = playingCenterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playing center not found"));

        List<String> imageUrls = playingCenterImageRepository.findByCenter(playingCenter).stream()
                .map(PlayingCenterImageEntity::getImage)
                .collect(Collectors.toList());

        return PlayingCenterResponse.builder()
                .name(playingCenter.getName())
                .description(playingCenter.getDescription())
                .address(playingCenter.getAddress())
                .imageUrls(imageUrls)
                .build();
    }

    public List<PlayingSlotEntity> getPlayingSlotByCenterId(Long id) {
        PlayingCenterEntity playingCenter = playingCenterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playing center not found"));

        return playingSlotRepository.findByPlayingCenter(playingCenter);
    }

    

    public Page<PlayingCenterEntity> searchCenters(
            String name, String location, LocalDateTime fromDateTime, LocalDateTime toDateTime,
            String sortBy, String sortDirection, int page, int size) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        PageRequest pageable = PageRequest.of(page, size, sort);

        // 1. Lọc theo thời gian trước
        List<Long> excludedCenters = new ArrayList<>();
        if (fromDateTime != null && toDateTime != null) {
            // Lấy danh sách slotId bị chiếm trong khoảng thời gian
            List<Long> unavailableSlotIds = unavailableSlotRepository.findUnavailableSlots(fromDateTime, toDateTime);

            if (!unavailableSlotIds.isEmpty()) {
                // Lấy danh sách centerId có slot bị chiếm
                Map<Long, List<Long>> centerSlotMap = playingSlotRepository.findCentersBySlotIds(unavailableSlotIds)
                        .stream()
                        .collect(Collectors.groupingBy(
                                result -> (Long) result[0], // centerId
                                Collectors.mapping(result -> (Long) result[1], Collectors.toList()) // slotId
                        ));
                // Loại bỏ center có tất cả slot bị chiếm
                excludedCenters = centerSlotMap.entrySet().stream()
                        .filter(entry -> entry.getValue().containsAll(unavailableSlotIds)) // Center có tất cả slot bị chiếm
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());
            }
        }
        // 2. Lọc theo tên và location
        Specification<PlayingCenterEntity> spec = Specification.where(null);
        if (!excludedCenters.isEmpty()) {
            spec = spec.and(PlayingCenterSpecification.excludeCenters(excludedCenters));
        }
        if (name != null && !name.isEmpty()) {
            spec = spec.and(PlayingCenterSpecification.filterByName(name));
        }
        if (location != null && !location.isEmpty()) {
            spec = spec.and(PlayingCenterSpecification.filterByAddress(location));
        }

        return playingCenterRepository.findAll(spec, pageable);
    }

}
