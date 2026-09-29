package com.example.leaguetrack.service;

import com.example.leaguetrack.entity.Tournament;
import com.example.leaguetrack.entity.TournamentRequest;
import com.example.leaguetrack.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TournamentService {
    private final TournamentRepository repo;

    public Tournament create(TournamentRequest r) {
        String name = r.name().trim();
        if (repo.existsByNameIgnoreCase(name)) {
            throw BusinessException.conflict("Tournament '" + name + "' already exists");
        }
        int win  = r.winPoints()  == null ? 3 : r.winPoints();
        int draw = r.drawPoints() == null ? 1 : r.drawPoints();
        int loss = r.lossPoints() == null ? 0 : r.lossPoints();
        // BUSINESS RULE: scoring rule must be sensible
        if (!(win > draw && draw >= loss)) {
            throw BusinessException.badRequest("Scoring rule must satisfy: winPoints > drawPoints >= lossPoints");
        }
        Tournament t = new Tournament();
        t.setName(name);
        t.setSport(r.sport().trim());
        t.setWinPoints(win);
        t.setDrawPoints(draw);
        t.setLossPoints(loss);
        return repo.save(t);
    }

    @Transactional(readOnly = true)
    public List<Tournament> getAll() { return repo.findAll(); }

    @Transactional(readOnly = true)
    public Tournament getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> BusinessException.notFound("Tournament with id " + id + " not found"));
    }
}