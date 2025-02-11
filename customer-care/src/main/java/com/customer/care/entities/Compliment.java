package com.customer.care.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "compliments")
public class Compliment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subCounty;
    private String ward;
    private String department;
    private String complimentType;
    private String satisfaction;
    private String satisfactionExplanation;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private AppUser createdBy;
    private LocalDateTime createdAt = LocalDateTime.now();
}
