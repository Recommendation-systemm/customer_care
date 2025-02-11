package com.customer.care.controllers;

import com.customer.care.dto.RegisterDTO;
import com.customer.care.entities.AppUser;
import com.customer.care.entities.Complaint;
import com.customer.care.entities.Compliment;
import com.customer.care.repositories.ComplaintRepository;
import com.customer.care.repositories.ComplimentRepository;
import com.customer.care.repositories.UserRepository;
import com.customer.care.services.ComplaintService;
import com.customer.care.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private ComplimentRepository complimentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ComplaintService complaintService;


    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String token) {
        Optional<AppUser> optionalUser = userRepository.findByVerificationToken(token);
        if (optionalUser.isPresent()) {
            AppUser user = optionalUser.get();
            user.setVerified(true);
            user.setVerificationToken(null); // Clear token after verification
            userRepository.save(user);
            return "Email verified successfully!";
        } else {
            return "Invalid verification token.";
        }
    }


//    @GetMapping("/register")
//    public String showRegisterForm(Model model ) {
//        RegisterDTO registerDTO = new RegisterDTO();
//        model.addAttribute(registerDTO);
//        model.addAttribute("success",false);
//        return "registration";
//    }
    @GetMapping("/dashboard")
    public String dashboard() {
//        RegisterDTO registerDTO = new RegisterDTO();
//        model.addAttribute(registerDTO);
//        model.addAttribute("success",false);
        return "index";
    }


//    @PostMapping("/login")
//    public String loginUser(@RequestParam String email, @RequestParam String password) {
//        AppUser user = userService.loginUser(email, password);
//        if (user != null) {
//            return "redirect:/compliment";
//        } else {
//            return "redirect:/login?error=true";
//        }
//    }

//    @GetMapping("/login")
//    public String showLoginForm() {
//        return "login";
//    }

    @GetMapping("/complaint/paged")
    public ResponseEntity<Map<String, Object>> getComplaintsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortColumn,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(defaultValue = "") String search) {

        System.out.println(sortColumn);
        System.out.println(page);
        System.out.println(size);
        System.out.println(sortDirection);
        System.out.println(search);
        // List of allowed columns to prevent SQL injection
        List<String> allowedSortColumns = Arrays.asList("id", "description", "createdAt");

        // Validate sort column, default to 'id' if not allowed
        if (!allowedSortColumns.contains(sortColumn)) {
            sortColumn = "id";
        }

        Sort sort = Sort.by(
                sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
                sortColumn
        );

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Complaint> complaintsPage = search.isEmpty()
                ? complaintService.getAllPaged(pageable)
                : complaintService.searchComplaints(search, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("data", complaintsPage.getContent());
        response.put("recordsTotal", complaintsPage.getTotalElements());
        response.put("recordsFiltered", complaintsPage.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/complaints")
    public String submitComplaint(Complaint complaint,Principal principal) {
        String username = principal.getName(); // Get logged-in user
        AppUser user = userRepository.findByEmail(username) // Fetch user entity
                .orElseThrow(() -> new RuntimeException("User not found"));

        complaint.setCreatedBy(user);
        complaintRepository.save(complaint);
        // return "redirect:/complaintsList";
        return "redirect:/";
    }
    @GetMapping("/complaints")
    public String getUserComplaints(Model model, Principal principal) {
        String username = principal.getName();
        AppUser user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Complaint> complaints = complaintRepository.findByCreatedBy(user);
        model.addAttribute("complaints", complaints); // Pass complaints to Thymeleaf

        return "user-complaints"; // Return view for displaying complaints
    }




    @GetMapping("/compliments")
    public String getUserComplements(Model model, Principal principal) {
        String username = principal.getName();
        AppUser user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Compliment> compliments = complimentRepository.findByCreatedBy(user);
        model.addAttribute("compliments", compliments); // Pass compliments to Thymeleaf

        return "user-compliments"; // Return view for displaying complaints
    }


}
