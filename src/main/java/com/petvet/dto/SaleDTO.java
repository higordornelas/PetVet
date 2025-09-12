package com.petvet.dto;

import java.time.LocalDate;
import java.util.List;

public class SaleDTO {
    private Long id;
    private LocalDate date;
    private List<SaleItemDTO> items;
    private Double total;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public List<SaleItemDTO> getItems() { return items; }
    public void setItems(List<SaleItemDTO> items) { this.items = items; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
}
