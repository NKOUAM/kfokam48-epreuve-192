package com.kfokam48.evaluation.service;

import com.kfokam48.evaluation.domain.Etudiant;
import com.kfokam48.evaluation.domain.Relecture;
import com.kfokam48.evaluation.domain.StatutRelecture;
import com.kfokam48.evaluation.dto.response.TableauLigneResponse;
import com.kfokam48.evaluation.exception.PromotionInconnueException;
import com.kfokam48.evaluation.repository.EtudiantRepository;
import com.kfokam48.evaluation.repository.ExerciceRepository;
import com.kfokam48.evaluation.repository.PresenceRepository;
import com.kfokam48.evaluation.repository.PromotionRepository;
import com.kfokam48.evaluation.repository.RelectureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableauService {

    private final PromotionRepository promotionRepository;
    private final EtudiantRepository etudiantRepository;
    private final PresenceRepository presenceRepository;
    private final ExerciceRepository exerciceRepository;
    private final RelectureRepository relectureRepository;

    public TableauService(PromotionRepository promotionRepository,
                          EtudiantRepository etudiantRepository,
                          PresenceRepository presenceRepository,
                          ExerciceRepository exerciceRepository,
                          RelectureRepository relectureRepository) {
        this.promotionRepository = promotionRepository;
        this.etudiantRepository = etudiantRepository;
        this.presenceRepository = presenceRepository;
        this.exerciceRepository = exerciceRepository;
        this.relectureRepository = relectureRepository;
    }

    public List<TableauLigneResponse> tableau(Long promotionId) {
        if (!promotionRepository.existsById(promotionId)) {
            throw new PromotionInconnueException();
        }

        List<Etudiant> etudiants = etudiantRepository.findByPromotionIdOrderByNomAsc(promotionId);
        List<TableauLigneResponse> resultat = new ArrayList<>();

        for (Etudiant e : etudiants) {
            int presences = (int) presenceRepository.countByEtudiantId(e.getId());
            int exercices = (int) exerciceRepository.countByEtudiantId(e.getId());

            List<Relecture> recues = relectureRepository
                    .findRecuesParEtudiant(e.getId(), StatutRelecture.RENDUE);

            Double moyenne = recues.isEmpty()
                    ? null
                    : recues.stream().mapToInt(Relecture::getNote).average().orElse(0);

            int enAttente = (int) relectureRepository
                    .countByRelecteurIdAndStatut(e.getId(), StatutRelecture.EN_ATTENTE);

            resultat.add(new TableauLigneResponse(
                    e.getId(), e.getNom(), presences, exercices, moyenne, enAttente));
        }

        return resultat;
    }
}
