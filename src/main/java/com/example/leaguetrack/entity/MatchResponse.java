package com.example.leaguetrack.entity;

public record MatchResponse(Long id, int round, String homeTeam, String awayTeam,
                            Integer homeScore, Integer awayScore, MatchStatus status, String winner) {
    public static MatchResponse from(Match m) {
        String winner = null;
        if (m.getStatus() == MatchStatus.COMPLETED) {
            if (m.getHomeScore() > m.getAwayScore()) winner = m.getHomeTeam().getName();
            else if (m.getHomeScore() < m.getAwayScore()) winner = m.getAwayTeam().getName();
            else winner = "DRAW";
        }
        return new MatchResponse(m.getId(), m.getRoundNumber(), m.getHomeTeam().getName(),
                m.getAwayTeam().getName(), m.getHomeScore(), m.getAwayScore(), m.getStatus(), winner);
    }
}