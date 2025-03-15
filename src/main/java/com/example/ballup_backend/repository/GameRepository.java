package com.example.ballup_backend.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ballup_backend.entity.GameEntity;


public interface GameRepository extends JpaRepository<GameEntity, Long>, JpaSpecificationExecutor<GameEntity> {
    @Query("""
        SELECT g FROM GameEntity g
        JOIN GamePlayerEntity gp ON gp.game.id = g.id
        WHERE g.fromTime BETWEEN :oneHourLater AND :twentyFourHoursLater
        GROUP BY g.id
        HAVING COUNT(gp.id) < (g.membersRequired * 2)
        ORDER BY g.booking.bookingSlot.fromTime ASC
    """)
    List<GameEntity> findUpcomingGamesWithin24Hours(@Param("oneHourLater") Timestamp oneHourLater,
                                                    @Param("twentyFourHoursLater") Timestamp twentyFourHoursLater);
    
    @Query("""
        SELECT g FROM GameEntity g
        JOIN GamePlayerEntity gp ON gp.game.id = g.id
        WHERE g.booking.bookingSlot.fromTime > :twentyFourHoursLater
        GROUP BY g.id
        HAVING COUNT(gp.id) < (g.membersRequired * 2)
        ORDER BY g.booking.bookingSlot.fromTime ASC
    """)
    List<GameEntity> findExtraUpcomingGames(@Param("twentyFourHoursLater") Timestamp twentyFourHoursLater);


}
