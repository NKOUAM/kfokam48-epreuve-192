package com.kfokam48.evaluation.repository;

import com.kfokam48.evaluation.domain.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    List<Etudiant> findByPromotionIdOrderByNomAsc(Long promotionId);
}
