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
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }




}


