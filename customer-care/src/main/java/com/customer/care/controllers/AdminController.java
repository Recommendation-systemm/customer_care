package com.customer.care.controllers;

import com.customer.care.entities.Complaint;
import com.customer.care.entities.Status;
import com.customer.care.repositories.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @Autowired
    private ComplaintRepository complaintRepository;

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
    @GetMapping("/admin/complaints")
    public String adminDashboard(Model model) {
        model.addAttribute("newComplaints", complaintRepository.findByStatus(Status.NEW));
        model.addAttribute("inProgressComplaints", complaintRepository.findByStatus(Status.IN_PROGRESS));
        model.addAttribute("resolvedComplaints", complaintRepository.findByStatus(Status.RESOLVED));
        model.addAttribute("closedComplaints", complaintRepository.findByStatus(Status.CLOSED));
        return "adminComplaints";
    }
}