package com.example.ballup_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.ballup_backend.entity.PlayingCenterEntity;
import com.example.ballup_backend.entity.UserEntity;

@Repository
public interface PlayingCenterRepository extends JpaRepository<PlayingCenterEntity, Long>,  JpaSpecificationExecutor<PlayingCenterEntity>{
    List<PlayingCenterEntity> findByOwner(UserEntity owner);

    @Query("SELECT c.id FROM PlayingCenterEntity c WHERE c.owner = :owner")
    List<Long> findCenterIdsByOwner(UserEntity owner);

}
