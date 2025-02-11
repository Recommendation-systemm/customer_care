package com.customer.care.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "wards")
public class Ward {
    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String wardName;
    @ManyToOne
    @JoinColumn(name = "subcounty_id", nullable = false)
    SubCounty subCounty;
}
