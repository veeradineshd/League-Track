package com.example.leaguetrack.repository;

import com.example.leaguetrack.entity.Fixture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FixtureRepository extends JpaRepository<Fixture, Long> {
    Optional<Fixture> findByTournamentId(Long tournamentId);
}