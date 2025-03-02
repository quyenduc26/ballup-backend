package com.example.ballup_backend.repository;

import com.example.ballup_backend.entity.TeamMemberEntity;
import com.example.ballup_backend.entity.UserEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamMemberRepository extends JpaRepository<TeamMemberEntity, Long> {
    @Query("SELECT COUNT(tm) FROM TeamMemberEntity tm WHERE tm.team.id = :teamId")
    Long countByTeamId(Long teamId);

   @Query("SELECT tm.user FROM TeamMemberEntity tm WHERE tm.team.id = :teamId")
    List<UserEntity> findUsersByTeamId(@Param("teamId") Long teamId);

    @Query("SELECT tm.user.id FROM TeamMemberEntity tm WHERE tm.team.id = :teamId AND tm.role = 'owner'")
    Optional<Long> findOwnerByTeamId(@Param("teamId") Long teamId);

    
}

