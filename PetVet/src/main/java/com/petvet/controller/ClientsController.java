package com.petvet.controller;

import com.petvet.dto.ClientDTO;
import com.petvet.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientsController {
    @Autowired
    private ClientService service;

    @GetMapping
    public List<ClientDTO> getAllClients() { return service.getAllClients(); }

    @GetMapping("/{id}")
    public ClientDTO getClientById(@PathVariable Long id) { return service.getClientById(id); }

    @PostMapping
    public ClientDTO createClient(@RequestBody ClientDTO dto) { return service.createClient(dto); }

    @PutMapping("/{id}")
    public ClientDTO updateClient(@PathVariable Long id, @RequestBody ClientDTO dto) { return service.updateClient(id, dto); }

    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable Long id) { service.deleteClient(id); }
}
