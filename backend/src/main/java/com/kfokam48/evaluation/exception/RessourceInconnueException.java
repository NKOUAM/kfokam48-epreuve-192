package com.kfokam48.evaluation.exception;

import org.springframework.http.HttpStatus;

public class RessourceInconnueException extends BusinessException {
    public RessourceInconnueException(String message) {
        super("RESSOURCE_INCONNUE", message, HttpStatus.NOT_FOUND);
    }
}
