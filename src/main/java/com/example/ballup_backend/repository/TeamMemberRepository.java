package com.example.ballup_backend.repository;

import com.example.ballup_backend.entity.TeamMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamMemberRepository extends JpaRepository<TeamMemberEntity, Long> {
    @Query("SELECT COUNT(tm) FROM TeamMemberEntity tm WHERE tm.team.id = :teamId")
    Long countByTeamId(Long teamId);
}

