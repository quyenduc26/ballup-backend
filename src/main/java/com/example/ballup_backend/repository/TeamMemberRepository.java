package com.example.ballup_backend.repository;

import com.example.ballup_backend.entity.TeamEntity;
import com.example.ballup_backend.entity.TeamEntity.Sport;
import com.example.ballup_backend.entity.TeamMemberEntity;
import com.example.ballup_backend.entity.UserEntity;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamMemberRepository extends JpaRepository<TeamMemberEntity, Long> {
    @Query("SELECT COUNT(tm) FROM TeamMemberEntity tm WHERE tm.team.id = :teamId")
    Long countByTeamId(Long teamId);

   @Query("SELECT tm.user FROM TeamMemberEntity tm WHERE tm.team.id = :teamId")
    List<UserEntity> findUsersByTeamId(@Param("teamId") Long teamId);

    @Query("SELECT tm.user.id FROM TeamMemberEntity tm WHERE tm.team.id = :teamId AND tm.role = 'owner'")
    Optional<Long> findOwnerByTeamId(@Param("teamId") Long teamId);

    @Query("SELECT tm FROM TeamMemberEntity tm WHERE tm.team.id = :teamId AND tm.user.id = :memberId")
    Optional<TeamMemberEntity> findByTeamIdAndMemberId(@Param("teamId") Long teamId, @Param("memberId") Long memberId);

    @Modifying
    @Transactional
    @Query("DELETE FROM TeamMemberEntity tm WHERE tm.team.id = :teamId")
    void deleteByTeamId(@Param("teamId") Long teamId);

    @Modifying
    @Query("DELETE FROM TeamMemberEntity tm WHERE tm.team = :team")
    void deleteByTeam(@Param("team") TeamEntity team);

    @Query("SELECT tm.user.id FROM TeamMemberEntity tm WHERE tm.team.id = :teamId")
    List<Long> findAllMemberIdsByTeamId(@Param("teamId") Long teamId);

    @Query("SELECT tm.team FROM TeamMemberEntity tm WHERE tm.user.id = :userId AND tm.team.sport = :sportType")
    List<TeamEntity> findTeamsByUserIdAndSport(@Param("userId") Long userId, @Param("sportType") Sport sportType);

    @Query("SELECT COUNT(tm) > 0 FROM TeamMemberEntity tm WHERE tm.user.id = :userId AND tm.team.sport = :sport")
    boolean existsByUserIdAndTeamSport(@Param("userId") Long userId, @Param("sport") Sport sport);

}

