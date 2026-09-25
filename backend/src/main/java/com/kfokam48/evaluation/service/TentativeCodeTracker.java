package com.kfokam48.evaluation.service;

import com.kfokam48.evaluation.exception.TropDeTentativesException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Suivi en mémoire des tentatives de code par étudiant (RG3).
 * Au bout de 5 échecs, l'étudiant est bloqué 2 minutes.
 */
@Component
public class TentativeCodeTracker {

    private static final int MAX_TENTATIVES = 5;
    private static final long BLOCAGE_MINUTES = 2;

    private record Etat(int tentatives, LocalDateTime bloqueJusqua) {}

    private final Map<Long, Etat> etats = new ConcurrentHashMap<>();

    public void verifierNonBloque(Long etudiantId) {
        Etat etat = etats.get(etudiantId);
        if (etat != null && etat.bloqueJusqua() != null
                && etat.bloqueJusqua().isAfter(LocalDateTime.now())) {
            throw new TropDeTentativesException();
        }
    }

    public void enregistrerEchec(Long etudiantId) {
        etats.compute(etudiantId, (k, v) -> {
            int tentatives = (v == null ? 0 : v.tentatives()) + 1;
            LocalDateTime bloqueJusqua = null;
            if (tentatives >= MAX_TENTATIVES) {
                bloqueJusqua = LocalDateTime.now().plusMinutes(BLOCAGE_MINUTES);
                tentatives = 0;
            }
            return new Etat(tentatives, bloqueJusqua);
        });
    }

    public void reinitialiser(Long etudiantId) {
        etats.remove(etudiantId);
    }
}
