package com.kfokam48.evaluation.repository;

import com.kfokam48.evaluation.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByCode(String code);
}
