package com.example.ballup_backend.dto.res.home;

import java.util.List;

import com.example.ballup_backend.dto.res.center.CardPlayingCenterResponse;
import com.example.ballup_backend.dto.res.game.GameResponse;
import com.example.ballup_backend.dto.res.team.TeamResponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HomeResponse {
    private List<GameResponse> games;
    private List<TeamResponse> teams;
    private List<CardPlayingCenterResponse> centers;
}
