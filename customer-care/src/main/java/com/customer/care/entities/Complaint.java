package com.customer.care.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "complaints")
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String category;
    private String subCategory;

    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    private String priority;

    @ManyToOne
    private User user;  // Complaint submitter

    @ManyToOne
    private User assignedTo;  // User to whom the complaint is assigned

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