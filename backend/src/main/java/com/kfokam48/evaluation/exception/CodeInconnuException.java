package com.kfokam48.evaluation.exception;

import org.springframework.http.HttpStatus;

public class CodeInconnuException extends BusinessException {
    public CodeInconnuException() {
        super("CODE_INCONNU", "Le code de présence est inconnu.", HttpStatus.BAD_REQUEST);
    }
}
