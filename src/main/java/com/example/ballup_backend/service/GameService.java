package com.example.ballup_backend.service;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ballup_backend.dto.req.game.CreateGameRequest;
import com.example.ballup_backend.entity.BookingEntity;
import com.example.ballup_backend.entity.BookingEntity.BookingStatus;
import com.example.ballup_backend.entity.ConversationEntity;
import com.example.ballup_backend.entity.GameEntity;
import com.example.ballup_backend.entity.PlayingSlotEntity;
import com.example.ballup_backend.entity.UnavailableSlotEntity;
import com.example.ballup_backend.entity.UserEntity;
import com.example.ballup_backend.entity.GamePlayerEntity.GameTeam;
import com.example.ballup_backend.entity.UnavailableSlotEntity.Status;
import com.example.ballup_backend.entity.UnavailableSlotEntity.createdBy;
import com.example.ballup_backend.repository.BookingRepository;
import com.example.ballup_backend.repository.ConversationRepository;
import com.example.ballup_backend.repository.GameRepository;
import com.example.ballup_backend.repository.PlayingSlotRepository;
import com.example.ballup_backend.repository.UnavailableSlotRepository;
import com.example.ballup_backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlayingSlotRepository slotRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private GamePlayerService gamePlayerService;

    @Autowired
    private UnavailableSlotService unavailableSlotService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UnavailableSlotRepository unavailableSlotRepository; 

    @Transactional
    public void createGame(CreateGameRequest request) {

        UserEntity creator = userRepository.getReferenceById(request.getUserId());
        
        ConversationEntity conversation = ConversationEntity.builder()
            .name("Chat-" + UUID.randomUUID().toString().substring(0, 8)) 
            .build();
        conversationRepository.save(conversation);
        
        //convert time to  localDateTime
        LocalDateTime fromDateTime = LocalDateTime.ofEpochSecond(request.getFromTime() / 1000, 0, ZoneOffset.UTC);
        LocalDateTime toDateTime = LocalDateTime.ofEpochSecond(request.getToTime() / 1000, 0, ZoneOffset.UTC);
        
        GameEntity game;
        if (request.getSlotId() != null) {
            PlayingSlotEntity slot = slotRepository.getReferenceById(request.getSlotId());

            //check slot available
            boolean isAvailable = unavailableSlotService.isSlotUnavailable(request.getSlotId(), fromDateTime, toDateTime);
            if(isAvailable) { 
                throw new RuntimeException("Slot not available"); 
            }

            //create unavailable slot
            UnavailableSlotEntity unavailableSlot = UnavailableSlotEntity.builder()
                .fromTime(Timestamp.valueOf(fromDateTime))
                .toTime(Timestamp.valueOf(toDateTime))
                .slot(slot)
                .creator(creator)
                .createBy(createdBy.BY_USER) 
                .status(Status.PENDING)
                .build();
            unavailableSlotRepository.save(unavailableSlot);

            //create booking 
            BookingEntity bookingEntity = BookingEntity.builder()
                .status(BookingStatus.REQUESTED)
                .payment(null)
                .bookingSlot(unavailableSlot)
                .build();
            bookingRepository.save(bookingEntity);


            //create game entity
            game = GameEntity.builder()
                .name(request.getName())
                .creator(creator)
                .fromTime(Timestamp.valueOf(fromDateTime))
                .toTime(Timestamp.valueOf(toDateTime))
                .location(request.getLocation())
                .description(request.getDescription())
                .type(request.getType())
                .conversation(conversation)
                .playingSlot(slot)
                .cover(request.getCover())
                .build();
        } else {
            game = GameEntity.builder()
            .name(request.getName())
            .creator(creator)
            .fromTime(Timestamp.valueOf(fromDateTime))
            .toTime(Timestamp.valueOf(toDateTime))
            .location(request.getLocation())
            .description(request.getDescription())
            .type(request.getType())
            .conversation(conversation)
            .cover(request.getCover())
            .build();
        }
        GameEntity savedGame =  gameRepository.save(game);
        
        gamePlayerService.addPlayersToGame(savedGame.getId(), GameTeam.TEAMA, request.getMemberIdList());
    }

    
    
}
