package com.kfokam48.evaluation.controller;

import com.kfokam48.evaluation.dto.request.RendreRelectureRequest;
import com.kfokam48.evaluation.service.RelectureService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/relectures")
public class RelectureController {

    private final RelectureService relectureService;

    public RelectureController(RelectureService relectureService) {
        this.relectureService = relectureService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> rendre(@PathVariable Long id,
                                        @Valid @RequestBody RendreRelectureRequest request) {
        relectureService.rendre(id, request);
        return ResponseEntity.ok().build();
    }
}
