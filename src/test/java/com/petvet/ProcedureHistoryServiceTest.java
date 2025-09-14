package com.petvet;

import com.petvet.dto.PetDTO;
import com.petvet.dto.ProcedureDTO;
import com.petvet.dto.ProcedureHistoryDTO;
import com.petvet.entity.Pet;
import com.petvet.entity.Procedure;
import com.petvet.entity.ProcedureHistory;
import com.petvet.repository.PetRepository;
import com.petvet.repository.ProcedureHistoryRepository;
import com.petvet.repository.ProcedureRepository;
import com.petvet.service.ProcedureHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class ProcedureHistoryServiceTest {

    @Mock
    private ProcedureHistoryRepository repo;

    @Mock
    private PetRepository petRepo;

    @Mock
    private ProcedureRepository procedureRepo;

    private ProcedureHistoryService service;

    private Pet pet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        service = new ProcedureHistoryService();
        service.repo = repo;
        service.petRepo = petRepo;
        service.procedureRepo = procedureRepo;

        pet = new Pet();
        pet.setId(1L);
        pet.setName("Iggy");
        pet.setBreed("Labrador");
        pet.setGender("Macho");
        pet.setAge(3);
    }

    @Test
    void testCreateProcedureHistory() {
        PetDTO petDTO = new PetDTO();
        petDTO.setId(1L);
        petDTO.setName("Iggy");
        petDTO.setBreed("Labrador");
        petDTO.setGender("Macho");
        petDTO.setAge(3);

        ProcedureDTO procedureDTO = new ProcedureDTO();
        procedureDTO.setId(1L);
        procedureDTO.setName("Consulta");
        procedureDTO.setPrice(100.0);

        ProcedureHistoryDTO dto = new ProcedureHistoryDTO();
        dto.setPet(petDTO);
        dto.setProcedure(procedureDTO);
        dto.setDate(LocalDate.now());
        dto.setPrice(100.0);
        dto.setNotes("Sem observações");
        dto.setVeterinarian("Dr. Robert Speedwagon");

        when(petRepo.findById(1L)).thenReturn(Optional.of(pet));
        Procedure procedure = new Procedure();
        procedure.setId(1L);
        procedure.setName("Consulta");
        procedure.setPrice(100.0);
        when(procedureRepo.findById(1L)).thenReturn(Optional.of(procedure));

        ProcedureHistory entity = new ProcedureHistory();
        entity.setId(1L);
        entity.setPet(pet);
        entity.setProcedure(procedure);
        entity.setDate(dto.getDate());
        entity.setPrice(dto.getPrice());
        entity.setNotes(dto.getNotes());
        entity.setVeterinarian(dto.getVeterinarian());

        when(repo.save(any(ProcedureHistory.class))).thenReturn(entity);

        ProcedureHistoryDTO result = service.createHistory(dto);

        assertEquals(1L, result.getId());
        assertEquals("Consulta", result.getProcedure().getName());
        assertEquals(100.0, result.getPrice());
        assertEquals("Iggy", result.getPet().getName());
        assertEquals("Dr. Robert Speedwagon", result.getVeterinarian());
    }

    @Test
    void testGetAllProcedureHistories() {
        ProcedureHistory ph1 = new ProcedureHistory();
        ph1.setId(1L);
        ph1.setProcedure(new Procedure());
        ph1.setPet(pet);
        ph1.setDate(LocalDate.now());
        ph1.setPrice(100.0);
        ph1.setVeterinarian("Dr. Robert Speedwagon");

        ProcedureHistory ph2 = new ProcedureHistory();
        ph2.setId(2L);
        ph2.setProcedure(new Procedure());
        ph2.setPet(pet);
        ph2.setDate(LocalDate.now());
        ph2.setPrice(50.0);
        ph2.setVeterinarian("Dr. Jotaro Kujoh");

        when(repo.findAll()).thenReturn(List.of(ph1, ph2));

        List<ProcedureHistoryDTO> list = service.getAllHistories();
        assertEquals(2, list.size());
    }

    @Test
    void testGetProcedureHistoryByIdFound() {
        ProcedureHistory ph = new ProcedureHistory();
        ph.setId(1L);
        ph.setProcedure(new Procedure());
        ph.setPet(pet);
        ph.setDate(LocalDate.now());
        ph.setPrice(100.0);
        ph.setVeterinarian("Dr. Bruno Bucciarati");

        when(repo.findById(1L)).thenReturn(Optional.of(ph));

        ProcedureHistoryDTO dto = service.getHistoryById(1L);
        assertEquals(1L, dto.getId());
        assertEquals("Dr. Bruno Bucciarati", dto.getVeterinarian());
    }

    @Test
    void testGetProcedureHistoryByIdNotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.getHistoryById(1L));
        assertEquals("Histórico de Procedimento não encontrado", ex.getMessage());
    }

    @Test
    void testUpdateProcedureHistory() {
        Procedure procedure = new Procedure();
        procedure.setId(1L);
        procedure.setName("Vacina");
        procedure.setPrice(50.0);

        ProcedureHistory existing = new ProcedureHistory();
        existing.setId(1L);
        existing.setProcedure(procedure);
        existing.setPet(pet);
        existing.setDate(LocalDate.now());
        existing.setPrice(100.0);
        existing.setVeterinarian("Dr. Giorno Giovanna");

        ProcedureDTO procedureDTO = new ProcedureDTO();
        procedureDTO.setId(1L);
        procedureDTO.setName("Vacina");
        procedureDTO.setPrice(50.0);

        PetDTO petDTO = new PetDTO();
        petDTO.setId(1L);
        petDTO.setName("Iggy");
        petDTO.setBreed("Labrador");
        petDTO.setGender("Macho");
        petDTO.setAge(3);

        ProcedureHistoryDTO updateDTO = new ProcedureHistoryDTO();
        updateDTO.setProcedure(procedureDTO);
        updateDTO.setPet(petDTO);
        updateDTO.setDate(LocalDate.now());
        updateDTO.setPrice(50.0);
        updateDTO.setNotes("Atualizado");
        updateDTO.setVeterinarian("Dr. Robert Speedwagon");

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(petRepo.findById(1L)).thenReturn(Optional.of(pet));
        when(procedureRepo.findById(1L)).thenReturn(Optional.of(procedure));
        when(repo.save(any(ProcedureHistory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProcedureHistoryDTO result = service.updateHistory(1L, updateDTO);

        assertEquals("Vacina", result.getProcedure().getName());
        assertEquals(50.0, result.getPrice());
        assertEquals("Atualizado", result.getNotes());
        assertEquals("Dr. Robert Speedwagon", result.getVeterinarian());
    }

    @Test
    void testDeleteProcedureHistory() {
        ProcedureHistory ph = new ProcedureHistory();
        ph.setId(1L);
        ph.setPet(pet);

        when(repo.findById(1L)).thenReturn(Optional.of(ph));
        doNothing().when(repo).deleteById(1L);

        service.deleteHistory(1L);
        verify(repo, times(1)).deleteById(1L);
    }
}
