package com.kfokam48.evaluation.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "formateur")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Formateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nom;
}
