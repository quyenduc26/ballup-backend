package com.example.ballup_backend.service;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.ballup_backend.dto.req.game.CreateGameRequest;
import com.example.ballup_backend.dto.res.game.GameResponse;
import com.example.ballup_backend.dto.res.game.MyGameResponse;
import com.example.ballup_backend.dto.res.team.GameTeamMemberResponse;
import com.example.ballup_backend.dto.res.team.GameTeamResponse;
import com.example.ballup_backend.dto.res.team.TeamOverviewResponse;
import com.example.ballup_backend.entity.BookingEntity;
import com.example.ballup_backend.entity.BookingEntity.BookingStatus;
import com.example.ballup_backend.entity.ConversationEntity;
import com.example.ballup_backend.entity.GameEntity;
import com.example.ballup_backend.entity.PlayingSlotEntity;
import com.example.ballup_backend.entity.TeamEntity;
import com.example.ballup_backend.entity.UnavailableSlotEntity;
import com.example.ballup_backend.entity.UserEntity;
import com.example.ballup_backend.entity.GamePlayerEntity.GameTeam;
import com.example.ballup_backend.entity.UnavailableSlotEntity.Status;
import com.example.ballup_backend.entity.UnavailableSlotEntity.createdBy;
import com.example.ballup_backend.repository.BookingRepository;
import com.example.ballup_backend.repository.ConversationRepository;
import com.example.ballup_backend.repository.GamePlayerRepository;
import com.example.ballup_backend.repository.GameRepository;
import com.example.ballup_backend.repository.PlayingSlotRepository;
import com.example.ballup_backend.repository.TeamMemberRepository;
import com.example.ballup_backend.repository.TeamRepository;
import com.example.ballup_backend.repository.UnavailableSlotRepository;
import com.example.ballup_backend.repository.UserRepository;
import com.example.ballup_backend.specification.GameSpecification;

import jakarta.transaction.Transactional;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private PlayingSlotRepository slotRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private GamePlayerService gamePlayerService;

    @Autowired
    private TeamService teamService;

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
        Timestamp fromTimestamp = new Timestamp(request.getFromTime());
        Timestamp toTimestamp = new Timestamp(request.getToTime());
        
        GameEntity game;
        if (request.getSlotId() != null) {
            PlayingSlotEntity slot = slotRepository.getReferenceById(request.getSlotId());

            //check slot available
            boolean isAvailable = unavailableSlotService.isSlotUnavailable(request.getSlotId(), fromTimestamp, toTimestamp);
            if(isAvailable) { 
                throw new RuntimeException("Slot not available"); 
            }

            //create unavailable slot
            UnavailableSlotEntity unavailableSlot = UnavailableSlotEntity.builder()
                .fromTime(fromTimestamp)
                .toTime(toTimestamp)
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
                .fromTime(fromTimestamp)
                .toTime(toTimestamp)
                .address(request.getLocation())
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
            .fromTime(fromTimestamp)
            .toTime(toTimestamp)
            .address(request.getLocation())
            .description(request.getDescription())
            .type(request.getType())
            .conversation(conversation)
            .cover(request.getCover())
            .build();
        }
        GameEntity savedGame =  gameRepository.save(game);
        
        gamePlayerService.addPlayersToGame(savedGame.getId(), request.getUserTeamId(), GameTeam.TEAMA, request.getMemberIdList());
    }

    @Transactional
    public List<MyGameResponse> getMyGames(Long userId) {
        UserEntity user = userRepository.getReferenceById(userId);
        
        List<GameEntity> games = gamePlayerRepository.findGamesByUser(user);
        
        return games.stream()
            .map(game -> {
                // Lấy danh sách teamId theo gameId
                List<Long> teamIds = gamePlayerRepository.findTeamIdsByGameId(game.getId());

                // Lấy thông tin overview của từng team
                TeamOverviewResponse teamA = teamIds.size() > 0 ? teamService.getTeamOverview(teamIds.get(0)) : null;
                TeamOverviewResponse teamB = teamIds.size() > 1 ? teamService.getTeamOverview(teamIds.get(1)) : null;

                return MyGameResponse.builder()
                    .id(game.getId())
                    .name(game.getName())
                    .fromTime(game.getFromTime())
                    .toTime(game.getToTime())
                    .center(game.getAddress())
                    .cover(game.getCover())
                    .type(game.getType())
                    .conversationId(game.getConversation().getId())
                    .slotId(game.getPlayingSlot() != null ? game.getPlayingSlot().getId() : null)
                    .isCreator(game.getCreator().getId().equals(user.getId()))
                    .teamA(teamA)
                    .teamB(teamB)
                    .build();
            })
            .collect(Collectors.toList());
    }

   @Transactional
    public List<GameResponse> getGamesWithOnlyTeamA(String name, String address, String sport) {
        // Tạo specification để lọc game theo điều kiện
        Specification<GameEntity> spec = GameSpecification.filterGames(name, address, sport);

        // Lấy danh sách game theo filter
        List<GameEntity> allGames = gameRepository.findAll(spec);
        List<GameResponse> resultList;

        // Lọc ra các game chỉ có teamA
        List<GameEntity> filteredGames = allGames.stream()
            .filter(game -> {
                List<Long> teamIds = gamePlayerRepository.findTeamIdsByGameId(game.getId());
                return teamIds.size() == 1; // Chỉ có duy nhất teamA
            })
            .collect(Collectors.toList());

            resultList = filteredGames.stream().map(game -> {
                List<Long> teamIds = gamePlayerRepository.findTeamIdsByGameId(game.getId());
                TeamEntity team = teamRepository.getReferenceById(teamIds.get(0));
                List<UserEntity> teamMembers = teamMemberRepository.findUsersByTeamId(team.getId());
                List<GameTeamMemberResponse> memberResponses = teamMembers.stream()
                    .map(user -> GameTeamMemberResponse.builder()
                        .avatar(user.getAvatar())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build())
                    .collect(Collectors.toList());
                GameTeamResponse teamA = GameTeamResponse.builder()
                    .name(team.getName())
                    .intro(team.getIntro())
                    .logo(team.getLogo())
                    .members(memberResponses)
                    .build();


                return GameResponse.builder()
                .id(game.getId())
                    .name(game.getName())
                    .fromTime(game.getFromTime())
                    .toTime(game.getToTime())
                    .cover(game.getCover())
                    .type(game.getType())
                    .conversationId(game.getConversation().getId())
                    .slotId(game.getPlayingSlot() != null ? game.getPlayingSlot().getId() : null)
                    .centerName(game.getPlayingSlot() != null ? game.getPlayingSlot().getPlayingCenter().getName() : null)
                    .slotName(game.getPlayingSlot() != null ? game.getPlayingSlot().getName() : null)
                    .teamA(teamA)
                    .build();
            }).collect(Collectors.toList());

            return resultList;
        }
    }
