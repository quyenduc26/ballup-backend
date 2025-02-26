package com.example.ballup_backend.repository;

import com.example.ballup_backend.entity.TeamMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMemberEntity, Long> {
    
}

