package com.example.leaguetrack.repository;

import com.example.leaguetrack.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByFixtureIdOrderByRoundNumberAscIdAsc(Long fixtureId);
}