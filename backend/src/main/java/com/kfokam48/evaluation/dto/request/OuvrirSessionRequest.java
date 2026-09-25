package com.kfokam48.evaluation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OuvrirSessionRequest(
        @NotBlank(message = "Le titre est obligatoire.")
        String titre,

        @NotNull(message = "L'identifiant de promotion est obligatoire.")
        Long promotionId
) {}
