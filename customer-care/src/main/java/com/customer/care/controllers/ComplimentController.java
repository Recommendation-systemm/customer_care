package com.customer.care.controllers;

import com.customer.care.entities.*;
import com.customer.care.repositories.ComplimentRepository;
import com.customer.care.repositories.UserRepository;
import com.customer.care.services.DepartmentService;
import com.customer.care.services.SubCountyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class ComplimentController {

    @Autowired
    private ComplimentRepository complimentRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubCountyService subCountyService;
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/user/compliment")
    public String showComplimentForm(Model model) {
        List<SubCounty> subCounties = subCountyService.getAllSubCounties();
        List<Department> departments = departmentService.getAllDepartments();
        model.addAttribute("subCounties", subCounties);
        model.addAttribute("compliment", new Compliment());
        model.addAttribute("departments", departments);
        return "complimentsForm";
    }
    @GetMapping("/anonymous/compliment")
    public String anonymousComplement(Model model) {
        model.addAttribute("anonymous",true);
        model.addAttribute("compliment", new Compliment());
        return "complimentsForm";
    }

    @PostMapping("/user/compliment")
    public String submitCompliment(Compliment compliment, Principal principal) {
        String username = principal.getName(); // Get logged-in user
        AppUser user = userRepository.findByEmail(username) // Fetch user entity
                .orElseThrow(() -> new RuntimeException("User not found"));

        compliment.setCreatedBy(user);
        complimentRepository.save(compliment);
        return "redirect:/";
    }

    @GetMapping("/compliments")
    public String viewCompliments(Model model) {

        model.addAttribute("compliments", complimentRepository.findAll());
        return "complimentsList";
    }

}
