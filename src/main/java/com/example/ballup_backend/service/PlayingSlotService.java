package com.example.ballup_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ballup_backend.dto.req.slot.CreateSlotRequest;
import com.example.ballup_backend.entity.PlayingCenterEntity;
import com.example.ballup_backend.entity.PlayingSlotEntity;
import com.example.ballup_backend.repository.PlayingCenterRepository;
import com.example.ballup_backend.repository.PlayingSlotRepository;

@Service
public class PlayingSlotService {

    @Autowired
    private PlayingSlotRepository playingSlotRepository; 

    @Autowired
    private PlayingCenterRepository playingCenterRepository; 

    public PlayingSlotEntity createPlayingSlot(CreateSlotRequest request) {
        // Tìm sân bóng từ ID
        PlayingCenterEntity playingCenter = playingCenterRepository.findById(request.getPlayingCenterId())
            .orElseThrow(() -> new RuntimeException("Playing center not found"));

        // Tạo mới entity slot
        PlayingSlotEntity slot = PlayingSlotEntity.builder()
            .name(request.getName())
            .primaryPrice(request.getPrimaryPrice())
            .nightPrice(request.getNightPrice())
            .playingCenter(playingCenter)
            .build();

        // Lưu vào database
        return playingSlotRepository.save(slot);
    }

    
}
