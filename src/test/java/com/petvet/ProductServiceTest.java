package com.petvet;

import com.petvet.dto.ProductDTO;
import com.petvet.entity.Product;
import com.petvet.repository.ProductRepository;
import com.petvet.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepo;

    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService();
        productService.repo = productRepo;

        product = new Product();
        product.setId(1L);
        product.setName("Ração");
        product.setDescription("Ração gourmet");
        product.setType("Comida");
        product.setPrice(10.0);
        product.setQuantity(10);

        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(productRepo.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void testGetAllProducts() {
        when(productRepo.findAll()).thenReturn(List.of(product));
        List<ProductDTO> products = productService.getAllProducts();
        assertEquals(1, products.size());
        assertEquals("Ração", products.get(0).getName());
    }

    @Test
    void testGetProductById() {
        ProductDTO dto = productService.getProductById(1L);
        assertEquals("Ração", dto.getName());
    }

    @Test
    void testCreateProduct() {
        ProductDTO dto = new ProductDTO();
        dto.setName("Shampoo");
        dto.setDescription("Para pets");
        dto.setType("Banho");
        dto.setPrice(15.0);
        dto.setQuantity(20);

        ProductDTO result = productService.createProduct(dto);
        assertEquals("Shampoo", result.getName());
        assertEquals(20, result.getQuantity());
    }

    @Test
    void testUpdateProduct() {
        ProductDTO dto = new ProductDTO();
        dto.setName("Ração Atualizada");
        dto.setDescription("Nova descrição");
        dto.setType("Comida");
        dto.setPrice(12.0);
        dto.setQuantity(15);

        ProductDTO updated = productService.updateProduct(1L, dto);
        assertEquals("Ração Atualizada", updated.getName());
        assertEquals(15, updated.getQuantity());
    }

    @Test
    void testDeleteProduct() {
        productService.deleteProduct(1L);
        verify(productRepo, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateStockReduce() {
        productService.updateStock(1L, -5);
        assertEquals(5, product.getQuantity());
    }

    @Test
    void testUpdateStockAdd() {
        productService.updateStock(1L, 10);
        assertEquals(20, product.getQuantity());
    }

    @Test
    void testUpdateStockNotEnough() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> productService.updateStock(1L, -20));
        assertTrue(ex.getMessage().contains("Sem estoque suficiente"));
    }
}
