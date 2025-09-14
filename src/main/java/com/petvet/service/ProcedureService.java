package com.petvet.service;

import com.petvet.dto.ProcedureDTO;
import com.petvet.entity.Procedure;
import com.petvet.repository.ProcedureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcedureService {
    @Autowired
    private ProcedureRepository repo;

    public List<ProcedureDTO> getAllProcedures() {
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    public ProcedureDTO getProcedureById(Long id) {
        return toDTO(repo.findById(id).orElseThrow(() -> new RuntimeException("Procedimento não encontrado")));
    }

    public ProcedureDTO createProcedure(ProcedureDTO dto) {
        Procedure proc = fromDTO(dto);
        return toDTO(repo.save(proc));
    }

    public ProcedureDTO updateProcedure(Long id, ProcedureDTO dto) {
        Procedure proc = repo.findById(id).orElseThrow(() -> new RuntimeException("Procedimento não encontrado"));
        proc.setName(dto.getName());
        proc.setPrice(dto.getPrice());
        proc.setDescription(dto.getDescription());
        return toDTO(repo.save(proc));
    }

    public void deleteProcedure(Long id) { repo.deleteById(id); }

    private ProcedureDTO toDTO(Procedure proc) {
        ProcedureDTO dto = new ProcedureDTO();
        dto.setId(proc.getId());
        dto.setName(proc.getName());
        dto.setDescription(proc.getDescription());
        dto.setPrice(proc.getPrice());
        return dto;
    }

    private Procedure fromDTO(ProcedureDTO dto) {
        Procedure proc = new Procedure();
        proc.setName(dto.getName());
        proc.setPrice(dto.getPrice());
        proc.setDescription(dto.getDescription());
        return proc;
    }
}
