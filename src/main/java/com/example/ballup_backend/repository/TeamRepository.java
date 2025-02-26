package com.example.ballup_backend.repository;

import com.example.ballup_backend.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
    Optional<TeamEntity> findByName(String name);
}

