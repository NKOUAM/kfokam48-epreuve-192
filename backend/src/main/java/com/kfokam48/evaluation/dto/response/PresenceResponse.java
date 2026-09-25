package com.kfokam48.evaluation.dto.response;

public record PresenceResponse(
        Long id,
        Long sessionId,
        Long etudiantId,
        String source
) {}
