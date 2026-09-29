package com.example.leaguetrack.controller;

import com.example.leaguetrack.entity.MatchResponse;
import com.example.leaguetrack.entity.ResultRequest;
import com.example.leaguetrack.service.ResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {
    private final ResultService resultService;

    @PostMapping("/{matchId}/result")
    @ResponseStatus(HttpStatus.CREATED)
    public MatchResponse record(@PathVariable Long matchId, @Valid @RequestBody ResultRequest request) {
        return resultService.record(matchId, request);
    }

    @PutMapping("/{matchId}/result")
    public MatchResponse correct(@PathVariable Long matchId, @Valid @RequestBody ResultRequest request) {
        return resultService.correct(matchId, request);
    }
}