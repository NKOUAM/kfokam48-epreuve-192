package com.kfokam48.evaluation.exception;

import org.springframework.http.HttpStatus;

public class ExerciceDejaDeposeException extends BusinessException {
    public ExerciceDejaDeposeException() {
        super("EXERCICE_DEJA_DEPOSE", "Un exercice a déjà été déposé pour cette session.", HttpStatus.CONFLICT);
    }
}
