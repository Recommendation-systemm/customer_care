package com.customer.care.controllers;

import com.customer.care.entities.*;
import com.customer.care.repositories.CategoryRepository;
import com.customer.care.repositories.ComplaintRepository;
import com.customer.care.repositories.UserRepository;
import com.customer.care.repositories.SubcategoryRepository;
import com.customer.care.services.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class ComplaintController {

    @Autowired
    private ComplaintRepository complaintRepository;

    public List<Complaint> getComplaintsByUser(User user) {
        return complaintRepository.findByUser(user);
    }

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String index()
    {
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
        return "redirect:/";
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

    @PostMapping("/complaints/{id}/assign")
    public String assignComplaintToUser(@PathVariable Long id,
                                        @RequestParam Long userId,
                                        RedirectAttributes redirectAttributes) {
        Optional<Complaint> optionalComplaint = complaintRepository.findById(id);
        if (optionalComplaint.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Complaint not found.");
            return "redirect:/complaints";
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/complaints/" + id;
        }

        Complaint complaint = optionalComplaint.get();
        User user = optionalUser.get();
        complaint.setAssignedTo(user);

        complaintRepository.save(complaint);

        redirectAttributes.addFlashAttribute("success", "Complaint successfully assigned to " + user.getUsername());
        return "redirect:/complaints/" + id;
    }

    @PostMapping("/complaints/{id}/resolve")
    public String resolveComplaint(
            @PathVariable Long id,
            @RequestParam("resolution") String resolution,
            RedirectAttributes redirectAttributes) {
        Optional<Complaint> optionalComplaint = complaintRepository.findById(id);

        if (optionalComplaint.isPresent()) {
            Complaint complaint = optionalComplaint.get();
            complaint.setResolution(resolution);
            complaint.setStatus(Status.RESOLVED);
            complaintRepository.save(complaint);

            redirectAttributes.addFlashAttribute("successMessage", "Complaint has been resolved successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Complaint not found.");
        }

        return "redirect:/complaints";
    }


}