package com.kfokam48.evaluation.dto.response;

import java.time.LocalDateTime;

public record SessionResponse(
        Long id,
        String code,
        LocalDateTime ouvertureAt,
        LocalDateTime expirationAt
) {}
