package com.kfokam48.evaluation.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "presence",
       uniqueConstraints = @UniqueConstraint(name = "uk_presence_session_etudiant",
                                             columnNames = {"session_id", "etudiant_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Presence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SourcePresence source;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
