package com.example.leaguetrack.service;

import com.example.leaguetrack.entity.StandingsEntry;
import com.example.leaguetrack.entity.Team;
import com.example.leaguetrack.entity.TeamRequest;
import com.example.leaguetrack.entity.Tournament;
import com.example.leaguetrack.repository.StandingsEntryRepository;
import com.example.leaguetrack.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {
    private final TeamRepository teamRepo;
    private final StandingsEntryRepository standingsRepo;
    private final TournamentService tournamentService;

    public Team register(Long tournamentId, TeamRequest r) {
        Tournament t = tournamentService.getById(tournamentId);
        if (t.isFixtureGenerated()) {
            throw BusinessException.conflict("Fixture already generated. Teams can no longer be registered");
        }
        String name = r.name().trim();
        if (teamRepo.existsByTournamentIdAndNameIgnoreCase(tournamentId, name)) {
            throw BusinessException.conflict("Team '" + name + "' is already registered in this tournament");
        }
        Team team = new Team();
        team.setName(name);
        team.setTournament(t);
        team = teamRepo.save(team);

        // every team starts with a zero row in the points table
        StandingsEntry entry = new StandingsEntry();
        entry.setTournament(t);
        entry.setTeam(team);
        standingsRepo.save(entry);
        return team;
    }

    @Transactional(readOnly = true)
    public List<Team> getTeams(Long tournamentId) {
        tournamentService.getById(tournamentId);
        return teamRepo.findByTournamentId(tournamentId);
    }
}