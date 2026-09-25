package com.kfokam48.evaluation.service;

import com.kfokam48.evaluation.domain.Formateur;
import com.kfokam48.evaluation.domain.Promotion;
import com.kfokam48.evaluation.domain.Session;
import com.kfokam48.evaluation.dto.request.OuvrirSessionRequest;
import com.kfokam48.evaluation.dto.response.SessionResponse;
import com.kfokam48.evaluation.exception.PromotionInconnueException;
import com.kfokam48.evaluation.exception.RessourceInconnueException;
import com.kfokam48.evaluation.repository.FormateurRepository;
import com.kfokam48.evaluation.repository.PromotionRepository;
import com.kfokam48.evaluation.repository.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@Transactional
public class SessionService {

    private static final int EXPIRATION_MINUTES = 15;
    private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final SessionRepository sessionRepository;
    private final PromotionRepository promotionRepository;
    private final FormateurRepository formateurRepository;

    public SessionService(SessionRepository sessionRepository,
                          PromotionRepository promotionRepository,
                          FormateurRepository formateurRepository) {
        this.sessionRepository = sessionRepository;
        this.promotionRepository = promotionRepository;
        this.formateurRepository = formateurRepository;
    }

    public SessionResponse ouvrir(OuvrirSessionRequest request) {
        Promotion promotion = promotionRepository.findById(request.promotionId())
                .orElseThrow(PromotionInconnueException::new);

        Formateur formateur = formateurRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RessourceInconnueException("Aucun formateur enregistré."));

        LocalDateTime maintenant = LocalDateTime.now();

        Session session = Session.builder()
                .titre(request.titre())
                .code(genererCode())
                .ouvertureAt(maintenant)
                .expirationAt(maintenant.plusMinutes(EXPIRATION_MINUTES))
                .promotion(promotion)
                .formateur(formateur)
                .build();

        Session saved = sessionRepository.save(session);

        return new SessionResponse(
                saved.getId(),
                saved.getCode(),
                saved.getOuvertureAt(),
                saved.getExpirationAt()
        );
    }

    private String genererCode() {
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}
