package com.kfokam48.evaluation.dto.response;

public record TableauLigneResponse(
        Long etudiantId,
        String nom,
        Integer presences,
        Integer exercicesDeposes,
        Double moyenne,
        Integer relecturesEnAttente
) {}
