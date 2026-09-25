package com.kfokam48.evaluation.repository;

import com.kfokam48.evaluation.domain.Relecture;
import com.kfokam48.evaluation.domain.StatutRelecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface RelectureRepository extends JpaRepository<Relecture, Long> {
    Optional<Relecture> findByExerciceId(Long exerciceId);
    List<Relecture> findByRelecteurId(Long relecteurId);
    long countByRelecteurIdAndStatut(Long relecteurId, StatutRelecture statut);

    @Query("SELECT r FROM Relecture r WHERE r.exercice.etudiant.id = :etudiantId AND r.statut = :statut")
    List<Relecture> findRecuesParEtudiant(@Param("etudiantId") Long etudiantId,
                                          @Param("statut") StatutRelecture statut);
}
