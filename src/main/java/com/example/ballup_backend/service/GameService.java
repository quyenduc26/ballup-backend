package com.example.ballup_backend.service;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.ballup_backend.dto.req.game.CreateGameRequest;
import com.example.ballup_backend.dto.req.game.UpdateGameInfoRequest;
import com.example.ballup_backend.dto.req.game.UpdateGameTimeAndSlotRequest;
import com.example.ballup_backend.dto.res.game.GameResponse;
import com.example.ballup_backend.dto.res.game.MyGameResponse;
import com.example.ballup_backend.dto.res.team.GameTeamMemberResponse;
import com.example.ballup_backend.dto.res.team.GameTeamResponse;
import com.example.ballup_backend.dto.res.team.TeamOverviewResponse;
import com.example.ballup_backend.entity.BookingEntity;
import com.example.ballup_backend.entity.BookingEntity.BookingStatus;
import com.example.ballup_backend.entity.ConversationEntity;
import com.example.ballup_backend.entity.ConversationMemberEntity;
import com.example.ballup_backend.entity.GameEntity;
import com.example.ballup_backend.entity.GamePlayerEntity;
import com.example.ballup_backend.entity.PaymentEntity;
import com.example.ballup_backend.entity.PlayingSlotEntity;
import com.example.ballup_backend.entity.TeamEntity;
import com.example.ballup_backend.entity.UnavailableSlotEntity;
import com.example.ballup_backend.entity.UserEntity;
import com.example.ballup_backend.entity.GamePlayerEntity.GameTeam;
import com.example.ballup_backend.entity.PaymentEntity.PaymentStatus;
import com.example.ballup_backend.entity.UnavailableSlotEntity.Status;
import com.example.ballup_backend.entity.UnavailableSlotEntity.createdBy;
import com.example.ballup_backend.repository.BookingRepository;
import com.example.ballup_backend.repository.ConversationMemberRepository;
import com.example.ballup_backend.repository.ConversationRepository;
import com.example.ballup_backend.repository.GamePlayerRepository;
import com.example.ballup_backend.repository.GameRepository;
import com.example.ballup_backend.repository.PaymentRepository;
import com.example.ballup_backend.repository.PlayingSlotRepository;
import com.example.ballup_backend.repository.TeamMemberRepository;
import com.example.ballup_backend.repository.TeamRepository;
import com.example.ballup_backend.repository.UnavailableSlotRepository;
import com.example.ballup_backend.repository.UserRepository;
import com.example.ballup_backend.specification.GameSpecification;
import com.example.ballup_backend.util.common.BookingPriceCalculator;

