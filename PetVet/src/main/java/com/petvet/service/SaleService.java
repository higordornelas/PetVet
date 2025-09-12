package com.petvet.service;

import com.petvet.dto.ProductDTO;
import com.petvet.dto.SaleDTO;
import com.petvet.dto.SaleItemDTO;
import com.petvet.entity.Product;
import com.petvet.entity.Sale;
import com.petvet.entity.SaleItem;
import com.petvet.repository.ProductRepository;
import com.petvet.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService {

    @Autowired
    public SaleRepository repo;

    @Autowired
    public ProductRepository productRepo;

    @Autowired
    public ProductService productService;

    public SaleDTO createSale(SaleDTO dto) {
        Sale sale = new Sale();
        sale.setDate(LocalDate.now());
        sale.setItems(new ArrayList<>());

        double total = 0;

        for (SaleItemDTO itemDTO : dto.getItems()) {
            Product product = productService.findEntityById(itemDTO.getProduct().getId());

            if (product.getQuantity() < itemDTO.getQuantity()) {
                throw new RuntimeException("Sem estoque suficiente do produto: " + product.getName());
            }

            productService.updateStock(product.getId(), -itemDTO.getQuantity());

            SaleItem saleItem = new SaleItem();
            saleItem.setProduct(product);
            saleItem.setQuantity(itemDTO.getQuantity());
            saleItem.setPrice(product.getPrice());
            sale.getItems().add(saleItem);

            total += product.getPrice() * itemDTO.getQuantity();
        }

        sale.setTotal(total);
        return toDTO(repo.save(sale));
    }

    public void deleteSale(Long saleId) {
        Sale sale = repo.findById(saleId).orElseThrow(() -> new RuntimeException("Venda não encontrada"));

        for (SaleItem item : sale.getItems()) {
            productService.updateStock(item.getProduct().getId(), item.getQuantity());
        }

        repo.deleteById(saleId);
    }

    public SaleDTO toDTO(Sale sale) {
        SaleDTO dto = new SaleDTO();
        dto.setId(sale.getId());
        dto.setDate(sale.getDate());
        dto.setTotal(sale.getTotal());

        List<SaleItemDTO> items = sale.getItems().stream().map(item -> {
            SaleItemDTO iDTO = new SaleItemDTO();

            ProductDTO pDTO = new ProductDTO();
            pDTO.setId(item.getProduct().getId());
            pDTO.setName(item.getProduct().getName());
            pDTO.setPrice(item.getProduct().getPrice());

            iDTO.setProduct(pDTO);
            iDTO.setQuantity(item.getQuantity());
            iDTO.setPrice(item.getPrice());
            return iDTO;
        }).collect(Collectors.toList());

        dto.setItems(items);
        return dto;
    }

    public List<SaleDTO> getAllSales() {
        return repo.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public SaleDTO getSaleById(Long id) {
        Sale sale = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));
        return toDTO(sale);
    }
}
