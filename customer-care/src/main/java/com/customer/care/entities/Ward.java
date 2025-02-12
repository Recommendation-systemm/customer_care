package com.customer.care.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "ward")
    @JsonIgnore
    private  List<Compliment> compliments=new ArrayList<>();

    @OneToMany(mappedBy = "ward",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<Complaint> complaints = new ArrayList<>();
}
