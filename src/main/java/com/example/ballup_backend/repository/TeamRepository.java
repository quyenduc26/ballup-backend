package com.example.ballup_backend.repository;

import com.example.ballup_backend.entity.TeamEntity;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<TeamEntity, Long>, JpaSpecificationExecutor<TeamEntity> {
    Optional<TeamEntity> findByName(String name);

    @Modifying
    @Transactional
    @Query("DELETE FROM TeamEntity t WHERE t.id = :teamId")
    void deleteTeamById(@Param("teamId") Long teamId);
    
}

