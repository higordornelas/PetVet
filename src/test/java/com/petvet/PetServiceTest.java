package com.petvet;

import com.petvet.dto.PetDTO;
import com.petvet.entity.Pet;
import com.petvet.repository.PetRepository;
import com.petvet.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetServiceTest {

    @Mock
    private PetRepository repo;

    private PetService petService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        petService = new PetService();
        petService.repo = repo;
    }

    @Test
    void testCreatePet() {
        PetDTO dto = new PetDTO();
        dto.setName("Iggy");
        dto.setBreed("Labrador");
        dto.setGender("Macho");
        dto.setAge(3);

        Pet petEntity = new Pet();
        petEntity.setId(1L);
        petEntity.setName(dto.getName());
        petEntity.setBreed(dto.getBreed());
        petEntity.setGender(dto.getGender());
        petEntity.setAge(dto.getAge());

        when(repo.save(any(Pet.class))).thenReturn(petEntity);

        PetDTO result = petService.createPet(dto);

        assertNotNull(result.getId());
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getBreed(), result.getBreed());
        assertEquals(dto.getGender(), result.getGender());
        assertEquals(dto.getAge(), result.getAge());
    }

    @Test
    void testGetAllPets() {
        Pet p1 = new Pet();
        p1.setId(1L); p1.setName("Iggy"); p1.setBreed("Labrador"); p1.setGender("Macho"); p1.setAge(3);
        Pet p2 = new Pet();
        p2.setId(2L); p2.setName("Nina"); p2.setBreed("Poodle"); p2.setGender("Fêmea"); p2.setAge(2);

        when(repo.findAll()).thenReturn(List.of(p1, p2));

        List<PetDTO> pets = petService.getAllPets();
        assertEquals(2, pets.size());
    }

    @Test
    void testGetPetByIdFound() {
        Pet p = new Pet();
        p.setId(1L); p.setName("Iggy"); p.setBreed("Labrador"); p.setGender("Macho"); p.setAge(3);
        when(repo.findById(1L)).thenReturn(Optional.of(p));

        PetDTO dto = petService.getPetById(1L);
        assertEquals(1L, dto.getId());
        assertEquals("Iggy", dto.getName());
    }

    @Test
    void testGetPetByIdNotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> petService.getPetById(1L));
        assertEquals("Animal não encontrado", ex.getMessage());
    }

    @Test
    void testUpdatePet() {
        Pet existingPet = new Pet();
        existingPet.setId(1L);
        existingPet.setName("Iggy");
        existingPet.setBreed("Labrador");
        existingPet.setGender("Macho");
        existingPet.setAge(3);

        PetDTO updateDTO = new PetDTO();
        updateDTO.setName("Sadaharu");
        updateDTO.setBreed("Golden Retriever");
        updateDTO.setGender("Macho");
        updateDTO.setAge(4);

        when(repo.findById(1L)).thenReturn(Optional.of(existingPet));
        when(repo.save(any(Pet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PetDTO result = petService.updatePet(1L, updateDTO);

        assertEquals(1L, result.getId());
        assertEquals("Sadaharu", result.getName());
        assertEquals("Golden Retriever", result.getBreed());
        assertEquals(4, result.getAge());
    }

    @Test
    void testDeletePet() {
        doNothing().when(repo).deleteById(1L);
        petService.deletePet(1L);
        verify(repo, times(1)).deleteById(1L);
    }
}
