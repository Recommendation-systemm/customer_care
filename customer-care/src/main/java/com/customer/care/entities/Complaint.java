package com.customer.care.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "complaints")
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subCounty;
//    private String ward;
    private String title;
    private String description;
    private String category;
    private String subCategory;
    @Column(unique = true)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "ward_id")
    @ToString.Exclude
    private  Ward ward;

    @OneToMany(mappedBy = "complaint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComplaintFile> files = new ArrayList<>();

    public void addFile(ComplaintFile file) {
        files.add(file);
        file.setComplaint(this);
    }


    @ManyToOne // Assuming a complaint belongs to one user
    @JoinColumn(name = "created_by", referencedColumnName = "id") // Store user ID
    @ToString.Exclude
    private AppUser createdBy;

    private Date createdAt = new Date();

    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    private String priority;

    @ManyToOne
    @ToString.Exclude
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    @ToString.Exclude
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