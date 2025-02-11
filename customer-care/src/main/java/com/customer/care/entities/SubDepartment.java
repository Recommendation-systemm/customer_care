package com.customer.care.entities;

import jakarta.persistence.*;

@Entity
public class SubDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department parentDepartment;

    // Constructors
    public SubDepartment() {}

    public SubDepartment(String name, Department parentDepartment) {
        this.name = name;
        this.parentDepartment = parentDepartment;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Department getParentDepartment() { return parentDepartment; }
    public void setParentDepartment(Department parentDepartment) {
        this.parentDepartment = parentDepartment;
    }
}

