package com.example.leaguetrack.entity;

public record StandingResponse(int rank, Long teamId, String team, int played, int won, int drawn,
                               int lost, int goalsFor, int goalsAgainst, int goalDifference, int points) {}