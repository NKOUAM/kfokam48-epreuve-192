package com.kfokam48.evaluation.repository;

import com.kfokam48.evaluation.domain.Presence;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PresenceRepository extends JpaRepository<Presence, Long> {
    boolean existsBySessionIdAndEtudiantId(Long sessionId, Long etudiantId);
    List<Presence> findBySessionId(Long sessionId);
    long countByEtudiantId(Long etudiantId);
}
