package com.petvet.entity;
import jakarta.persistence.*;

@Entity
public class Pet {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String breed;
    private String gender;
    private Integer age;
    @ManyToOne
    private Client owner;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public Client getOwner() { return owner; }
    public void setOwner(Client owner) { this.owner = owner; }
}
