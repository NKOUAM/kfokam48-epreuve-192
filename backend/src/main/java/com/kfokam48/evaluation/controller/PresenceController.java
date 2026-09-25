package com.kfokam48.evaluation.controller;

import com.kfokam48.evaluation.dto.request.MarquerPresenceRequest;
import com.kfokam48.evaluation.dto.response.PresenceResponse;
import com.kfokam48.evaluation.service.PresenceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/presences")
public class PresenceController {

    private final PresenceService presenceService;

    public PresenceController(PresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @PostMapping
    public ResponseEntity<PresenceResponse> marquer(@Valid @RequestBody MarquerPresenceRequest request) {
        PresenceResponse response = presenceService.marquer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
