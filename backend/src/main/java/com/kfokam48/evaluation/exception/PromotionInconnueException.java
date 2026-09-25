package com.kfokam48.evaluation.exception;

import org.springframework.http.HttpStatus;

public class PromotionInconnueException extends BusinessException {
    public PromotionInconnueException() {
        super("PROMOTION_INCONNUE", "La promotion demandée est inconnue.", HttpStatus.NOT_FOUND);
    }
}
