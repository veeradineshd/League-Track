package com.example.leaguetrack.entity;

import jakarta.validation.constraints.NotBlank;

public record TeamRequest(@NotBlank(message = "Team name is required") String name) {}