package com.example.ballup_backend.service;

import com.example.ballup_backend.dto.req.team.CreateTeamRequest;
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
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private TeamMemberService teamMemberService;

    @Transactional
    public String createTeam(CreateTeamRequest request) {
        TeamEntity team = TeamEntity.builder()
            .name(request.getName())
            .address(request.getAddress())
            .intro(request.getIntro())
            .logo(request.getLogo())
            .cover(request.getCover())
            .sport(request.getSport())
            .build();
        TeamEntity savedTeam = teamRepository.save(team);
        UserEntity user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        teamMemberService.createTeamMember(user.getId(), savedTeam.getId(), Role.OWNER);
        return "Create team successfully";
    }

    public String joinTeam(Long userId, Long teamId) {
        TeamEntity team = teamRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found"));
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        TeamMemberEntity teamMember = TeamMemberEntity.builder()
            .team(team)
            .user(user)
            .build();
        teamMemberRepository.save(teamMember);

        return "User " + user.getUsername() + " joined team " + team.getName();
    }

}
