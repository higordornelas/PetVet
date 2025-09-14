package com.petvet;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.petvet.dto.ClientDTO;
import com.petvet.entity.Client;
import com.petvet.repository.ClientRepository;
import com.petvet.service.ClientService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
public class ClientServiceTest {

    @Mock
    private ClientRepository repo;

    @InjectMocks
    private ClientService service;

    public ClientServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllClients() {
        Client c = new Client(); c.setId(1L); c.setName("Yoshikage Kira");
        when(repo.findAll()).thenReturn(List.of(c));

        List<ClientDTO> clients = service.getAllClients();
        assertEquals(1, clients.size());
        assertEquals("Yoshikage Kira", clients.get(0).getName());
    }

    @Test
    void testGetClientById() {
        Client c = new Client(); c.setId(1L); c.setName("Yoshikage Kira");
        when(repo.findById(1L)).thenReturn(Optional.of(c));

        ClientDTO dto = service.getClientById(1L);
        assertEquals("Yoshikage Kira", dto.getName());
    }

    @Test
    void testCreateClient() {
        ClientDTO dto = new ClientDTO(); dto.setName("Jolyne Kujoh");
        Client saved = new Client(); saved.setId(1L); saved.setName("Jolyne Kujoh");
        when(repo.save(any(Client.class))).thenReturn(saved);

        ClientDTO result = service.createClient(dto);
        assertEquals(1L, result.getId());
        assertEquals("Jolyne Kujoh", result.getName());
    }

    @Test
    void testUpdateClient() {
        Client existing = new Client(); existing.setId(1L); existing.setName("Jonathan Joestar");
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        ClientDTO dto = new ClientDTO(); dto.setName("Dio Brando");
        ClientDTO updated = service.updateClient(1L, dto);
        assertEquals("Dio Brando", updated.getName());
    }

    @Test
    void testDeleteClient() {
        service.deleteClient(1L);
        verify(repo, times(1)).deleteById(1L);
    }
}
