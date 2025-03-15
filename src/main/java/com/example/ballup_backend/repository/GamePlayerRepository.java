package com.example.ballup_backend.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ballup_backend.entity.GameEntity;
import com.example.ballup_backend.entity.GamePlayerEntity;
import com.example.ballup_backend.entity.TeamEntity;
import com.example.ballup_backend.entity.UserEntity;

import jakarta.transaction.Transactional;

public interface GamePlayerRepository extends JpaRepository<GamePlayerEntity, Long> {

    boolean existsByGameAndUser(GameEntity game, UserEntity user);
    
    @Query("SELECT gp.game FROM GamePlayerEntity gp WHERE gp.user = :user")
    List<GameEntity> findGamesByUser(@Param("user") UserEntity user);

    @Query("""
        SELECT DISTINCT gp.gameTeam FROM GamePlayerEntity gp
        WHERE gp.game.id IS NOT NULL AND gp.game.id = :gameId 
    """)
    List<GamePlayerEntity.GameTeam> findDistinctTeamsByGameId(@Param("gameId") Long gameId);
    
    Optional<GamePlayerEntity> findFirstByGameIdAndGameTeam(Long gameId, GamePlayerEntity.GameTeam gameTeam);
    
    default List<Long> findTeamIdsByGameId(Long gameId) {
        List<GamePlayerEntity.GameTeam> distinctTeams = findDistinctTeamsByGameId(gameId);
        List<Long> teamIds = new ArrayList<>();

        for (GamePlayerEntity.GameTeam team : distinctTeams) {
            findFirstByGameIdAndGameTeam(gameId, team)
                .map(GamePlayerEntity::getJoinedTeam)
                .map(TeamEntity::getId)
                .ifPresent(teamIds::add); // Chỉ thêm nếu không NULL
        }

        return teamIds;
    }


    @Transactional
    @Modifying
    @Query("DELETE FROM GamePlayerEntity gp WHERE gp.game.id = :gameId AND gp.user.id = :userId")
    void deleteByGameIdAndUserId(@Param("gameId") Long gameId, @Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM GamePlayerEntity gp WHERE gp.game.id = :gameId")
    void deleteByGameId(@Param("gameId") Long gameId);

    @Query("SELECT gp FROM GamePlayerEntity gp WHERE gp.game.id = :gameId")
    List<GamePlayerEntity> findAllPlayersByGameId(@Param("gameId") Long gameId);



} 
