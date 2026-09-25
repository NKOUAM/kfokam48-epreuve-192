package com.kfokam48.evaluation.exception;

import org.springframework.http.HttpStatus;

public class AutoRelectureException extends BusinessException {
    public AutoRelectureException() {
        super("AUTO_RELECTURE", "Un étudiant ne peut pas relire son propre exercice.", HttpStatus.FORBIDDEN);
    }
}
