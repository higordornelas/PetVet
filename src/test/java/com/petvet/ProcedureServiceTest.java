package com.petvet;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.petvet.dto.ProcedureDTO;
import com.petvet.entity.Procedure;
import com.petvet.repository.ProcedureRepository;
import com.petvet.service.ProcedureService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
public class ProcedureServiceTest {

    @Mock
    private ProcedureRepository repo;

    @InjectMocks
    private ProcedureService service;

    public ProcedureServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProcedures() {
        Procedure p = new Procedure(); p.setId(1L); p.setName("Vacina");
        when(repo.findAll()).thenReturn(List.of(p));

        List<ProcedureDTO> list = service.getAllProcedures();
        assertEquals(1, list.size());
        assertEquals("Vacina", list.get(0).getName());
    }

    @Test
    void testCreateProcedure() {
        ProcedureDTO dto = new ProcedureDTO(); dto.setName("Exame");
        Procedure saved = new Procedure(); saved.setId(1L); saved.setName("Exame");
        when(repo.save(any(Procedure.class))).thenReturn(saved);

        ProcedureDTO result = service.createProcedure(dto);
        assertEquals("Exame", result.getName());
    }

    @Test
    void testUpdateProcedure() {
        Procedure existing = new Procedure(); existing.setId(1L); existing.setName("Velho");
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        ProcedureDTO dto = new ProcedureDTO(); dto.setName("Novo");
        ProcedureDTO updated = service.updateProcedure(1L, dto);
        assertEquals("Novo", updated.getName());
    }

    @Test
    void testDeleteProcedure() {
        service.deleteProcedure(1L);
        verify(repo, times(1)).deleteById(1L);
    }
}
