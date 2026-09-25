package com.kfokam48.evaluation.controller;

import com.kfokam48.evaluation.dto.response.TableauLigneResponse;
import com.kfokam48.evaluation.service.TableauService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tableau")
public class TableauController {

    private final TableauService tableauService;

    public TableauController(TableauService tableauService) {
        this.tableauService = tableauService;
    }

    @GetMapping
    public ResponseEntity<List<TableauLigneResponse>> tableau(@RequestParam Long promotionId) {
        return ResponseEntity.ok(tableauService.tableau(promotionId));
    }
}
