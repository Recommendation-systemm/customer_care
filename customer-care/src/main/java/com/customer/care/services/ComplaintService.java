package com.customer.care.services;

import com.customer.care.entities.Complaint;
import com.customer.care.entities.Status;
import com.customer.care.entities.User;
import com.customer.care.repositories.ComplaintRepository;
import com.customer.care.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    public Complaint addInternalNote(Long complaintId, String note) {
        Complaint complaint = complaintRepository.findById(complaintId).orElseThrow(() -> new RuntimeException("Complaint not found"));
        complaint.getInternalNotes().add(note);
        return complaintRepository.save(complaint);
    }

    public List<String> getInternalNotes(Long complaintId) {
        Complaint complaint = complaintRepository.findById(complaintId).orElseThrow(() -> new RuntimeException("Complaint not found"));
        return complaint.getInternalNotes();
    }
    @Autowired
    private UserRepository userRepository;

    public Complaint assignComplaint(Long complaintId, Long userId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        complaint.setAssignedTo(user);
        return complaintRepository.save(complaint);
    }
    public Complaint resolveComplaint(Long complaintId, String resolution) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        complaint.setStatus(Status.RESOLVED);
        complaint.setResolution(resolution);
        complaint.setResolvedAt(new Date());

        return complaintRepository.save(complaint);
    }


    public List<Complaint> getComplaintsByUser(User user) {
        return complaintRepository.findByUser(user);
    }


}