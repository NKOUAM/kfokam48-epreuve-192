package com.kfokam48.evaluation.exception;

import org.springframework.http.HttpStatus;

public class RelectureDejaRendueException extends BusinessException {
    public RelectureDejaRendueException() {
        super("RELECTURE_DEJA_RENDUE", "Cette relecture a déjà été rendue et la session est clôturée.", HttpStatus.CONFLICT);
    }
}
