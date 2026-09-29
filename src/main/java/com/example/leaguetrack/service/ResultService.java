package com.example.leaguetrack.service;

import com.example.leaguetrack.entity.Match;
import com.example.leaguetrack.entity.MatchResponse;
import com.example.leaguetrack.entity.MatchStatus;
import com.example.leaguetrack.entity.ResultRequest;
import com.example.leaguetrack.entity.StandingsEntry;
import com.example.leaguetrack.entity.Team;
import com.example.leaguetrack.entity.Tournament;
import com.example.leaguetrack.repository.MatchRepository;
import com.example.leaguetrack.repository.StandingsEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ResultService {
    private final MatchRepository matchRepo;
    private final StandingsEntryRepository standingsRepo;

    /** First-time result. Standings are updated exactly once. */
    public MatchResponse record(Long matchId, ResultRequest r) {
        Match m = findMatch(matchId);
        if (m.getStatus() == MatchStatus.COMPLETED) {
            throw BusinessException.conflict("Result already recorded for match " + matchId
                    + ". Use PUT to correct it");
        }
        apply(m, r.homeScore(), r.awayScore(), +1);
        m.setHomeScore(r.homeScore());
        m.setAwayScore(r.awayScore());
        m.setStatus(MatchStatus.COMPLETED);
        log.info("NOTIFY: Result recorded - {} {} : {} {}", m.getHomeTeam().getName(),
                r.homeScore(), r.awayScore(), m.getAwayTeam().getName());
        return MatchResponse.from(m);
    }

    /** Correct an existing result: old result is reversed first, then the new one is applied. */
    public MatchResponse correct(Long matchId, ResultRequest r) {
        Match m = findMatch(matchId);
        if (m.getStatus() != MatchStatus.COMPLETED) {
            throw BusinessException.badRequest("No result recorded yet for match " + matchId + ". Use POST first");
        }
        apply(m, m.getHomeScore(), m.getAwayScore(), -1);   // undo old result
        apply(m, r.homeScore(), r.awayScore(), +1);          // apply new result
        m.setHomeScore(r.homeScore());
        m.setAwayScore(r.awayScore());
        log.info("NOTIFY: Result corrected - {} {} : {} {}", m.getHomeTeam().getName(),
                r.homeScore(), r.awayScore(), m.getAwayTeam().getName());
        return MatchResponse.from(m);
    }

    private Match findMatch(Long id) {
        return matchRepo.findById(id)
                .orElseThrow(() -> BusinessException.notFound("Match with id " + id + " not found"));
    }

    // sign = +1 adds a result to the table, -1 removes it
    private void apply(Match m, int homeScore, int awayScore, int sign) {
        Tournament t = m.getFixture().getTournament();
        update(entry(t, m.getHomeTeam()), homeScore, awayScore, t, sign);
        update(entry(t, m.getAwayTeam()), awayScore, homeScore, t, sign);
    }

    private StandingsEntry entry(Tournament t, Team team) {
        return standingsRepo.findByTournamentIdAndTeamId(t.getId(), team.getId())
                .orElseThrow(() -> BusinessException.notFound("Standings row missing for team " + team.getName()));
    }

    private void update(StandingsEntry e, int goalsFor, int goalsAgainst, Tournament t, int sign) {
        e.setPlayed(e.getPlayed() + sign);
        e.setGoalsFor(e.getGoalsFor() + sign * goalsFor);
        e.setGoalsAgainst(e.getGoalsAgainst() + sign * goalsAgainst);
        if (goalsFor > goalsAgainst) {
            e.setWon(e.getWon() + sign);
            e.setPoints(e.getPoints() + sign * t.getWinPoints());
        } else if (goalsFor == goalsAgainst) {
            e.setDrawn(e.getDrawn() + sign);
            e.setPoints(e.getPoints() + sign * t.getDrawPoints());
        } else {
            e.setLost(e.getLost() + sign);
            e.setPoints(e.getPoints() + sign * t.getLossPoints());
        }
    }
}