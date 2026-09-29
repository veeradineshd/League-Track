package com.example.leaguetrack.service;

import com.example.leaguetrack.entity.Fixture;
import com.example.leaguetrack.entity.Match;
import com.example.leaguetrack.entity.MatchResponse;
import com.example.leaguetrack.entity.Team;
import com.example.leaguetrack.entity.Tournament;
import com.example.leaguetrack.repository.FixtureRepository;
import com.example.leaguetrack.repository.MatchRepository;
import com.example.leaguetrack.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FixtureService {
    private final TournamentService tournamentService;
    private final TeamRepository teamRepo;
    private final FixtureRepository fixtureRepo;
    private final MatchRepository matchRepo;

    public List<MatchResponse> generate(Long tournamentId) {
        Tournament t = tournamentService.getById(tournamentId);
        if (t.isFixtureGenerated()) {
            throw BusinessException.conflict("Fixture already generated for this tournament");
        }
        List<Team> teams = teamRepo.findByTournamentId(tournamentId);
        if (teams.size() < 2) {
            throw BusinessException.badRequest("At least 2 teams are required to generate a fixture");
        }

        Fixture fixture = new Fixture();
        fixture.setTournament(t);
        fixture.setGeneratedAt(LocalDateTime.now());
        fixture = fixtureRepo.save(fixture);

        List<Team> list = new ArrayList<>(teams);
        Collections.shuffle(list);
        if (list.size() % 2 == 1) list.add(null);       // null = "bye" for odd number of teams
        int n = list.size();

        List<Match> matches = new ArrayList<>();
        for (int round = 1; round < n; round++) {
            for (int i = 0; i < n / 2; i++) {
                Team home = list.get(i);
                Team away = list.get(n - 1 - i);
                if (home == null || away == null) continue;
                if (i == 0 && round % 2 == 0) { Team tmp = home; home = away; away = tmp; }
                Match m = new Match();
                m.setFixture(fixture);
                m.setRoundNumber(round);
                m.setHomeTeam(home);
                m.setAwayTeam(away);
                matches.add(m);
            }
            list.add(1, list.remove(n - 1));            // rotate all teams except the first
        }
        matchRepo.saveAll(matches);

        t.setFixtureGenerated(true);
        return matches.stream().map(MatchResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<MatchResponse> getFixture(Long tournamentId) {
        tournamentService.getById(tournamentId);
        Fixture f = fixtureRepo.findByTournamentId(tournamentId)
                .orElseThrow(() -> BusinessException.notFound("Fixture not generated yet for tournament " + tournamentId));
        return matchRepo.findByFixtureIdOrderByRoundNumberAscIdAsc(f.getId())
                .stream().map(MatchResponse::from).toList();
    }
}