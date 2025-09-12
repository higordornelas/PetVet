package com.petvet.entity;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Consultation {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String notes;
    private LocalDate date;
    private Double price;
    @ManyToOne private Pet pet;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Pet getPet() { return pet; }
    public void setPet(Pet pet) { this.pet = pet; }
}
