package com.petvet.controller;

import com.petvet.dto.SaleDTO;
import com.petvet.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    @Autowired
    private SaleService service;

    @GetMapping
    public List<SaleDTO> getAllSales() { return service.getAllSales(); }

    @GetMapping("/{id}")
    public SaleDTO getSaleById(@PathVariable Long id) { return service.getSaleById(id); }

    @PostMapping
    public SaleDTO createSale(@RequestBody SaleDTO dto) { return service.createSale(dto); }

    @DeleteMapping("/{id}")
    public void deleteSale(@PathVariable Long id) { service.deleteSale(id); }
}
