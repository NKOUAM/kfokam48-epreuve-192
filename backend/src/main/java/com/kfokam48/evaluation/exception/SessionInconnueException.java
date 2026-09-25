package com.kfokam48.evaluation.exception;

import org.springframework.http.HttpStatus;

public class SessionInconnueException extends BusinessException {
    public SessionInconnueException() {
        super("SESSION_INCONNUE", "La session demandée est inconnue.", HttpStatus.NOT_FOUND);
    }
}
