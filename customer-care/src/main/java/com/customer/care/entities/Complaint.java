package com.customer.care.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "complaints")
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subCounty;
    private String ward;
    private String title;
    private String description;
    private String category;
    private String subCategory;

    @ManyToOne // Assuming a complaint belongs to one user
    @JoinColumn(name = "created_by", referencedColumnName = "id") // Store user ID
    private AppUser createdBy;
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    private String priority;

    @ManyToOne
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    private AppUser assignedTo;


    private String resolution;

    @Temporal(TemporalType.TIMESTAMP)
    private Date submittedAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date resolvedAt;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name="complaint_notes", joinColumns=@JoinColumn(name="complaint_id"))
    @Column(name="note")
    private List<String> internalNotes;
}