package com.customer.care.controllers;

import com.customer.care.entities.Complaint;
import com.customer.care.entities.Status;
import com.customer.care.repositories.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private ComplaintRepository complaintRepository;

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
    //@GetMapping("/admin/complaints")
    //    public String adminDashboard(Model model) {
    //    model.addAttribute("newComplaints", complaintRepository.findByStatus(Status.NEW));
    //    model.addAttribute("inProgressComplaints", complaintRepository.findByStatus(Status.IN_PROGRESS));
    //    model.addAttribute("resolvedComplaints", complaintRepository.findByStatus(Status.RESOLVED));
    //    model.addAttribute("closedComplaints", complaintRepository.findByStatus(Status.CLOSED));
    //    return "adminComplaints";
    //}

    @GetMapping("/admin/complaints")
    public String adminComplaintsDashboard(Model model) {
        List<Complaint> newComplaints = complaintRepository.findByStatus(Status.NEW);
        List<Complaint> inProgressComplaints = complaintRepository.findByStatus(Status.IN_PROGRESS);
        List<Complaint> resolvedComplaints = complaintRepository.findByStatus(Status.RESOLVED);

        for (Complaint complaint : newComplaints) {
            complaint.setStatus(Status.IN_PROGRESS);
            complaintRepository.save(complaint);
        }

        model.addAttribute("inProgressComplaints", newComplaints);
        model.addAttribute("inProgressComplaints", inProgressComplaints);
        model.addAttribute("resolvedComplaints", resolvedComplaints);

        return "adminComplaints";
    }

}