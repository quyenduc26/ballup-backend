package com.example.ballup_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ballup_backend.dto.req.game.CreateGameRequest;
import com.example.ballup_backend.dto.req.game.UpdateGameInfoRequest;
import com.example.ballup_backend.dto.req.game.UpdateGameTimeAndSlotRequest;
import com.example.ballup_backend.dto.res.game.GameResponse;
import com.example.ballup_backend.dto.res.game.MyGameResponse;
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

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MyGameResponse>> getUserGame(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(gameService.getMyGames(userId));
    }

    @GetMapping()
    public ResponseEntity<List<GameResponse>> getGamesWithOnlyTeamA(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String sport) {
        List<GameResponse> games = gameService.getGamesWithOnlyTeamA(name, address, sport);
        return ResponseEntity.ok(games);
    }

    @PatchMapping("/update/info")
    public ResponseEntity<String> updateGameInfo(@RequestBody UpdateGameInfoRequest updateData) {
        gameService.updateGameInfo(updateData);
        return ResponseEntity.status(HttpStatus.OK).body("Game updated successfully!");
    }

    @PatchMapping("/update/time-slot")
    public ResponseEntity<String> updateGameTimeOrSlot(@RequestBody UpdateGameTimeAndSlotRequest updateData) {
        gameService.updateGameTimeAndSlot(updateData);
        return ResponseEntity.status(HttpStatus.OK).body("Game updated successfully!");
    }

    @PostMapping("/{gameId}/join")
    public ResponseEntity<String> joinGame( @PathVariable Long gameId, @RequestParam Long userId) {
        gameService.joinGame(gameId, userId);
        return ResponseEntity.ok("Joined the game successfully");
    }

    @PostMapping("/{gameId}/join-team")
    public ResponseEntity<String> joinGameAsTeam( @PathVariable Long gameId, @RequestParam Long userId) {
        gameService.joinGameAsTeam(gameId, userId);
        return ResponseEntity.ok("Team joined the game successfully");
    }
}
