package com.example.leaguetrack.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "standings_entries",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tournament_id", "team_id"}))
@Getter @Setter @NoArgsConstructor
public class StandingsEntry {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @ManyToOne(optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(name = "played", nullable = false)        private int played;
    @Column(name = "won", nullable = false)           private int won;
    @Column(name = "drawn", nullable = false)         private int drawn;
    @Column(name = "lost", nullable = false)          private int lost;
    @Column(name = "goals_for", nullable = false)     private int goalsFor;
    @Column(name = "goals_against", nullable = false) private int goalsAgainst;
    @Column(name = "points", nullable = false)        private int points;
}