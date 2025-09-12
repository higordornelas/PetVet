package com.petvet.service;

import com.petvet.dto.ClientDTO;
import com.petvet.entity.Client;
import com.petvet.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    @Autowired
    private ClientRepository repo;

    public List<ClientDTO> getAllClients() {
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    public ClientDTO getClientById(Long id) {
        return toDTO(repo.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado")));
    }

    public ClientDTO createClient(ClientDTO dto) {
        Client client = fromDTO(dto);
        return toDTO(repo.save(client));
    }

    public ClientDTO updateClient(Long id, ClientDTO dto) {
        Client client = repo.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        client.setName(dto.getName());
        client.setDocument(dto.getDocument());
        client.setPhone(dto.getPhone());
        client.setAddress(dto.getAddress());
        client.setEmail(dto.getEmail());
        return toDTO(repo.save(client));
    }

    public void deleteClient(Long id) { repo.deleteById(id); }

    private ClientDTO toDTO(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setDocument(client.getDocument());
        dto.setPhone(client.getPhone());
        dto.setAddress(client.getAddress());
        dto.setEmail(client.getEmail());
        return dto;
    }

    private Client fromDTO(ClientDTO dto) {
        Client client = new Client();
        client.setName(dto.getName());
        client.setDocument(dto.getDocument());
        client.setPhone(dto.getPhone());
        client.setAddress(dto.getAddress());
        client.setEmail(dto.getEmail());
        return client;
    }
}
