package com.petvet.service;

import com.petvet.dto.PetDTO;
import com.petvet.dto.ProcedureDTO;
import com.petvet.dto.ProcedureHistoryDTO;
import com.petvet.entity.Pet;
import com.petvet.entity.Procedure;
import com.petvet.entity.ProcedureHistory;
import com.petvet.repository.PetRepository;
import com.petvet.repository.ProcedureHistoryRepository;
import com.petvet.repository.ProcedureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcedureHistoryService {
    @Autowired
    public ProcedureHistoryRepository repo;

    @Autowired
    public PetRepository petRepo;

    @Autowired
    public ProcedureRepository procedureRepo;

    public List<ProcedureHistoryDTO> getAllHistories() {
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    public ProcedureHistoryDTO getHistoryById(Long id) {
        return toDTO(repo.findById(id).orElseThrow(() -> new RuntimeException("Histórico de Procedimento não encontrado")));
    }

    public ProcedureHistoryDTO createHistory(ProcedureHistoryDTO dto) {
        ProcedureHistory ph = fromDTO(dto);
        return getProcedureHistoryDTO(dto, ph);
    }

    private ProcedureHistoryDTO getProcedureHistoryDTO(ProcedureHistoryDTO dto, ProcedureHistory ph) {
        Pet pet = petRepo.findById(dto.getPet().getId()).orElseThrow(() -> new RuntimeException("Animal não encontrado"));
        Procedure procedure = procedureRepo.findById(dto.getProcedure().getId()).orElseThrow(() -> new RuntimeException("Procedimento não encontrado"));

        ph.setPet(pet);
        ph.setProcedure(procedure);
        ph.setPrice(procedure.getPrice());
        ph.setNotes(dto.getNotes());

        return toDTO(repo.save(ph));
    }

    public ProcedureHistoryDTO updateHistory(Long id, ProcedureHistoryDTO dto) {
        ProcedureHistory ph = repo.findById(id).orElseThrow(() -> new RuntimeException("Histórico de Procedimento não encontrado"));
        ph.setDate(dto.getDate());
        ph.setVeterinarian(dto.getVeterinarian());
        ph.setPrice(dto.getPrice());
        ph.setNotes(dto.getNotes());

        return getProcedureHistoryDTO(dto, ph);
    }

    public void deleteHistory(Long id) { repo.deleteById(id); }

    private ProcedureHistoryDTO toDTO(ProcedureHistory ph) {
        ProcedureHistoryDTO dto = new ProcedureHistoryDTO();
        dto.setId(ph.getId());

        ProcedureDTO procedureDTO = new ProcedureDTO();
        procedureDTO.setId(ph.getProcedure().getId());
        procedureDTO.setName(ph.getProcedure().getName());
        procedureDTO.setPrice(ph.getProcedure().getPrice());
        dto.setProcedure(procedureDTO);

        PetDTO petDTO = new PetDTO();
        petDTO.setId(ph.getPet().getId());
        petDTO.setName(ph.getPet().getName());
        petDTO.setBreed(ph.getPet().getBreed());
        petDTO.setGender(ph.getPet().getGender());
        petDTO.setAge(ph.getPet().getAge());
        dto.setPet(petDTO);

        dto.setVeterinarian(ph.getVeterinarian());
        dto.setDate(ph.getDate());
        dto.setPrice(ph.getPrice());
        dto.setNotes(ph.getNotes());

        return dto;
    }

    private ProcedureHistory fromDTO(ProcedureHistoryDTO dto) {
        ProcedureHistory ph = new ProcedureHistory();

        ph.setVeterinarian(dto.getVeterinarian());
        ph.setDate(dto.getDate());
        ph.setPrice(dto.getPrice());
        ph.setNotes(dto.getNotes());

        return ph;
    }
}
