package com.example.ballup_backend.service;

import com.example.ballup_backend.dto.req.team.CreateTeamRequest;
import com.example.ballup_backend.dto.req.team.UpdateTeamRequest;
import com.example.ballup_backend.dto.res.team.TeamDetailResponse;
import com.example.ballup_backend.dto.res.team.TeamMemberResponse;
import com.example.ballup_backend.dto.res.team.TeamOverviewResponse;
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
    
        // Kiểm tra xem user đã tham gia team nào có cùng sport hay chưa
        boolean isAlreadyInTeam = teamMemberRepository.existsByUserIdAndTeamSportType(userId, team.getSport());
        
        if (isAlreadyInTeam) {
            throw new RuntimeException("You are already a member of a team in the same sport ");
        }
    
        // Thêm user vào team nếu chưa tham gia team nào có cùng sport
        TeamMemberEntity teamMember = TeamMemberEntity.builder()
            .team(team)
            .user(user)
            .role(Role.MEMBER)
            .build();
    
        teamMemberRepository.save(teamMember);
        return "User " + user.getUsername() + " has successfully joined the team: " + team.getName();
    }
    

    public List<TeamResponse> getAllTeams(String name, String location, TeamEntity.SportType sport, String sortBy) {
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
        .map(teamMember -> {
            String fullName = (teamMember.getLastName() == null ? "" : teamMember.getLastName()) + 
                              " " + 
                              (teamMember.getFirstName() == null ? "" : teamMember.getFirstName());
            fullName = fullName.trim().isEmpty() ? "Anonymous" : fullName.trim();
    
            return TeamMemberResponse.builder()
                .id(teamMember.getId())
                .name(fullName)
                .username(teamMember.getUsername())
                .avatar(teamMember.getAvatar())
                .height(teamMember.getHeight())
                .weight(teamMember.getWeight())
                .build();
        })
        .collect(Collectors.toList());
    

        Long ownerId = teamMemberRepository.findOwnerByTeamId(teamId)
            .orElse(null); 
    
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

        if (request.getName() != null && !request.getName().equals("")) team.setName(request.getName());
        if (request.getAddress() != null && !request.getAddress().equals("")) team.setAddress(request.getAddress());
        if (request.getIntro() != null && !request.getIntro().equals("")) team.setIntro(request.getIntro());
        if (request.getLogo() != null && !request.getLogo().equals("")) team.setLogo(request.getLogo());
        if (request.getCover() != null && !request.getCover().equals("")) team.setCover(request.getCover());

        teamRepository.save(team);
    }

    @Transactional
    public void deleteTeam(Long teamId, Long userId) {

        TeamMemberEntity owner = teamMemberRepository.findByTeamIdAndMemberId(teamId, userId)
            .orElseThrow(() -> new RuntimeException("Member does not belong to the specified team."));

        if (!owner.getRole().equals(Role.OWNER)) {
            throw new RuntimeException("Only the team owner can update member roles.");
        }

        TeamEntity team = teamRepository.getReferenceById(teamId);
        
        teamMemberRepository.deleteByTeamId(teamId);

        teamRepository.deleteTeamById(teamId);
    }

    public TeamOverviewResponse getTeamOverview(Long teamId) {
        TeamEntity team = teamRepository.getReferenceById(teamId);
    
        if (team == null) {
            throw new RuntimeException("Team not found!");
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

    public List<TeamEntity> getTeamForHomepage() {
        List<TeamEntity> allTeams = teamMemberRepository.findTopTeamsWithMostMembers();
        return allTeams.size() > 6 ? allTeams.subList(0, 6) : allTeams;
    }

    public List<TeamOverviewResponse> getMyTeams(Long userId) {
        List<TeamEntity> teams = teamMemberRepository.findTeamsByUserId(userId)
            .stream()
            .collect(Collectors.toList());
    
        return teams.stream()
            .map(team -> TeamOverviewResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .logo(team.getLogo())
                .sport(team.getSport())
                .totalMembers(teamMemberRepository.countByTeamId(team.getId())) 
                .build())
            .collect(Collectors.toList());
    }
    
}
