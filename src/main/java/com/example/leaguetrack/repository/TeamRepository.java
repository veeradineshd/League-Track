package com.example.leaguetrack.repository;

import com.example.leaguetrack.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByTournamentId(Long tournamentId);
    boolean existsByTournamentIdAndNameIgnoreCase(Long tournamentId, String name);
}