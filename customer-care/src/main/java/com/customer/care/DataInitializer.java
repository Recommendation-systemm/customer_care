package com.customer.care;

import com.customer.care.entities.Category;
import com.customer.care.entities.Subcategory;
import com.customer.care.repositories.CategoryRepository;
import com.customer.care.repositories.SubcategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository) {
        return args -> {
            Category productIssues = new Category();
            productIssues.setName("Product Issues");
            categoryRepository.save(productIssues);

            Subcategory warrantyIssues = new Subcategory();
            warrantyIssues.setName("Warranty Issues");
            warrantyIssues.setCategory(productIssues);
            subcategoryRepository.save(warrantyIssues);

            Subcategory defectIssues = new Subcategory();
            defectIssues.setName("Defective Product");
            defectIssues.setCategory(productIssues);
            subcategoryRepository.save(defectIssues);

            Category billing = new Category();
            billing.setName("Billing");
            categoryRepository.save(billing);

            Subcategory incorrectCharge = new Subcategory();
            incorrectCharge.setName("Incorrect Charge");
            incorrectCharge.setCategory(billing);
            subcategoryRepository.save(incorrectCharge);

            Subcategory refundRequests = new Subcategory();
            refundRequests.setName("Refund Requests");
            refundRequests.setCategory(billing);
            subcategoryRepository.save(refundRequests);
        };
    }
}