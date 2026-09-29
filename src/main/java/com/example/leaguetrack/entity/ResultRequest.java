package com.example.leaguetrack.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ResultRequest(
        @NotNull(message = "homeScore is required") @PositiveOrZero(message = "homeScore cannot be negative") Integer homeScore,
        @NotNull(message = "awayScore is required") @PositiveOrZero(message = "awayScore cannot be negative") Integer awayScore) {}