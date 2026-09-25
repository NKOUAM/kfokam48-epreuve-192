package com.kfokam48.evaluation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DeposerExerciceRequest(
        @NotNull(message = "L'identifiant de session est obligatoire.")
        Long sessionId,

        @NotNull(message = "L'identifiant de l'étudiant est obligatoire.")
        Long etudiantId,

        @NotBlank(message = "Le lien est obligatoire.")
        String lien
) {}
