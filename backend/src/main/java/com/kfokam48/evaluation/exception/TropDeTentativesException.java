package com.kfokam48.evaluation.exception;

import org.springframework.http.HttpStatus;

public class TropDeTentativesException extends BusinessException {
    public TropDeTentativesException() {
        super("TROP_DE_TENTATIVES", "Trop de tentatives, réessayez dans 2 minutes.", HttpStatus.TOO_MANY_REQUESTS);
    }
}
