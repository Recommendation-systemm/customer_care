package com.customer.care.controllers;

import com.customer.care.entities.Complaint;
import com.customer.care.entities.Status;
import com.customer.care.repositories.ComplaintRepository;
import com.customer.care.repositories.ComplimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private ComplimentRepository complimentRepository;

    @GetMapping
    public String admin() {
        return "admin";
    }

    @GetMapping("/compliments")
    public String viewCompliments(Model model) {
        model.addAttribute("compliments", complimentRepository.findAllByOrderByCreatedAtDesc());
        return "complimentsList";
    }

    @GetMapping("/complaints")
    public String listComplaints(Model model) {
        model.addAttribute("complaints", complaintRepository.findAllByOrderByCreatedAtDesc());
        return "complaintsList";
    }
//    @GetMapping("/admin/complaints")
//        public String adminDashboard(Model model) {
//        model.addAttribute("newComplaints", complaintRepository.findByStatus(Status.NEW));
//        model.addAttribute("inProgressComplaints", complaintRepository.findByStatus(Status.IN_PROGRESS));
//        model.addAttribute("resolvedComplaints", complaintRepository.findByStatus(Status.RESOLVED));
//        model.addAttribute("closedComplaints", complaintRepository.findByStatus(Status.CLOSED));
//        return "adminComplaints";
//    }

//    @GetMapping("/complaints")
//    public String adminComplaintsDashboard(Model model) {
//        List<Complaint> newComplaints = complaintRepository.findByStatus(Status.NEW);
//        List<Complaint> inProgressComplaints = complaintRepository.findByStatus(Status.IN_PROGRESS);
//        List<Complaint> resolvedComplaints = complaintRepository.findByStatus(Status.RESOLVED);
//
//        for (Complaint complaint : newComplaints) {
//            complaint.setStatus(Status.IN_PROGRESS);
//            complaintRepository.save(complaint);
//        }
//
//        model.addAttribute("inProgressComplaints", newComplaints);
//        model.addAttribute("inProgressComplaints", inProgressComplaints);
//        model.addAttribute("resolvedComplaints", resolvedComplaints);
//
//        return "adminComplaints";
//    }

}