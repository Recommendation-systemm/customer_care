package com.customer.care.controllers;

import com.customer.care.entities.Complaint;
import com.customer.care.entities.Category;
import com.customer.care.entities.Subcategory;
import com.customer.care.repositories.CategoryRepository;
import com.customer.care.repositories.ComplaintRepository;
import com.customer.care.repositories.SubcategoryRepository;
import com.customer.care.services.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ComplaintController {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @GetMapping
    public String index(){
        return "index";
    }

    @GetMapping("/complaints/new")
    public String showComplaintForm(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("complaint", new Complaint());
        model.addAttribute("categories", categories);
        return "complaintForm";
    }

    @PostMapping("/complaints")
    public String submitComplaint(Complaint complaint) {
        complaintRepository.save(complaint);
       // return "redirect:/complaintsList";
        return "redirect:/complaints";
    }

    @GetMapping("/complaints")
    public String listComplaints(Model model) {
        model.addAttribute("complaints", complaintRepository.findAll());
        return "complaintsList";
    }

    @GetMapping("/subcategories/{categoryId}")
    @ResponseBody
    public List<Subcategory> getSubcategories(@PathVariable Long categoryId) {
        return subcategoryRepository.findByCategoryId(categoryId);
    }
    @Autowired
    private ComplaintService complaintService;

    @PostMapping("/complaints/{complaintId}/notes")
    public String addInternalNote(@PathVariable Long complaintId, @RequestParam String note) {
        complaintService.addInternalNote(complaintId, note);
        return "redirect:/complaints/" + complaintId;
    }

    @GetMapping("/complaints/{complaintId}")
    public String showComplaintDetails(@PathVariable Long complaintId, Model model) {
        Complaint complaint = complaintRepository.findById(complaintId).orElseThrow(() -> new RuntimeException("Complaint not found"));
        model.addAttribute("complaint", complaint);
        return "complaintDetails";
    }
}