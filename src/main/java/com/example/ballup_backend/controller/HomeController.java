package com.example.ballup_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ballup_backend.dto.res.game.GameResponse;
import com.example.ballup_backend.entity.TeamEntity;
import com.example.ballup_backend.service.GameService;
import com.example.ballup_backend.service.TeamService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private GameService gameService;

    @Autowired
    private TeamService teamService;

    HomeController(TeamService teamService) {
        this.teamService = teamService;
    }
    
    @GetMapping
    public ResponseEntity<List<TeamEntity>> getHeroSections() {
        // return ResponseEntity.status(HttpStatus.OK).body(gameService.getGamesForHomepage());
        return ResponseEntity.status(HttpStatus.OK).body(teamService.getTeamForHomepage());
    }
    
}
