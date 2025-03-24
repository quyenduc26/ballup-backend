package com.example.ballup_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ballup_backend.dto.res.center.CardPlayingCenterResponse;
import com.example.ballup_backend.dto.res.game.GameResponse;
import com.example.ballup_backend.dto.res.home.HomeResponse;
import com.example.ballup_backend.dto.res.team.TeamResponse;
import com.example.ballup_backend.entity.TeamEntity;
import com.example.ballup_backend.service.GameService;
import com.example.ballup_backend.service.PlayingCenterService;
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

    @Autowired
    private PlayingCenterService playingCenterService;

    HomeController(TeamService teamService) {
        this.teamService = teamService;
    }
    
    @GetMapping
    public ResponseEntity<HomeResponse> getHomeSections() {
        List<GameResponse> games = gameService.getGamesForHomepage();
        List<TeamResponse> teams = teamService.getTeamForHomepage();
        List<CardPlayingCenterResponse> centers =  playingCenterService.getCenterForHomepage();

        HomeResponse homeResponse = HomeResponse.builder()
                .games(games)
                .teams(teams)
                .centers(centers)
                .build();

        return ResponseEntity.ok(homeResponse);
    }
    
}
