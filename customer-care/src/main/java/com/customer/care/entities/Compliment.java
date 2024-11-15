package com.customer.care.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "compliments")
public class Compliment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String subCounty;
    private String ward;
    private String title;
    private String description;
}
