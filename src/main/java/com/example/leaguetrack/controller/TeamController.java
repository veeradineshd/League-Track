package com.example.leaguetrack.controller;

import com.example.leaguetrack.entity.Team;
import com.example.leaguetrack.entity.TeamRequest;
import com.example.leaguetrack.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments/{tournamentId}/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Team register(@PathVariable Long tournamentId, @Valid @RequestBody TeamRequest request) {
        return teamService.register(tournamentId, request);
    }

    @GetMapping
    public List<Team> list(@PathVariable Long tournamentId) {
        return teamService.getTeams(tournamentId);
    }
}