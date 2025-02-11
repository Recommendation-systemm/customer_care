package com.customer.care.controllers;

import com.customer.care.dto.RegisterDTO;
import com.customer.care.entities.AppUser;
import com.customer.care.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;


    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        System.out.println("Verifying.......");
        boolean verified = userService.verifyEmailToken(token);

        if (verified) {
            redirectAttributes.addFlashAttribute("success", "Your email has been verified. You can now log in.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid or expired verification link.");
        }

        return "redirect:/login";
    }
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
//        model.addAttribute("user", new AppUser());
        RegisterDTO registerDTO = new RegisterDTO();
        model.addAttribute(registerDTO);
        model.addAttribute("success",false);
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(Model model, @Valid @ModelAttribute RegisterDTO registerDTO, BindingResult result) {

        AppUser user=userService.findByEmail(registerDTO.getEmail());
        if(user!=null){
            System.out.println("Message............................ User found");
            model.addAttribute(registerDTO);
            result.addError(new FieldError("registerDTO","email","Email Address is taken"));
        }

        if(result.hasErrors()){
            return "registration";
        }
        System.out.println("Creating user........");
        try {
            System.out.println(" Trying Creating user........");
            var bCryptEncoder= new BCryptPasswordEncoder();

            AppUser appUser=new AppUser();
            appUser.setPassword(bCryptEncoder.encode(registerDTO.getPassword()));
            appUser.setEmail(registerDTO.getEmail());
            appUser.setFullName(registerDTO.getFullName());
            appUser.setUsername(registerDTO.getEmail());
            appUser.setPhone(registerDTO.getPhone());
            userService.registerUser(appUser);
            model.addAttribute("registerDTO",new RegisterDTO());
            model.addAttribute("success",true);
        }catch (Exception e){
            System.out.println("Exception user........"+e.getMessage());
            result.addError(new FieldError("registerDTO","firstName",e.getMessage()));
        }
        return "registration";
//        return "redirect:/login";
    }

//    @PostMapping("/register")
//    public String registerUser(@ModelAttribute("user") AppUser user, RedirectAttributes redirectAttributes) {
//        userService.registerUser(user);
//        redirectAttributes.addFlashAttribute("success", "Registration successful! You can now log in.");
//        return "redirect:/auth/login";
//    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

//    @PostMapping("/login")
//    public String login(@RequestParam String email,
//                        @RequestParam String password,
//                        RedirectAttributes redirectAttributes) {
//        System.out.println("...........................logging in...");
//        try {
//            AppUser user = userService.authenticate(email, password);
//
//            if (!user.isVerified()) {
//                redirectAttributes.addFlashAttribute("error", "Please verify your email before logging in.");
//                return "redirect:/login";
//            }
//
//            // Proceed with authentication (Spring Security or manual session management)
//            return "redirect:/dashboard"; // Redirect to user dashboard
//
//        } catch (RuntimeException e) {
//            redirectAttributes.addFlashAttribute("error", "Invalid email or password.");
//            return "redirect:/login";
//        }
//
//
//    }




}


