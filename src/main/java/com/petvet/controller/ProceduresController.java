package com.petvet.controller;

import com.petvet.dto.ProcedureDTO;
import com.petvet.service.ProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/procedures")
public class ProceduresController {

    @Autowired
    private ProcedureService service;

    @GetMapping
    public List<ProcedureDTO> getAllProcedures() { return service.getAllProcedures(); }

    @GetMapping("/{id}")
    public ProcedureDTO getProcedureById(@PathVariable Long id) { return service.getProcedureById(id); }

    @PostMapping
    public ProcedureDTO createProcedure(@RequestBody ProcedureDTO dto) { return service.createProcedure(dto); }

    @PutMapping("/{id}")
    public ProcedureDTO updateProcedure(@PathVariable Long id, @RequestBody ProcedureDTO dto) { return service.updateProcedure(id, dto); }

    @DeleteMapping("/{id}")
    public void deleteProcedure(@PathVariable Long id) { service.deleteProcedure(id); }
}
