package com.kfokam48.evaluation.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exercice",
       uniqueConstraints = @UniqueConstraint(name = "uk_exercice_session_etudiant",
                                             columnNames = {"session_id", "etudiant_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Exercice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    @Column(nullable = false, length = 500)
    private String lien;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatutExercice statut;
}
