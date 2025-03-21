package com.example.ballup_backend.service;

import com.example.ballup_backend.dto.req.team.KickTeamMemberRequest;
import com.example.ballup_backend.dto.req.team.UpdateMemberRoleRequest;
import com.example.ballup_backend.dto.res.team.TeamOverviewResponse;
import com.example.ballup_backend.entity.NotificationEntity.NotificationType;
import com.example.ballup_backend.entity.TeamEntity;
import com.example.ballup_backend.entity.TeamMemberEntity;
import com.example.ballup_backend.entity.UserEntity;
import com.example.ballup_backend.entity.TeamEntity.SportType;
import com.example.ballup_backend.exception.BaseException;
import com.example.ballup_backend.exception.ErrorCodeEnum;
import com.example.ballup_backend.entity.TeamMemberEntity.Role;
import com.example.ballup_backend.repository.TeamRepository;
import com.example.ballup_backend.repository.UserRepository;

import jakarta.transaction.Transactional;

import com.example.ballup_backend.repository.TeamMemberRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class TeamMemberService {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private NotificationService notificationService;


    public TeamMemberEntity createTeamMember(Long userId, Long teamId, Role role) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        TeamEntity team = teamRepository.findById(teamId)
        .orElseThrow(() -> new RuntimeException("Team not found"));;

        TeamMemberEntity teamMember = TeamMemberEntity.builder()
                .user(user)
                .team(team)
                .role(role)
                .build();
        UserEntity teamCreator = teamMemberRepository.findOwnerByTeamId(team.getId());
        notificationService.createUserTeamNotification(teamCreator, team, NotificationType.TEAM_JOINED );
        return teamMemberRepository.save(teamMember);
    }

    @Transactional
    public void updateTeamMemberRole(Long memberId, UpdateMemberRoleRequest updateMemberRoleRequest) {
        TeamMemberEntity owner = teamMemberRepository.findByTeamIdAndMemberId(updateMemberRoleRequest.getTeamId(), updateMemberRoleRequest.getUserId())
            .orElseThrow(() -> new RuntimeException("Member does not belong to the specified team."));
        if (!owner.getRole().equals(Role.OWNER)) {
            throw new RuntimeException("Only the team owner can update member roles.");
        }

        TeamMemberEntity teamMember = teamMemberRepository.findByTeamIdAndMemberId(updateMemberRoleRequest.getTeamId(), memberId)
            .orElseThrow(() -> new RuntimeException("Member does not belong to the specified team."));

        teamMember.setRole(updateMemberRoleRequest.getNewRole());
        teamMemberRepository.save(teamMember);
    }

    public void kickMember(KickTeamMemberRequest kickTeamMemberRequest, Long memberId) {
        TeamMemberEntity teamMember = teamMemberRepository.findByTeamIdAndMemberId(kickTeamMemberRequest.getTeamId(), kickTeamMemberRequest.getUserId())
            .orElseThrow(() -> new BaseException(ErrorCodeEnum.USER_NOT_IN_TEAM, HttpStatus.BAD_REQUEST));

        if (teamMember.getRole() != Role.OWNER) {
            throw new BaseException(ErrorCodeEnum.FORBIDDEN, HttpStatus.FORBIDDEN);
        }

        if (kickTeamMemberRequest.getUserId().equals(memberId)) {
            throw new BaseException(ErrorCodeEnum.CANNOT_KICK_SELF, HttpStatus.BAD_REQUEST);
        }

        TeamMemberEntity memberToKick = teamMemberRepository.findByTeamIdAndMemberId(kickTeamMemberRequest.getTeamId(), memberId)
            .orElseThrow(() -> new BaseException(ErrorCodeEnum.MEMBER_NOT_FOUND, HttpStatus.NOT_FOUND));

        teamMemberRepository.delete(memberToKick);
        notificationService.createUserTeamNotification(memberToKick.getUser(), memberToKick.getTeam(), NotificationType.TEAM_JOINED );
    }

    public void leaveTeam(Long teamId, Long memberId) {
        TeamMemberEntity memberToLeave = teamMemberRepository.findByTeamIdAndMemberId(teamId, memberId)
            .orElseThrow(() -> new BaseException(ErrorCodeEnum.USER_NOT_IN_TEAM, HttpStatus.BAD_REQUEST));
        teamMemberRepository.delete(memberToLeave);
        UserEntity teamCreator = teamMemberRepository.findOwnerByTeamId(teamId);
        notificationService.createUserTeamNotification(teamCreator, memberToLeave.getTeam(), NotificationType.TEAM_LEFT );
    }
    
    @Transactional
    public void deleteByTeamId(Long teamId) {
        List<UserEntity> members = teamMemberRepository.findUsersByTeamId(teamId);
        TeamEntity team = teamRepository.findById(teamId)
            .orElseThrow(() -> new BaseException(ErrorCodeEnum.TEAM_NOT_FOUND, HttpStatus.NOT_FOUND));
        for (UserEntity member : members) {
            notificationService.createUserTeamNotification(member, team, NotificationType.TEAM_DELETED);
        }
        teamMemberRepository.deleteByTeamId(teamId);
    }


    public List<Long> getTeamMemberIds( Long userId, SportType sport) {
        Long teamId = teamMemberRepository.findTeamsByUserIdAndSportType(userId, sport);
        return teamMemberRepository.findAllMemberIdsByTeamId(teamId);
    }

    public TeamOverviewResponse getTeamByUserIdAndSport(Long userId, SportType sport) {
        TeamEntity team = teamMemberRepository.findTeamByUserIdAndSportType(userId, sport);
    
        if (team == null) {
            throw new RuntimeException("Team not found for userId: " + userId + " and sport: " + sport);
        }
        return TeamOverviewResponse.builder()
            .id(team.getId())
            .name(team.getName())
            .logo(team.getLogo())
            .cover(team.getCover()) 
            .sport(team.getSport())
            .totalMembers(teamMemberRepository.countByTeamId(team.getId())) 
            .build();
    }
    

}
