package com.example.ballup_backend.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ballup_backend.dto.res.slot.UnavailableSlotResponse;
import com.example.ballup_backend.entity.PlayingCenterEntity;
import com.example.ballup_backend.entity.UnavailableSlotEntity;
import com.example.ballup_backend.repository.PlayingCenterRepository;
import com.example.ballup_backend.repository.PlayingSlotRepository;
import com.example.ballup_backend.repository.UnavailableSlotRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UnavailableSlotService {
    @Autowired
    private UnavailableSlotRepository unavailableSlotRepository;

    @Autowired
    private PlayingSlotRepository playingSlotRepository;

    @Autowired
    private PlayingCenterRepository playingCenterRepository;

    public boolean isSlotUnavailable(Long slotId, Timestamp fromTime, Timestamp toTime) {
        return unavailableSlotRepository.isSlotUnavailable(slotId, fromTime, toTime);
    }

    public List<UnavailableSlotResponse> getUnavailableSlotsByDate(Long slotId, long startOfDay, long endOfDay) {
        Timestamp startTimestamp = new Timestamp(startOfDay);
        Timestamp endTimestamp = new Timestamp(endOfDay);

        List<UnavailableSlotEntity> unavailableSlots = unavailableSlotRepository.findBySlotIdAndTimeRange(slotId,
                startTimestamp, endTimestamp);

        return unavailableSlots.stream()
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
    }

    public List<UnavailableSlotResponse> getUnavailableSlotsById(Long slotId) {
        if (!playingSlotRepository.existsById(slotId)) {
            throw new EntityNotFoundException("Playing slot not found!");
        }
        List<UnavailableSlotEntity> unavailableSlots = unavailableSlotRepository.findUpcomingUnavailableSlots(slotId);
        List<UnavailableSlotResponse> unavailableSlotResponses = unavailableSlots.stream()
                .map(slot -> {
                    UnavailableSlotResponse response = new UnavailableSlotResponse();
                    response.setId(slot.getId());
                    response.setFromTime(slot.getFromTime());
                    response.setToTime(slot.getToTime());
                    response.setCreateBy(slot.getCreateBy());
                    response.setCreatorName(slot.getCreator().getUsername());
                    return response;
                })
                .collect(Collectors.toList());
        return unavailableSlotResponses;
    }

    public List<UnavailableSlotResponse> getUnavailableSlotsByCenterId(Long centerId) {
        PlayingCenterEntity center = playingCenterRepository.getReferenceById(centerId);
        List<Long> slotIds = playingSlotRepository.findSlotIdsByPlayingCenter(center);
        List<UnavailableSlotEntity> unavailableSlots = unavailableSlotRepository
                .findUpcomingUnavailableSlotsBySlotIds(slotIds);
        List<UnavailableSlotResponse> unavailableSlotResponses = unavailableSlots.stream()
                .map(slot -> {
                    UnavailableSlotResponse response = new UnavailableSlotResponse();
                    response.setId(slot.getId());
                    response.setFromTime(slot.getFromTime());
                    response.setToTime(slot.getToTime());
                    response.setCreateBy(slot.getCreateBy());
                    response.setCreatorName(slot.getCreator().getUsername());
                    return response;
                })
                .collect(Collectors.toList());
        return unavailableSlotResponses;
    }

}