import jakarta.persistence.EntityNotFoundException;
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

    @Autowired
    private ConversationMemberRepository conversationMemberRepository; 

    @Autowired
    private BookingPriceCalculator bookingPriceCalculator; 

    @Autowired
    private PaymentRepository paymentRepository; 

    @Transactional
    public void createGame(CreateGameRequest request) {

        UserEntity creator = userRepository.getReferenceById(request.getUserId());
        
        ConversationEntity conversation = ConversationEntity.builder()
            .name("Chat-" + UUID.randomUUID().toString().substring(0, 8)) 
            .build();
        ConversationEntity savedConversation = conversationRepository.save(conversation);
        
        List<UserEntity> relatedUsers = userRepository.findAllById(request.getMemberIdList());
        for (UserEntity user : relatedUsers) {
            ConversationMemberEntity memberConversation = ConversationMemberEntity.builder()
                .conversation(savedConversation)
                .user(user)
                .build();
                conversationMemberRepository.save(memberConversation);
        }
        //convert time to  localDateTime
        Timestamp fromTimestamp = new Timestamp(request.getFromTime());
        Timestamp toTimestamp = new Timestamp(request.getToTime());
        
        GameEntity game;
        if (request.getSlotId() != null) {
            PlayingSlotEntity slot = slotRepository.getReferenceById(request.getSlotId());

            //check slot available
            boolean isAvailable = unavailableSlotService.isSlotUnavailable(request.getSlotId(), fromTimestamp, toTimestamp);
            Long totalCost = bookingPriceCalculator.calculateBookingPrice(fromTimestamp, toTimestamp, slot.getPrimaryPrice(), slot.getNightPrice());

            if( isAvailable ) { 
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
            UnavailableSlotEntity savedUnavailableSlot = unavailableSlotRepository.save(unavailableSlot);

            //create payment
            PaymentEntity paymentEntity = PaymentEntity.builder()
                .amount(totalCost)
                .creator(creator)
                .status(PaymentStatus.PENDING)
                .build();
            PaymentEntity savedPayment = paymentRepository.save(paymentEntity);

            //create booking 
            BookingEntity bookingEntity = BookingEntity.builder()
                .status(BookingStatus.REQUESTED)
                .payment(savedPayment)
                .bookingSlot(savedUnavailableSlot)
                .build();
            BookingEntity savedBookingEntity = bookingRepository.save(bookingEntity);

            //create game entity
            game = GameEntity.builder()
                .name(request.getName())
                .creator(creator)
                .fromTime(fromTimestamp)
                .toTime(toTimestamp)
                .address(request.getLocation())
                .description(request.getDescription())
                .type(request.getType())
                .conversation(savedConversation)
                .playingSlot(slot)
                .booking(savedBookingEntity)
                .cover(request.getCover())
                .membersRequired(request.getMembersRequired())
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
            .conversation(savedConversation)
            .cover(request.getCover())
            .membersRequired(request.getMembersRequired())
            .build();
        }
        GameEntity savedGame = gameRepository.save(game);
        gamePlayerService.addPlayersToGame(savedGame.getId(), request.getUserTeamId(), GameTeam.TEAMA, request.getMemberIdList());
    }


    @Transactional
    public List<MyGameResponse> getMyGames(Long userId) {
        UserEntity user = userRepository.getReferenceById(userId);
        List<GameEntity> games = gamePlayerRepository.findGamesByUser(user);
        return games.stream()
            .map(game -> {
                List<Long> teamIds = gamePlayerRepository.findTeamIdsByGameId(game.getId());
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


    @Transactional
    public void updateGameTimeAndSlot(UpdateGameTimeAndSlotRequest request) {
        GameEntity game = gameRepository.getReferenceById(request.getGameId());
    
        Timestamp fromTimestamp = new Timestamp(request.getFromTime());
        Timestamp toTimestamp = new Timestamp(request.getToTime());
    
        // === Nhóm 1: Cập nhật thời gian ===
        boolean isTimeUpdated = !game.getFromTime().equals(fromTimestamp) || !game.getToTime().equals(toTimestamp);
        if (isTimeUpdated) {
            game.setFromTime(fromTimestamp);
            game.setToTime(toTimestamp);
        }
        // === Nhóm 2: Cập nhật slot ===
        boolean isSlotUpdated = request.getNewSlotId() != null && (game.getPlayingSlot() == null || !game.getPlayingSlot().getId().equals(request.getNewSlotId()));
    
        if (isSlotUpdated) {
            PlayingSlotEntity slot = slotRepository.getReferenceById(request.getNewSlotId());
            Long totalCost = bookingPriceCalculator.calculateBookingPrice(fromTimestamp, toTimestamp, slot.getPrimaryPrice(), slot.getNightPrice());
            System.out.println(totalCost);
            // Hủy đặt chỗ cũ nếu có
            UserEntity creator = userRepository.getReferenceById(game.getBooking().getPayment().getCreator().getId());

            //giữ lại các entities
            UnavailableSlotEntity oldUnavailableSlot = unavailableSlotRepository.getReferenceById(game.getBooking().getBookingSlot().getId());
            BookingEntity oldBooking = bookingRepository.getReferenceById(game.getBooking().getId());
            PaymentEntity oldPayment = paymentRepository.getReferenceById(game.getBooking().getPayment().getId());

            // Kiểm tra slot mới có khả dụng không
            PlayingSlotEntity newSlot = slotRepository.findById(request.getNewSlotId())
                .orElseThrow(() -> new RuntimeException("New slot not found"));

            boolean isUnavailable = unavailableSlotService.isSlotUnavailable(newSlot.getId(), fromTimestamp, toTimestamp);
            if (isUnavailable) {
                throw new RuntimeException("New slot is not available");
            }

            UnavailableSlotEntity newUnavailableSlot = UnavailableSlotEntity.builder()
                .createBy(createdBy.BY_USER)
                .fromTime(fromTimestamp)
                .toTime(toTimestamp)
                .slot(newSlot)
                .creator(creator)
                .status(Status.SUBMITTING)
                .build();
            UnavailableSlotEntity savedUnavailableSlotEntity = unavailableSlotRepository.save(newUnavailableSlot);
            unavailableSlotRepository.flush(); // Đảm bảo ID được gán
            savedUnavailableSlotEntity = unavailableSlotRepository.findById(savedUnavailableSlotEntity.getId()).orElseThrow();
                
            //create payment
            PaymentEntity paymentEntity = PaymentEntity.builder()
                .amount(totalCost)
                .creator(creator)
                .status(PaymentStatus.PENDING)
                .build();
            PaymentEntity savedPayment = paymentRepository.save(paymentEntity);
            paymentRepository.flush();
            savedPayment = paymentRepository.findById(savedPayment.getId()).orElseThrow();
                

            // create booking
            BookingEntity bookingEntity = BookingEntity.builder()
                .status(BookingStatus.REQUESTED)
                .payment(savedPayment)
                .bookingSlot(savedUnavailableSlotEntity)
                .build();
            BookingEntity savedBooking = bookingRepository.save(bookingEntity);
            bookingRepository.flush();

            System.out.println(savedBooking.getBookingSlot());
            // Gán slot mới cho game
            game.setPlayingSlot(newSlot);
            game.setBooking(savedBooking);

            bookingRepository.deleteById(oldBooking.getId());
            paymentRepository.deleteById(oldPayment.getId());
            unavailableSlotRepository.deleteById(oldUnavailableSlot.getId());
        }

        gameRepository.save(game);
    }

    @Transactional
    public void updateGameInfo(UpdateGameInfoRequest request) {
        GameEntity game = gameRepository.findById(request.getGameId())
            .orElseThrow(() -> new RuntimeException("Game not found"));
    
        if (StringUtils.hasText(request.getName())) {
            game.setName(request.getName());
        }
        if (StringUtils.hasText(request.getCover())) {
            game.setCover(request.getCover());
        }
        if (StringUtils.hasText(request.getDescription())) {
            game.setDescription(request.getDescription());
        }
        if (request.getMembersRequired() != null) {
            game.setMembersRequired(request.getMembersRequired());
        }
        gameRepository.save(game);
    }

    @Transactional
    public void leaveGame(Long gameId, Long userId) {
        gamePlayerRepository.deleteByGameIdAndUserId(gameId, userId);
        conversationMemberRepository.deleteByUserIdAndConversationId( userId, gameRepository.getReferenceById(gameId).getConversation().getId());
    }
    
    @Transactional
    public void joinGame(Long gameId, Long userId) {
        GameEntity game = gameRepository.findById(gameId)
            .orElseThrow(() -> new RuntimeException("Game not found"));

        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        GamePlayerEntity gamePlayer = GamePlayerEntity.builder()
            .game(game)
            .user(user)
            .gameTeam(GameTeam.TEAMB) 
            .build();
        gamePlayerRepository.save(gamePlayer);

        ConversationMemberEntity joinedMember = ConversationMemberEntity.builder()
            .conversation(game.getConversation())
            .user(user)
            .build();
        conversationMemberRepository.save(joinedMember);
    }

    @Transactional
    public void joinGameAsTeam(Long gameId, Long userId) {
        GameEntity game = gameRepository.findById(gameId)
            .orElseThrow(() -> new RuntimeException("Game not found"));

        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Team not found"));

        TeamEntity teamMembers = teamMemberRepository.findTeamByUserIdAndSportType(userId, game.getType());
        List<Long> teamMemberIds = teamMemberRepository.findAllMemberIdsByTeamId(teamMembers.getId());

        for (Long id : teamMemberIds) {
            UserEntity member = userRepository.getReferenceById(id);
            boolean isAlreadyJoined = gamePlayerRepository.existsByGameAndUser(game, member);
            if (!isAlreadyJoined) {
                GamePlayerEntity gamePlayer = GamePlayerEntity.builder()
                    .game(game)
                    .user(member)
                    .joinedTeam(teamMembers)
                    .gameTeam(GameTeam.TEAMB) 
                    .build();
                gamePlayerRepository.save(gamePlayer);
                
                ConversationMemberEntity joinedMember = ConversationMemberEntity.builder()
                    .conversation(game.getConversation())
                    .user(member)
                    .build();
                conversationMemberRepository.save(joinedMember);
            }
        }
    }

    @Transactional
    public void cancelGame( Long gameId, Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        GameEntity game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Game not found"));

        if (!game.getCreator().getId().equals(user.getId())) {
            throw new RuntimeException("Only the creator can cancel the team.");
        }
        Long conversationId = game.getConversation() != null ? game.getConversation().getId() : null;
        gamePlayerRepository.deleteByGameId(game.getId());

        gameRepository.deleteById(game.getId());
        if (conversationId != null) {
            conversationMemberRepository.deleteByConversationId(conversationId);
            conversationRepository.deleteById(conversationId);
        }
        bookingRepository.deleteById(game.getBooking().getId());
        unavailableSlotRepository.deleteById(game.getBooking().getBookingSlot().getId());
        paymentRepository.deleteById(game.getBooking().getPayment().getId());
    }


}
