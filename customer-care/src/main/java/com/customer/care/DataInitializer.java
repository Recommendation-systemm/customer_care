package com.customer.care;

import com.customer.care.entities.*;
import com.customer.care.repositories.CategoryRepository;
import com.customer.care.repositories.RoleRepository;
import com.customer.care.repositories.SubcategoryRepository;
import com.customer.care.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        if (roleRepository.findByName(RoleName.USER).isEmpty()) {
            roleRepository.save(new Role(RoleName.USER));
        }
        if (roleRepository.findByName(RoleName.ADMIN).isEmpty()) {
            roleRepository.save(new Role(RoleName.ADMIN));
        }


        if (userRepository.findByEmail("admin@example.com").isEmpty()) {

            AppUser appUser=new AppUser();//"Admin", "admin@example.com", passwordEncoder.encode("admin123"),"","",""
            appUser.setUsername("admin");
            appUser.setFullName("admin");
            appUser.setPassword(passwordEncoder.encode("admin@1234"));
            appUser.setEmail("admin@example.com");
            appUser.setVerified(true);
            Role adminRole = roleRepository.findByName(RoleName.ADMIN).get();
            appUser.addRole(adminRole);
            userRepository.save(appUser);
        }
    }

//    @Bean
//    CommandLineRunner initDatabase(CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository) {
//        return args -> {
//            Category productIssues = new Category();
//            productIssues.setName("Product Issues");
//            categoryRepository.save(productIssues);
//
//            Subcategory warrantyIssues = new Subcategory();
//            warrantyIssues.setName("Warranty Issues");
//            warrantyIssues.setCategory(productIssues);
//            subcategoryRepository.save(warrantyIssues);
//
//            Subcategory defectIssues = new Subcategory();
//            defectIssues.setName("Defective Product");
//            defectIssues.setCategory(productIssues);
//            subcategoryRepository.save(defectIssues);
//
//            Category billing = new Category();
//            billing.setName("Billing");
//            categoryRepository.save(billing);
//
//            Subcategory incorrectCharge = new Subcategory();
//            incorrectCharge.setName("Incorrect Charge");
//            incorrectCharge.setCategory(billing);
//            subcategoryRepository.save(incorrectCharge);
//
//            Subcategory refundRequests = new Subcategory();
//            refundRequests.setName("Refund Requests");
//            refundRequests.setCategory(billing);
//            subcategoryRepository.save(refundRequests);
//        };
//    }
}