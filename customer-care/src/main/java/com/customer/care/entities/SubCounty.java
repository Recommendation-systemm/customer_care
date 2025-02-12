package com.customer.care.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "sub_counties")
public class SubCounty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String subCountyName;
    @OneToMany(mappedBy = "subCounty",cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    Set<Ward> wards =new HashSet<>();
}
