package com.kfokam48.evaluation.exception;

import org.springframework.http.HttpStatus;

public class NoteInvalideException extends BusinessException {
    public NoteInvalideException() {
        super("NOTE_INVALIDE", "La note doit être un entier entre 0 et 20.", HttpStatus.BAD_REQUEST);
    }
}
