package com.kfokam48.evaluation.controller;

import com.kfokam48.evaluation.dto.request.OuvrirSessionRequest;
import com.kfokam48.evaluation.dto.response.SessionResponse;
import com.kfokam48.evaluation.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public ResponseEntity<SessionResponse> ouvrir(@Valid @RequestBody OuvrirSessionRequest request) {
        SessionResponse response = sessionService.ouvrir(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
