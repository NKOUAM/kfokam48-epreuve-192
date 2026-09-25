package com.kfokam48.evaluation.dto.request;

import jakarta.validation.constraints.NotNull;

public record AjouterPresenceFormateurRequest(
        @NotNull(message = "L'identifiant de session est obligatoire.")
        Long sessionId,

        @NotNull(message = "L'identifiant de l'étudiant est obligatoire.")
        Long etudiantId
) {}
