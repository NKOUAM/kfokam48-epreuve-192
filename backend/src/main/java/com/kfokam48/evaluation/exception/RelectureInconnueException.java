package com.kfokam48.evaluation.exception;

import org.springframework.http.HttpStatus;

public class RelectureInconnueException extends BusinessException {
    public RelectureInconnueException() {
        super("RELECTURE_INCONNUE", "La relecture demandée est inconnue.", HttpStatus.NOT_FOUND);
    }
}
