package com.example.ballup_backend.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ballup_backend.dto.req.slot.CreateSlotRequest;
import com.example.ballup_backend.dto.req.slot.DisableSlotRequest;
import com.example.ballup_backend.dto.req.slot.UpdateSlotRequest;
import com.example.ballup_backend.dto.res.slot.UnavailableSlotResponse;
import com.example.ballup_backend.entity.BookingEntity;
import com.example.ballup_backend.entity.PlayingCenterEntity;
import com.example.ballup_backend.entity.PlayingSlotEntity;
import com.example.ballup_backend.entity.UnavailableSlotEntity;
import com.example.ballup_backend.entity.UserEntity;
import com.example.ballup_backend.entity.UserEntity.Role;
import com.example.ballup_backend.entity.BookingEntity.BookingStatus;
import com.example.ballup_backend.entity.UnavailableSlotEntity.Status;
import com.example.ballup_backend.repository.BookingRepository;
import com.example.ballup_backend.repository.PlayingCenterRepository;
import com.example.ballup_backend.repository.PlayingSlotRepository;
import com.example.ballup_backend.repository.UnavailableSlotRepository;
import com.example.ballup_backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PlayingSlotService {

    @Autowired
    private PlayingSlotRepository playingSlotRepository; 

    @Autowired
    private PlayingCenterRepository playingCenterRepository; 

    @Autowired
    private UnavailableSlotRepository unavailableSlotRepository; 

     @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository; 

    @Autowired
    private UnavailableSlotService unavailableSlotService;

    public PlayingSlotEntity createPlayingSlot(CreateSlotRequest request) {
        PlayingCenterEntity playingCenter = playingCenterRepository.findById(request.getPlayingCenterId())
            .orElseThrow(() -> new RuntimeException("Playing center not found"));

        PlayingSlotEntity slot = PlayingSlotEntity.builder()
            .name(request.getName())
            .primaryPrice(request.getPrimaryPrice())
            .nightPrice(request.getNightPrice())
            .playingCenter(playingCenter)
            .build();

        return playingSlotRepository.save(slot);
    }

    public void disableSlot(DisableSlotRequest request) {

        Timestamp fromTimestamp = new Timestamp(request.getFromTime());
        Timestamp toTimestamp = new Timestamp(request.getToTime());
 
        // In kết quả
        System.out.println("From Timestamp: " + fromTimestamp.toLocalDateTime());
        System.out.println("To Timestamp: " + toTimestamp.toLocalDateTime());

        //check available
        boolean isAvailable = unavailableSlotService.isSlotUnavailable(request.getPlayingSlotId(), fromTimestamp, toTimestamp);
        if(isAvailable) { 
            throw new RuntimeException("Slot not available"); 
        }

        //check valid slot, user
        PlayingSlotEntity playingSlot = playingSlotRepository.findById(request.getPlayingSlotId())
            .orElseThrow(() -> new RuntimeException("Playing slot not found"));
    
        UserEntity user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
    
        //prepaer role
        UnavailableSlotEntity.createdBy createdBy = user.getRole().equals(Role.OWNER) 
            ? UnavailableSlotEntity.createdBy.BY_OWNER 
            : UnavailableSlotEntity.createdBy.BY_USER;

        //create unavailable entity
        UnavailableSlotEntity unavailableSlot = UnavailableSlotEntity.builder()
            .fromTime(fromTimestamp)
            .toTime(toTimestamp)
            .slot(playingSlot)
            .creator(user)
            .createBy(createdBy) 
            .status(Status.SUBMITTING)
            .build();
        unavailableSlotRepository.save(unavailableSlot);

        //if user -> create booking
        if(user.getRole().equals(Role.USER)){
            BookingEntity bookingEntity = BookingEntity.builder()
                .status(BookingStatus.REQUESTED)
                .payment(null)
                .bookingSlot(unavailableSlot)
                .build();
            bookingRepository.save(bookingEntity);
        }     
    }

    public List<UnavailableSlotResponse> getDisabledSlots(Long slotId) {

        PlayingSlotEntity playingSlot = playingSlotRepository.findById(slotId)
            .orElseThrow(() -> new RuntimeException("Playing slot not found"));

        List<UnavailableSlotEntity> unavailableSlots = unavailableSlotRepository.findBySlot(playingSlot);
    
        List<UnavailableSlotResponse> unavailableSlotsResponses = unavailableSlots.stream()
        .map(slot -> {
            return UnavailableSlotResponse.builder()
                .id(slot.getId())
                .fromTime(slot.getFromTime())
                .toTime(slot.getToTime())
                .createBy(slot.getCreateBy())
                .creatorName(slot.getCreator().getUsername()) 
                .build();
        })
        .collect(Collectors.toList());

        return unavailableSlotsResponses;

    }

    @Transactional
    public void updatePlayingSlot(Long id, UpdateSlotRequest request) {
        PlayingSlotEntity existingSlot = playingSlotRepository.getReferenceById(id);

        if (request.getName() != null) {
            existingSlot.setName(request.getName());
        }
        if (request.getPrimaryPrice() != null) {
            existingSlot.setPrimaryPrice(request.getPrimaryPrice());
        }
        if (request.getNightPrice() != null) {
            existingSlot.setNightPrice(request.getNightPrice());
        }
        playingSlotRepository.save(existingSlot);
    }
}
