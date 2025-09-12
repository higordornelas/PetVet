package com.petvet.service;

import com.petvet.dto.ProductDTO;
import com.petvet.entity.Product;
import com.petvet.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    public ProductRepository repo;

    public List<ProductDTO> getAllProducts() {
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    public ProductDTO getProductById(Long id) {
        return toDTO(repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado")));
    }

    public Product findEntityById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public ProductDTO createProduct(ProductDTO dto) {
        Product p = new Product();
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setType(dto.getType());
        p.setPrice(dto.getPrice());
        p.setQuantity(dto.getQuantity());
        return toDTO(repo.save(p));
    }

    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        Product p = findEntityById(id);
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setType(dto.getType());
        p.setPrice(dto.getPrice());
        p.setQuantity(dto.getQuantity());
        return toDTO(repo.save(p));
    }

    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }

    public void updateStock(Long id, int quantityChange) {
        Product product = findEntityById(id);
        int newQuantity = (product.getQuantity() != null ? product.getQuantity() : 0) + quantityChange;

        if (newQuantity < 0) {
            throw new RuntimeException("Sem estoque suficiente para o produto: " + product.getName());
        }

        product.setQuantity(newQuantity);
        repo.save(product);
    }

    public ProductDTO toDTO(Product p) {
        ProductDTO dto = new ProductDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setType(p.getType());
        dto.setPrice(p.getPrice());
        dto.setQuantity(p.getQuantity());
        return dto;
    }
}
