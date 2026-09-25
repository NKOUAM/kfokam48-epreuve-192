package com.kfokam48.evaluation.repository;

import com.kfokam48.evaluation.domain.Relecture;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RelectureRepository extends JpaRepository<Relecture, Long> {
    Optional<Relecture> findByExerciceId(Long exerciceId);
    List<Relecture> findByRelecteurId(Long relecteurId);
    long countByRelecteurIdAndStatut(Long relecteurId, com.kfokam48.evaluation.domain.StatutRelecture statut);
}
