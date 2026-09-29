package com.example.leaguetrack.service;

import com.example.leaguetrack.entity.StandingResponse;
import com.example.leaguetrack.entity.StandingsEntry;
import com.example.leaguetrack.repository.StandingsEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StandingsService {
    private final StandingsEntryRepository standingsRepo;
    private final TournamentService tournamentService;

    public List<StandingResponse> getStandings(Long tournamentId) {
        tournamentService.getById(tournamentId);
        List<StandingsEntry> sorted = standingsRepo.findByTournamentId(tournamentId).stream()
                .sorted(Comparator.comparingInt(StandingsEntry::getPoints).reversed()
                        .thenComparing(Comparator.comparingInt(
                                (StandingsEntry e) -> e.getGoalsFor() - e.getGoalsAgainst()).reversed())
                        .thenComparing(Comparator.comparingInt(StandingsEntry::getGoalsFor).reversed())
                        .thenComparing(e -> e.getTeam().getName()))
                .toList();

        return IntStream.range(0, sorted.size()).mapToObj(i -> {
            StandingsEntry e = sorted.get(i);
            return new StandingResponse(i + 1, e.getTeam().getId(), e.getTeam().getName(),
                    e.getPlayed(), e.getWon(), e.getDrawn(), e.getLost(),
                    e.getGoalsFor(), e.getGoalsAgainst(), e.getGoalsFor() - e.getGoalsAgainst(), e.getPoints());
        }).toList();
    }
}