package com.example.ballup_backend.controller;

import com.example.ballup_backend.dto.req.team.CreateTeamRequest;
import com.example.ballup_backend.service.TeamService;

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

    @PostMapping("/create")
    public ResponseEntity<String> createTeam(@RequestBody CreateTeamRequest request) {
        String team = teamService.createTeam(request);
        return ResponseEntity.ok("Team created successfully!");
    }

    @PostMapping("/join")
    public ResponseEntity<String> joinTeam(@RequestParam Long userId, @RequestParam Long teamId) {
        String response = teamService.joinTeam(userId, teamId);
        return ResponseEntity.ok(response);
    }
}
