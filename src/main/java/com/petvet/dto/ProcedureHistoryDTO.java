package com.petvet.dto;

import java.time.LocalDate;

public class ProcedureHistoryDTO {
    private Long id;
    private ProcedureDTO procedure;
    private PetDTO pet;
    private String veterinarian;
    private LocalDate date;
    private Double price;
    private String notes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ProcedureDTO getProcedure() { return procedure; }
    public void setProcedure(ProcedureDTO procedure) { this.procedure = procedure; }

    public PetDTO getPet() { return pet; }
    public void setPet(PetDTO pet) { this.pet = pet; }

    public String getVeterinarian() { return veterinarian; }
    public void setVeterinarian(String veterinarian) { this.veterinarian = veterinarian; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
