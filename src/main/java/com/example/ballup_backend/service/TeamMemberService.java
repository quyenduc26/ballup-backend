package com.example.ballup_backend.service;

import com.example.ballup_backend.dto.req.team.UpdateMemberRoleRequest;
import com.example.ballup_backend.entity.TeamEntity;
import com.example.ballup_backend.entity.TeamMemberEntity;
import com.example.ballup_backend.entity.UserEntity;
import com.example.ballup_backend.entity.TeamMemberEntity.Role;
import com.example.ballup_backend.repository.TeamRepository;
import com.example.ballup_backend.repository.UserRepository;

import jakarta.transaction.Transactional;

import com.example.ballup_backend.repository.TeamMemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TeamMemberService {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;


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

        return teamMemberRepository.save(teamMember);
    }

    @Transactional
    public void updateTeamMemberRole(Long memberId, UpdateMemberRoleRequest updateMemberRoleRequest) {
        System.out.println(updateMemberRoleRequest.getTeamId() + "" + memberId + "" + memberId);

        TeamMemberEntity owner = teamMemberRepository.findByTeamIdAndMemberId(updateMemberRoleRequest.getTeamId(), updateMemberRoleRequest.getUserId())
            .orElseThrow(() -> new RuntimeException("Member does not belong to the specified team."));
        if (!owner.getRole().equals(Role.OWNER)) {
            throw new RuntimeException("Only the team owner can update member roles.");
        }
        System.out.println(updateMemberRoleRequest.getTeamId() + "" + memberId);

        TeamMemberEntity teamMember = teamMemberRepository.findByTeamIdAndMemberId(updateMemberRoleRequest.getTeamId(), memberId)
            .orElseThrow(() -> new RuntimeException("Member does not belong to the specified team."));

        teamMember.setRole(updateMemberRoleRequest.getNewRole());
        teamMemberRepository.save(teamMember);
    }


}
