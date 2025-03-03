package com.example.ballup_backend.service;

import com.example.ballup_backend.dto.req.team.CreateTeamRequest;
import com.example.ballup_backend.dto.req.team.UpdateTeamRequest;
import com.example.ballup_backend.dto.res.team.TeamDetailResponse;
import com.example.ballup_backend.dto.res.team.TeamMemberResponse;
import com.example.ballup_backend.dto.res.team.TeamResponse;
import com.example.ballup_backend.entity.TeamEntity;
import com.example.ballup_backend.entity.TeamMemberEntity;
import com.example.ballup_backend.entity.UserEntity;
import com.example.ballup_backend.entity.TeamMemberEntity.Role;
import com.example.ballup_backend.repository.TeamRepository;
import com.example.ballup_backend.repository.UserRepository;
import com.example.ballup_backend.specification.TeamSpecification;

import jakarta.transaction.Transactional;

import com.example.ballup_backend.repository.TeamMemberRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
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
    public void createTeam(CreateTeamRequest request) {
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

    public List<TeamResponse> getAllTeams(String name, String location, TeamEntity.Sport sport, String sortBy) {
        Specification<TeamEntity> spec = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            spec = spec.and(TeamSpecification.filterByName(name));
        }
        if (location != null && !location.isEmpty()) {
            spec = spec.and(TeamSpecification.filterByLocation(location));
        }
        if (sport != null) {
            spec = spec.and(TeamSpecification.filterBySport(sport));
        }

        List<TeamEntity> teams = teamRepository.findAll(spec);

        List<TeamResponse> teamResponses = teams.stream().map(team -> {
            Long totalMembers = teamMemberRepository.countByTeamId(team.getId());
            return TeamResponse.builder()
                    .id(team.getId())
                    .name(team.getName())
                    .address(team.getAddress())
                    .intro(team.getIntro())
                    .logo(team.getLogo())
                    .cover(team.getCover())
                    .sport(team.getSport())
                    .totalMembers(totalMembers)
                    .build();
        }).collect(Collectors.toList());

        teamResponses.sort(Comparator.comparingLong((TeamResponse team) -> team.getTotalMembers() == null ? 0L : team.getTotalMembers()).reversed());

        return teamResponses;
    }

    public TeamDetailResponse getTeamById(Long teamId, Long userId) {
        TeamEntity team = teamRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found"));
    
        List<UserEntity> teamMembers = teamMemberRepository.findUsersByTeamId(teamId);
    
        List<TeamMemberResponse> memberResponses = teamMembers.stream()
            .map(teamMember -> TeamMemberResponse.builder()
                .id(teamMember.getId())
                .name(teamMember.getLastName().concat(" " +teamMember.getFirstName()))
                .username(teamMember.getUsername())
                .avatar(teamMember.getAvatar())
                .height(teamMember.getHeight())
                .weight(teamMember.getWeight())
                .build())
            .collect(Collectors.toList());

        Long ownerId = teamMemberRepository.findOwnerByTeamId(teamId)
            .orElse(null); // Trả về null nếu không có owner nào
    
        boolean isOwner = ownerId != null && ownerId.equals(userId);
    
        return TeamDetailResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .address(team.getAddress())
                .intro(team.getIntro())
                .logo(team.getLogo())
                .cover(team.getCover())
                .sport(team.getSport())
                .members(memberResponses) 
                .isOwner(isOwner)
                .build();
    }
    
    @Transactional
    public void updateTeam(Long teamId, UpdateTeamRequest request) {
        TeamEntity team = teamRepository.getReferenceById(teamId); 

        if (request.getName() != null) team.setName(request.getName());
        if (request.getAddress() != null) team.setAddress(request.getAddress());
        if (request.getIntro() != null) team.setIntro(request.getIntro());
        if (request.getLogo() != null) team.setLogo(request.getLogo());
        if (request.getCover() != null) team.setCover(request.getCover());

        teamRepository.save(team);
    }


}
