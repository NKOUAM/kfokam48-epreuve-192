package com.kfokam48.evaluation.dto.request;

import jakarta.validation.constraints.NotNull;

public record RendreRelectureRequest(
        @NotNull(message = "La note est obligatoire.")
        Integer note,
        String commentaire
) {}
