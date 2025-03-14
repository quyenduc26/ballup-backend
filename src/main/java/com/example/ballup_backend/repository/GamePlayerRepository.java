package com.example.ballup_backend.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ballup_backend.entity.GameEntity;
import com.example.ballup_backend.entity.GamePlayerEntity;
import com.example.ballup_backend.entity.UserEntity;

public interface GamePlayerRepository extends JpaRepository<GamePlayerEntity, Long> {

    boolean existsByGameAndUser(GameEntity game, UserEntity user);
    
    @Query("SELECT gp.match FROM GamePlayerEntity gp WHERE gp.user = :user")
    List<GameEntity> findGamesByUser(@Param("user") UserEntity user);

    @Query("SELECT DISTINCT gp.gameTeam FROM GamePlayerEntity gp WHERE gp.match.id = :gameId")
    List<GamePlayerEntity.GameTeam> findDistinctTeamsByGameId(@Param("gameId") Long gameId);
    
    Optional<GamePlayerEntity> findFirstByMatch_IdAndGameTeam(Long gameId, GamePlayerEntity.GameTeam gameTeam);
    
    default List<Long> findTeamIdsByGameId(Long gameId) {
        List<GamePlayerEntity.GameTeam> distinctTeams = findDistinctTeamsByGameId(gameId);
        List<Long> teamIds = new ArrayList<>();

        for (GamePlayerEntity.GameTeam team : distinctTeams) {
            Long teamId = findFirstByMatch_IdAndGameTeam(gameId, team)
                    .map(gp -> gp.getJoinedTeam().getId())
                    .orElse(null);

            if (teamId != null) {
                teamIds.add(teamId);
            }
        }

        return teamIds;
    }

    
} 
