package com.petvet.service;

import com.petvet.dto.ConsultationDTO;
import com.petvet.dto.PetDTO;
import com.petvet.entity.Consultation;
import com.petvet.entity.Pet;
import com.petvet.repository.ConsultationRepository;
import com.petvet.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultationService {
    @Autowired
    private ConsultationRepository repo;
    @Autowired private PetRepository petRepo;

    public List<ConsultationDTO> getAllConsultations() {
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    public ConsultationDTO getConsultationById(Long id) {
        return toDTO(repo.findById(id).orElseThrow(() -> new RuntimeException("Consulta não encontrada")));
    }

    public ConsultationDTO createConsultation(ConsultationDTO dto) {
        Consultation c = new Consultation();
        c.setType(dto.getType());
        c.setNotes(dto.getNotes());
        c.setDate(dto.getDate());
        c.setPrice(dto.getPrice());
        Pet pet = petRepo.findById(dto.getPet().getId())
                .orElseThrow(() -> new RuntimeException("Pet não encontrado"));
        c.setPet(pet);
        return toDTO(repo.save(c));
    }

    public ConsultationDTO updateConsultation(Long id, ConsultationDTO dto) {
        Consultation c = repo.findById(id).orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
        c.setType(dto.getType());
        c.setNotes(dto.getNotes());
        c.setDate(dto.getDate());
        c.setPrice(dto.getPrice());
        Pet pet = petRepo.findById(dto.getPet().getId())
                .orElseThrow(() -> new RuntimeException("Pet não encontrado"));
        c.setPet(pet);
        return toDTO(repo.save(c));
    }

    public void deleteConsultation(Long id) { repo.deleteById(id); }

    private ConsultationDTO toDTO(Consultation c) {
        ConsultationDTO dto = new ConsultationDTO();
        dto.setId(c.getId());
        dto.setType(c.getType());
        dto.setNotes(c.getNotes());
        dto.setDate(c.getDate());
        dto.setPrice(c.getPrice());
        PetDTO petDto = new PetDTO();
        petDto.setId(c.getPet().getId());
        petDto.setName(c.getPet().getName());
        dto.setPet(petDto);
        return dto;
    }
}