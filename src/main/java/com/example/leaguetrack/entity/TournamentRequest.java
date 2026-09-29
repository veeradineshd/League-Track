package com.example.leaguetrack.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record TournamentRequest(
        @NotBlank(message = "Tournament name is required") String name,
        @NotBlank(message = "Sport is required") String sport,
        @PositiveOrZero(message = "winPoints cannot be negative") Integer winPoints,
        @PositiveOrZero(message = "drawPoints cannot be negative") Integer drawPoints,
        @PositiveOrZero(message = "lossPoints cannot be negative") Integer lossPoints) {}