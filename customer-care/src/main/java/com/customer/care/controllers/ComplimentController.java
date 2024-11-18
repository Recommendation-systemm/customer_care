package com.customer.care.controllers;

import com.customer.care.entities.Compliment;
import com.customer.care.repositories.ComplimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ComplimentController {

    @Autowired
    private ComplimentRepository complimentRepository;

    @GetMapping("/compliment")
    public String showComplimentForm(Model model) {
        model.addAttribute("compliment", new Compliment());
        return "complimentsForm";
    }

    @PostMapping("/compliment")
    public String submitCompliment(Compliment compliment) {
        System.out.println("Received compliment: " + compliment);
        complimentRepository.save(compliment);
        return "redirect:/compliments";
    }

    @GetMapping("/compliments")
    public String viewCompliments(Model model) {
        model.addAttribute("compliments", complimentRepository.findAll());
        return "complimentsList";
    }

}
