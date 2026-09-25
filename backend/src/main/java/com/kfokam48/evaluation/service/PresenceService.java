package com.kfokam48.evaluation.service;

import com.kfokam48.evaluation.domain.Etudiant;
import com.kfokam48.evaluation.domain.Presence;
import com.kfokam48.evaluation.domain.Session;
import com.kfokam48.evaluation.domain.SourcePresence;
import com.kfokam48.evaluation.dto.request.MarquerPresenceRequest;
import com.kfokam48.evaluation.dto.response.PresenceResponse;
import com.kfokam48.evaluation.exception.CodeExpireException;
import com.kfokam48.evaluation.exception.CodeInconnuException;
import com.kfokam48.evaluation.exception.DejaPresentException;
import com.kfokam48.evaluation.exception.RessourceInconnueException;
import com.kfokam48.evaluation.exception.SessionClotureeException;
import com.kfokam48.evaluation.repository.EtudiantRepository;
import com.kfokam48.evaluation.repository.PresenceRepository;
import com.kfokam48.evaluation.repository.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class PresenceService {

    private final PresenceRepository presenceRepository;
    private final SessionRepository sessionRepository;
    private final EtudiantRepository etudiantRepository;
    private final TentativeCodeTracker tentativeTracker;

    public PresenceService(PresenceRepository presenceRepository,
                           SessionRepository sessionRepository,
                           EtudiantRepository etudiantRepository,
                           TentativeCodeTracker tentativeTracker) {
        this.presenceRepository = presenceRepository;
        this.sessionRepository = sessionRepository;
        this.etudiantRepository = etudiantRepository;
        this.tentativeTracker = tentativeTracker;
    }

    public PresenceResponse marquer(MarquerPresenceRequest request) {
        // RG3 : vérifier que l'étudiant n'est pas bloqué
        tentativeTracker.verifierNonBloque(request.etudiantId());

        // Rechercher la session par code
        Session session = sessionRepository.findByCode(request.code())
                .orElseGet(() -> {
                    tentativeTracker.enregistrerEchec(request.etudiantId());
                    throw new CodeInconnuException();
                });

        // RG1 : expiration du code
        if (session.getExpirationAt().isBefore(LocalDateTime.now())) {
            tentativeTracker.reinitialiser(request.etudiantId());
            throw new CodeExpireException();
        }

        // RG2 / clôture : plus de présence après clôture
        if (session.isCloturee()) {
            throw new SessionClotureeException();
        }

        // Étudiant existant ?
        Etudiant etudiant = etudiantRepository.findById(request.etudiantId())
                .orElseThrow(() -> new RessourceInconnueException("Étudiant inconnu."));

        // EF4 : une seule présence par (session, étudiant)
        if (presenceRepository.existsBySessionIdAndEtudiantId(session.getId(), etudiant.getId())) {
            throw new DejaPresentException();
        }

        // Création de la présence
        Presence presence = Presence.builder()
                .session(session)
                .etudiant(etudiant)
                .source(SourcePresence.ETUDIANT)
                .createdAt(LocalDateTime.now())
                .build();

        Presence saved = presenceRepository.save(presence);
        tentativeTracker.reinitialiser(etudiant.getId());

        return new PresenceResponse(
                saved.getId(),
                session.getId(),
                etudiant.getId(),
                saved.getSource().name()
        );
    }
}
