package com.example.ballup_backend.service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ballup_backend.entity.GameEntity;
import com.example.ballup_backend.entity.GamePlayerEntity;
import com.example.ballup_backend.entity.TeamEntity;
import com.example.ballup_backend.entity.UserEntity;
import com.example.ballup_backend.repository.GamePlayerRepository;
import com.example.ballup_backend.repository.GameRepository;
import com.example.ballup_backend.repository.TeamRepository;
import com.example.ballup_backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class GamePlayerService {
    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Transactional
    public void addPlayersToGame(Long gameId, Long userTeamId, GamePlayerEntity.GameTeam gameTeam, List<Long> playerIds) {
        GameEntity game = gameRepository.getReferenceById(gameId);
        TeamEntity team = teamRepository.getReferenceById(userTeamId);


        List<GamePlayerEntity> gamePlayers = playerIds.stream()
                .map(playerId -> {
                    UserEntity user = userRepository.findById(playerId)
                            .orElseThrow(() -> new RuntimeException("User not found: " + playerId));

                    return GamePlayerEntity.builder()
                            .game(game)
                            .user(user)
                            .gameTeam(gameTeam)
                            .joinedTeam(team)
                            .build();
                })
                .collect(Collectors.toList());

        gamePlayerRepository.saveAll(gamePlayers);
    }
    
    public List<Long> getTeamIdsByGameId(Long gameId) {
        return gamePlayerRepository.findTeamIdsByGameId(gameId);
    }
}
