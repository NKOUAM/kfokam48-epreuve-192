package com.kfokam48.evaluation.exception;

import org.springframework.http.HttpStatus;

public class CodeExpireException extends BusinessException {
    public CodeExpireException() {
        super("CODE_EXPIRE", "Le code de présence a expiré.", HttpStatus.GONE);
    }
}
