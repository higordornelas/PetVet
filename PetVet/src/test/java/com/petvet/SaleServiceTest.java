package com.petvet;

import com.petvet.dto.ProductDTO;
import com.petvet.dto.SaleDTO;
import com.petvet.dto.SaleItemDTO;
import com.petvet.entity.Product;
import com.petvet.entity.Sale;
import com.petvet.entity.SaleItem;
import com.petvet.repository.ProductRepository;
import com.petvet.repository.SaleRepository;
import com.petvet.service.ProductService;
import com.petvet.service.SaleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaleServiceTest {

    @Mock
    private SaleRepository saleRepo;

    @Mock
    private ProductRepository productRepo;

    private ProductService productService;
    private SaleService saleService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product1 = new Product();
        product1.setId(1L);
        product1.setName("Ração");
        product1.setPrice(10.0);
        product1.setQuantity(5);

        product2 = new Product();
        product2.setId(2L);
        product2.setName("Shampoo");
        product2.setPrice(20.0);
        product2.setQuantity(3);

        productService = new ProductService();
        productService.repo = productRepo;

        saleService = new SaleService();
        saleService.repo = saleRepo;
        saleService.productService = productService;

        when(productRepo.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepo.findById(2L)).thenReturn(Optional.of(product2));

        when(productRepo.save(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            if (p.getId().equals(1L)) product1.setQuantity(p.getQuantity());
            if (p.getId().equals(2L)) product2.setQuantity(p.getQuantity());
            return p;
        });

        when(saleRepo.save(any(Sale.class))).thenAnswer(invocation -> {
            Sale s = invocation.getArgument(0);
            if (s.getItems() == null) s.setItems(new ArrayList<>());
            return s;
        });
    }

    @Test
    void testCreateSaleReducesStockSingleProduct() {
        SaleItemDTO itemDTO = new SaleItemDTO();
        ProductDTO pDTO = new ProductDTO();
        pDTO.setId(1L);
        itemDTO.setProduct(pDTO);
        itemDTO.setQuantity(3);

        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setItems(new ArrayList<>(List.of(itemDTO)));
        saleDTO.setDate(LocalDate.now());

        SaleDTO result = saleService.createSale(saleDTO);

        assertEquals(1, result.getItems().size());
        assertEquals(2, product1.getQuantity());
    }

    @Test
    void testCreateSaleReducesStockMultipleProducts() {
        SaleItemDTO item1 = new SaleItemDTO();
        ProductDTO p1 = new ProductDTO(); p1.setId(1L); item1.setProduct(p1); item1.setQuantity(2);

        SaleItemDTO item2 = new SaleItemDTO();
        ProductDTO p2 = new ProductDTO(); p2.setId(2L); item2.setProduct(p2); item2.setQuantity(1);

        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setItems(new ArrayList<>(List.of(item1, item2)));
        saleDTO.setDate(LocalDate.now());

        SaleDTO result = saleService.createSale(saleDTO);

        assertEquals(2, result.getItems().size());
        assertEquals(3, product1.getQuantity());
        assertEquals(2, product2.getQuantity());
    }

    @Test
    void testCreateSaleNotEnoughStock() {
        SaleItemDTO itemDTO = new SaleItemDTO();
        ProductDTO pDTO = new ProductDTO(); pDTO.setId(1L); itemDTO.setProduct(pDTO);
        itemDTO.setQuantity(10);

        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setItems(new ArrayList<>(List.of(itemDTO)));
        saleDTO.setDate(LocalDate.now());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> saleService.createSale(saleDTO));
        assertTrue(ex.getMessage().contains("Sem estoque suficiente do produto: "));
    }

    @Test
    void testDeleteSaleReplenishesStock() {
        product1.setQuantity(2);

        SaleItem item = new SaleItem();
        item.setProduct(product1);
        item.setQuantity(3);

        Sale sale = new Sale();
        sale.setId(1L);
        sale.setItems(new ArrayList<>(List.of(item)));

        when(saleRepo.findById(1L)).thenReturn(Optional.of(sale));

        doAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            if (p.getId().equals(1L)) product1.setQuantity(p.getQuantity());
            return p;
        }).when(productRepo).save(any(Product.class));

        saleService.deleteSale(1L);

        assertEquals(5, product1.getQuantity());
        verify(saleRepo, times(1)).deleteById(1L);
    }

    @Test
    void testGetAllSales() {
        Sale s1 = new Sale(); s1.setId(1L); s1.setItems(new ArrayList<>());
        Sale s2 = new Sale(); s2.setId(2L); s2.setItems(new ArrayList<>());

        when(saleRepo.findAll()).thenReturn(List.of(s1, s2));

        List<SaleDTO> sales = saleService.getAllSales();
        assertEquals(2, sales.size());
    }

    @Test
    void testGetSaleByIdFound() {
        Sale sale = new Sale(); sale.setId(1L); sale.setItems(new ArrayList<>());
        when(saleRepo.findById(1L)).thenReturn(Optional.of(sale));

        SaleDTO dto = saleService.getSaleById(1L);
        assertEquals(1L, dto.getId());
    }

    @Test
    void testGetSaleByIdNotFound() {
        when(saleRepo.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> saleService.getSaleById(1L));
        assertEquals("Venda não encontrada", ex.getMessage());
    }
}
