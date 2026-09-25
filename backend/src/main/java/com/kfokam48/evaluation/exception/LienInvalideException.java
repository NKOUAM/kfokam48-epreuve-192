package com.kfokam48.evaluation.exception;

import org.springframework.http.HttpStatus;

public class LienInvalideException extends BusinessException {
    public LienInvalideException() {
        super("LIEN_INVALIDE", "Le lien fourni n'est pas une URL valide.", HttpStatus.BAD_REQUEST);
    }
}
