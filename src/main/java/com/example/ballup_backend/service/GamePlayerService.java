package com.example.ballup_backend.service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ballup_backend.entity.GameEntity;
import com.example.ballup_backend.entity.GamePlayerEntity;
import com.example.ballup_backend.entity.UserEntity;
import com.example.ballup_backend.repository.GamePlayerRepository;
import com.example.ballup_backend.repository.GameRepository;
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

    @Transactional
    public void addPlayersToGame(Long gameId, GamePlayerEntity.GameTeam gameTeam, List<Long> playerIds) {
        GameEntity match = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        List<GamePlayerEntity> gamePlayers = playerIds.stream()
                .map(playerId -> {
                    UserEntity user = userRepository.findById(playerId)
                            .orElseThrow(() -> new RuntimeException("User not found: " + playerId));

                    return GamePlayerEntity.builder()
                            .match(match)
                            .user(user)
                            .team(gameTeam)
                            .build();
                })
                .collect(Collectors.toList());

        gamePlayerRepository.saveAll(gamePlayers);
    }
    
}
