package com.example.leaguetrack.repository;

import com.example.leaguetrack.entity.StandingsEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StandingsEntryRepository extends JpaRepository<StandingsEntry, Long> {
    List<StandingsEntry> findByTournamentId(Long tournamentId);
    Optional<StandingsEntry> findByTournamentIdAndTeamId(Long tournamentId, Long teamId);
}