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

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model ) {
        RegisterDTO registerDTO = new RegisterDTO();
        model.addAttribute(registerDTO);
        model.addAttribute("success",false);
        return "registration";
    }
    @PostMapping("/register")
    public String registerUser(Model model, @Valid  @ModelAttribute RegisterDTO registerDTO, BindingResult result) {

        AppUser user=userService.findByEmail(registerDTO.getEmail());
        if(user!=null){
            System.out.println("Message............................ User found");
            model.addAttribute(registerDTO);
            result.addError(new FieldError("registerDTO","email","Email Address is taken"));
        }

        if(result.hasErrors()){
            return "registration";
        }

        try {
            var bCryptEncoder= new BCryptPasswordEncoder();

            AppUser appUser=new AppUser();
            appUser.setPassword(bCryptEncoder.encode(registerDTO.getPassword()));
            appUser.setEmail(registerDTO.getEmail());
            appUser.setFullName(registerDTO.getFullName());
            appUser.setUsername(registerDTO.getEmail());
            appUser.setPhone(registerDTO.getPhone());
            appUser.setPrivilege("client");
            userService.registerUser(appUser);
            model.addAttribute("registerDTO",new RegisterDTO());
            model.addAttribute("success",true);
        }catch (Exception e){
            result.addError(new FieldError("registerDTO","firstName",e.getMessage()));
        }
        return "registration";
//        return "redirect:/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password) {
        AppUser user = userService.loginUser(email, password);
        if (user != null) {
            return "redirect:/compliment";
        } else {
            return "redirect:/login?error=true";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }


}
