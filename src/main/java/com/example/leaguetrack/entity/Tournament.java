package com.example.leaguetrack.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tournaments")
@Getter @Setter @NoArgsConstructor
public class Tournament {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "sport", nullable = false, length = 50)
    private String sport;

    @Column(name = "win_points", nullable = false)
    private int winPoints = 3;

    @Column(name = "draw_points", nullable = false)
    private int drawPoints = 1;

    @Column(name = "loss_points", nullable = false)
    private int lossPoints = 0;

    @Column(name = "fixture_generated", nullable = false)
    private boolean fixtureGenerated = false;
}