package com.example.ballup_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ballup_backend.dto.req.game.CreateGameRequest;
import com.example.ballup_backend.service.GameService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {
    
    @Autowired
    private GameService gameService;

    @PostMapping("/create")
    public ResponseEntity<String> createGame(@RequestBody CreateGameRequest request) {
        gameService.createGame(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Game created successfully!");
    }
}
