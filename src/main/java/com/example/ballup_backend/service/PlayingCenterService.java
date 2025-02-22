package com.example.ballup_backend.service;

import java.util.List;
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
import com.example.ballup_backend.repository.UserRepository;
import com.example.ballup_backend.specification.PlayingCenterSpecification;

@Service
public class PlayingCenterService  {

    @Autowired
    private PlayingCenterRepository playingCenterRepository; 

    @Autowired
    private PlayingSlotRepository playingSlotRepository; 

    @Autowired
    private PlayingCenterImageRepository playingCenterImageRepository; 

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


     public Page<PlayingCenterEntity> searchCenters(String name, String location, Integer minCapacity, Integer maxCapacity, String sortBy, String sortDirection, int page, int size) {
        Specification<PlayingCenterEntity> spec = Specification
            .where(PlayingCenterSpecification.filterByName(name))
            .and(PlayingCenterSpecification.filterByAddress(location));

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        PageRequest pageable = PageRequest.of(page, size, sort);

        return playingCenterRepository.findAll(spec, pageable);

     }
}
