package com.example.leaguetrack.controller;

import com.example.leaguetrack.entity.StandingResponse;
import com.example.leaguetrack.entity.Tournament;
import com.example.leaguetrack.entity.TournamentRequest;
import com.example.leaguetrack.service.StandingsService;
import com.example.leaguetrack.service.TournamentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
public class TournamentController {
    private final TournamentService tournamentService;
    private final StandingsService standingsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tournament create(@Valid @RequestBody TournamentRequest request) {
        return tournamentService.create(request);
    }

    @GetMapping
    public List<Tournament> getAll() { return tournamentService.getAll(); }

    @GetMapping("/{id}")
    public Tournament getOne(@PathVariable Long id) { return tournamentService.getById(id); }

    @GetMapping("/{id}/standings")
    public List<StandingResponse> standings(@PathVariable Long id) {
        return standingsService.getStandings(id);
    }
}