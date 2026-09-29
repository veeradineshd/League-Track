package com.example.leaguetrack.repository;

import com.example.leaguetrack.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    boolean existsByNameIgnoreCase(String name);
}