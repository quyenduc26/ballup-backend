package com.example.ballup_backend.controller;

import com.example.ballup_backend.dto.req.team.CreateTeamRequest;
import com.example.ballup_backend.dto.req.team.UpdateTeamRequest;
import com.example.ballup_backend.dto.res.team.TeamDetailResponse;
import com.example.ballup_backend.dto.res.team.TeamOverviewResponse;
import com.example.ballup_backend.dto.res.team.TeamResponse;
import com.example.ballup_backend.entity.TeamEntity;
import com.example.ballup_backend.service.TeamService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public ResponseEntity<List<TeamResponse>> getAllTeams(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "sport", required = false) TeamEntity.SportType sport,
            @RequestParam(value = "sortBy", required = false) String sortBy) {

        List<TeamResponse> teams = teamService.getAllTeams(name, location, sport, sortBy);
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/{teamId}/user/{userId}")
    public ResponseEntity<TeamDetailResponse> getTeamDetail(@PathVariable Long teamId, @PathVariable Long userId) {
        return ResponseEntity.ok(teamService.getTeamById(teamId, userId));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTeam(@RequestBody CreateTeamRequest request) {
        teamService.createTeam(request);
        return ResponseEntity.ok("Team created successfully!");
    }


    @PostMapping("/join")
    public ResponseEntity<String> joinTeam(@RequestParam Long userId, @RequestParam Long teamId) {
        String response = teamService.joinTeam(userId, teamId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{teamId}")
    public ResponseEntity<String> updateTeam( @PathVariable Long teamId, @RequestBody UpdateTeamRequest request) {
        teamService.updateTeam(teamId, request);
        return ResponseEntity.ok("Team updated successfully");
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<String> deleteTeam( @PathVariable Long teamId, @RequestParam Long userId) {
        teamService.deleteTeam(teamId, userId);
        return ResponseEntity.ok("Team deleted successfully");
    }

    @GetMapping("/{teamId}/overview")
    public ResponseEntity<TeamOverviewResponse> getTeamByUserIdAndSport( @PathVariable Long teamId) {
        return ResponseEntity.ok(teamService.getTeamOverview(teamId));
    }
}
