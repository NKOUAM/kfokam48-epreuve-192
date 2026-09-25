package com.kfokam48.evaluation.service;

import com.kfokam48.evaluation.domain.Etudiant;
import com.kfokam48.evaluation.domain.Exercice;
import com.kfokam48.evaluation.domain.Relecture;
import com.kfokam48.evaluation.domain.Session;
import com.kfokam48.evaluation.domain.StatutExercice;
import com.kfokam48.evaluation.dto.request.DeposerExerciceRequest;
import com.kfokam48.evaluation.dto.response.ExerciceResponse;
import com.kfokam48.evaluation.dto.response.RelecturePubliqueResponse;
import com.kfokam48.evaluation.exception.ExerciceDejaDeposeException;
import com.kfokam48.evaluation.exception.LienInvalideException;
import com.kfokam48.evaluation.exception.RessourceInconnueException;
import com.kfokam48.evaluation.exception.SessionClotureeException;
import com.kfokam48.evaluation.exception.SessionInconnueException;
import com.kfokam48.evaluation.repository.EtudiantRepository;
import com.kfokam48.evaluation.repository.ExerciceRepository;
import com.kfokam48.evaluation.repository.RelectureRepository;
import com.kfokam48.evaluation.repository.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@Transactional
public class ExerciceService {

    private final ExerciceRepository exerciceRepository;
    private final SessionRepository sessionRepository;
    private final EtudiantRepository etudiantRepository;
    private final RelectureRepository relectureRepository;
    private final AssignationRelecteurService assignationRelecteurService;

    public ExerciceService(ExerciceRepository exerciceRepository,
                           SessionRepository sessionRepository,
                           EtudiantRepository etudiantRepository,
                           RelectureRepository relectureRepository,
                           AssignationRelecteurService assignationRelecteurService) {
        this.exerciceRepository = exerciceRepository;
        this.sessionRepository = sessionRepository;
        this.etudiantRepository = etudiantRepository;
        this.relectureRepository = relectureRepository;
        this.assignationRelecteurService = assignationRelecteurService;
    }

    public ExerciceResponse deposer(DeposerExerciceRequest request) {
        if (!estUrlValide(request.lien())) {
            throw new LienInvalideException();
        }
        Session session = sessionRepository.findById(request.sessionId())
                .orElseThrow(SessionInconnueException::new);
        Etudiant etudiant = etudiantRepository.findById(request.etudiantId())
                .orElseThrow(() -> new RessourceInconnueException("Étudiant inconnu."));
        if (session.isCloturee()) {
            throw new SessionClotureeException();
        }
        if (exerciceRepository.existsBySessionIdAndEtudiantId(session.getId(), etudiant.getId())) {
            throw new ExerciceDejaDeposeException();
        }
        Exercice exercice = Exercice.builder()
                .session(session).etudiant(etudiant)
                .lien(request.lien()).statut(StatutExercice.DEPOSE).build();
        Exercice saved = exerciceRepository.save(exercice);
        assignationRelecteurService.tenterAssignation(saved);
        Exercice finalEx = exerciceRepository.findById(saved.getId()).orElse(saved);
        return new ExerciceResponse(finalEx.getId(), finalEx.getStatut().name());
    }

    public RelecturePubliqueResponse consulterRelecture(Long exerciceId) {
        Relecture relecture = relectureRepository.findByExerciceId(exerciceId)
                .orElseThrow(() -> new RessourceInconnueException("Aucune relecture pour cet exercice."));
        return new RelecturePubliqueResponse(
                relecture.getNote(), relecture.getCommentaire(), relecture.getStatut().name());
    }

    private boolean estUrlValide(String lien) {
        try {
            URI uri = new URI(lien);
            return uri.isAbsolute() && uri.getScheme() != null
                    && (uri.getScheme().equals("http") || uri.getScheme().equals("https"));
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
