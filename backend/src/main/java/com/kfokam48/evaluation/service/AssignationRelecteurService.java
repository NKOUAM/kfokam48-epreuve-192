package com.kfokam48.evaluation.service;

import com.kfokam48.evaluation.domain.Etudiant;
import com.kfokam48.evaluation.domain.Exercice;
import com.kfokam48.evaluation.domain.Presence;
import com.kfokam48.evaluation.domain.Relecture;
import com.kfokam48.evaluation.domain.StatutExercice;
import com.kfokam48.evaluation.domain.StatutRelecture;
import com.kfokam48.evaluation.repository.ExerciceRepository;
import com.kfokam48.evaluation.repository.PresenceRepository;
import com.kfokam48.evaluation.repository.RelectureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AssignationRelecteurService {

    private final PresenceRepository presenceRepository;
    private final RelectureRepository relectureRepository;
    private final ExerciceRepository exerciceRepository;
    private final SecureRandom random = new SecureRandom();

    public AssignationRelecteurService(PresenceRepository presenceRepository,
                                       RelectureRepository relectureRepository,
                                       ExerciceRepository exerciceRepository) {
        this.presenceRepository = presenceRepository;
        this.relectureRepository = relectureRepository;
        this.exerciceRepository = exerciceRepository;
    }

    public boolean tenterAssignation(Exercice exercice) {
        if (relectureRepository.findByExerciceId(exercice.getId()).isPresent()) {
            return true;
        }

        Long auteurId = exercice.getEtudiant().getId();
        Long sessionId = exercice.getSession().getId();

        List<Etudiant> candidats = presenceRepository.findBySessionId(sessionId).stream()
                .map(Presence::getEtudiant)
                .filter(e -> !e.getId().equals(auteurId))
                .toList();

        if (candidats.isEmpty()) {
            return false;
        }

        Etudiant relecteur = candidats.get(random.nextInt(candidats.size()));

        Relecture relecture = Relecture.builder()
                .exercice(exercice)
                .relecteur(relecteur)
                .statut(StatutRelecture.EN_ATTENTE)
                .createdAt(LocalDateTime.now())
                .build();
        relectureRepository.save(relecture);

        exercice.setStatut(StatutExercice.EN_ATTENTE_RELECTURE);
        exerciceRepository.save(exercice);

        return true;
    }

    public void tenterAssignationsEnAttente(Long sessionId) {
        List<Exercice> exercices = exerciceRepository.findBySessionId(sessionId);
        for (Exercice ex : exercices) {
            if (ex.getStatut() == StatutExercice.DEPOSE) {
                tenterAssignation(ex);
            }
        }
    }
}
