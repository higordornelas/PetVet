package com.petvet.controller;
import com.petvet.dto.PetDTO;
import com.petvet.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetsController {

    @Autowired
    private PetService petService;

    @GetMapping
    public List<PetDTO> getAllPets() {
        return petService.getAllPets();
    }

    @GetMapping("/{id}")
    public PetDTO getPetById(@PathVariable Long id) {
        return petService.getPetById(id);
    }

    @PostMapping
    public PetDTO createPet(@RequestBody PetDTO petDTO) {
        return petService.createPet(petDTO);
    }

    @PutMapping("/{id}")
    public PetDTO updatePet(@PathVariable Long id, @RequestBody PetDTO petDTO) {
        return petService.updatePet(id, petDTO);
    }

    @DeleteMapping("/{id}")
    public void deletePet(@PathVariable Long id) {
        petService.deletePet(id);
    }
}
