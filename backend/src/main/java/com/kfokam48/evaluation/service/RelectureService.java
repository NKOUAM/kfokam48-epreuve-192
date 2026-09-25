package com.kfokam48.evaluation.service;

import com.kfokam48.evaluation.domain.Exercice;
import com.kfokam48.evaluation.domain.Relecture;
import com.kfokam48.evaluation.domain.StatutExercice;
import com.kfokam48.evaluation.domain.StatutRelecture;
import com.kfokam48.evaluation.dto.request.RendreRelectureRequest;
import com.kfokam48.evaluation.exception.AutoRelectureException;
import com.kfokam48.evaluation.exception.NoteInvalideException;
import com.kfokam48.evaluation.exception.RelectureDejaRendueException;
import com.kfokam48.evaluation.exception.RelectureInconnueException;
import com.kfokam48.evaluation.repository.ExerciceRepository;
import com.kfokam48.evaluation.repository.RelectureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class RelectureService {

    private final RelectureRepository relectureRepository;
    private final ExerciceRepository exerciceRepository;

    public RelectureService(RelectureRepository relectureRepository,
                            ExerciceRepository exerciceRepository) {
        this.relectureRepository = relectureRepository;
        this.exerciceRepository = exerciceRepository;
    }

    public void rendre(Long relectureId, RendreRelectureRequest request) {
        Relecture relecture = relectureRepository.findById(relectureId)
                .orElseThrow(RelectureInconnueException::new);

        if (request.note() == null || request.note() < 0 || request.note() > 20) {
            throw new NoteInvalideException();
        }

        if (relecture.getRelecteur().getId().equals(relecture.getExercice().getEtudiant().getId())) {
            throw new AutoRelectureException();
        }

        if (relecture.getStatut() == StatutRelecture.RENDUE
                && relecture.getExercice().getSession().isCloturee()) {
            throw new RelectureDejaRendueException();
        }

        relecture.setNote(request.note());
        relecture.setCommentaire(request.commentaire());
        relecture.setStatut(StatutRelecture.RENDUE);
        relecture.setUpdatedAt(LocalDateTime.now());
        relectureRepository.save(relecture);

        Exercice exercice = relecture.getExercice();
        exercice.setStatut(StatutExercice.RELU);
        exerciceRepository.save(exercice);
    }
}
