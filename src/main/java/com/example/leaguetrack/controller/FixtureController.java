package com.example.leaguetrack.controller;

import com.example.leaguetrack.entity.MatchResponse;
import com.example.leaguetrack.service.FixtureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments/{tournamentId}/fixtures")
@RequiredArgsConstructor
public class FixtureController {
    private final FixtureService fixtureService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<MatchResponse> generate(@PathVariable Long tournamentId) {
        return fixtureService.generate(tournamentId);
    }

    @GetMapping
    public List<MatchResponse> get(@PathVariable Long tournamentId) {
        return fixtureService.getFixture(tournamentId);
    }
}