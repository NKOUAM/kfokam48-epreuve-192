package com.kfokam48.evaluation.exception;

import org.springframework.http.HttpStatus;

public class DejaPresentException extends BusinessException {
    public DejaPresentException() {
        super("DEJA_PRESENT", "Cette présence est déjà enregistrée.", HttpStatus.CONFLICT);
    }
}
