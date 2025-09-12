package com.petvet.controller;

import com.petvet.dto.ProcedureHistoryDTO;
import com.petvet.service.ProcedureHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/procedure-histories")
public class ProcedureHistoriesController {
    @Autowired
    private ProcedureHistoryService service;

    @GetMapping
    public List<ProcedureHistoryDTO> getAllHistories() { return service.getAllHistories(); }

    @GetMapping("/{id}")
    public ProcedureHistoryDTO getHistoryById(@PathVariable Long id) { return service.getHistoryById(id); }

    @PostMapping
    public ProcedureHistoryDTO createHistory(@RequestBody ProcedureHistoryDTO dto) { return service.createHistory(dto); }

    @PutMapping("/{id}")
    public ProcedureHistoryDTO updateHistory(@PathVariable Long id, @RequestBody ProcedureHistoryDTO dto) { return service.updateHistory(id, dto); }

    @DeleteMapping("/{id}")
    public void deleteHistory(@PathVariable Long id) { service.deleteHistory(id); }
}
