package com.kfokam48.evaluation.exception;

import org.springframework.http.HttpStatus;

public class SessionClotureeException extends BusinessException {
    public SessionClotureeException() {
        super("SESSION_CLOTUREE", "La session est clôturée.", HttpStatus.CONFLICT);
    }
}
