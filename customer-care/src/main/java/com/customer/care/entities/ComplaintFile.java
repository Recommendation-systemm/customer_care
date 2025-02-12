package com.customer.care.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "complaint_files")
public class ComplaintFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String fileName;      // Stored file name
    private String fileDescription;
    private String filePath;      // Full path to the saved file
    @Column(unique = true)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "complaint_id")
    @JsonIgnore
    private Complaint complaint;  // Link to the complaint

    // Constructors
    public ComplaintFile() {}

    public ComplaintFile(String fileName, String fileDescription, String filePath, Complaint complaint,UUID uuid) {
        this.fileName = fileName;
        this.fileDescription = fileDescription;
        this.filePath = filePath;
        this.complaint = complaint;
        this.uuid=uuid;
    }



    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileDescription() { return fileDescription; }
    public void setFileDescription(String fileDescription) { this.fileDescription = fileDescription; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public Complaint getComplaint() { return complaint; }
    public void setComplaint(Complaint complaint) { this.complaint = complaint; }
}

