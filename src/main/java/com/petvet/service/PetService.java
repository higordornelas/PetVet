package com.petvet.service;

import com.petvet.dto.PetDTO;
import com.petvet.entity.Client;
import com.petvet.entity.Pet;
import com.petvet.repository.ClientRepository;
import com.petvet.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {
    @Autowired
    public PetRepository repo;

    @Autowired
    private ClientRepository clientRepo;

    public List<PetDTO> getAllPets() {
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    public PetDTO getPetById(Long id) {
        return toDTO(repo.findById(id).orElseThrow(() -> new RuntimeException("Animal não encontrado")));
    }

    public PetDTO createPet(PetDTO dto) {
        Pet pet = fromDTO(dto);
        return toDTO(repo.save(pet));
    }

    public PetDTO updatePet(Long id, PetDTO dto) {
        Pet pet = repo.findById(id).orElseThrow(() -> new RuntimeException("Animal não encontrado"));
        pet.setName(dto.getName());
        pet.setBreed(dto.getBreed());
        pet.setGender(dto.getGender());
        pet.setAge(dto.getAge());
        if (dto.getOwnerId() != null) {
            Client owner = clientRepo.findById(dto.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("Dono não encontrado"));
            pet.setOwner(owner);
        }
        return toDTO(repo.save(pet));
    }

    public void deletePet(Long id) { repo.deleteById(id); }

    private PetDTO toDTO(Pet pet) {
        PetDTO dto = new PetDTO();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setBreed(pet.getBreed());
        dto.setGender(pet.getGender());
        dto.setAge(pet.getAge());
        if (pet.getOwner() != null) {
            dto.setOwnerId(pet.getOwner().getId());
            dto.setOwnerName(pet.getOwner().getName());
        }
        return dto;
    }

    private Pet fromDTO(PetDTO dto) {
        Pet pet = new Pet();
        pet.setName(dto.getName());
        pet.setBreed(dto.getBreed());
        pet.setGender(dto.getGender());
        pet.setAge(dto.getAge() != null ? dto.getAge() : 0);
        if (dto.getOwnerId() != null) {
            Client owner = clientRepo.findById(dto.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("Dono não encontrado"));
            pet.setOwner(owner);
        }
        return pet;
    }
}
