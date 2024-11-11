package com.customer.care;

import com.customer.care.entities.Role;
import com.customer.care.entities.RoleType;
import com.customer.care.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByType(RoleType.CUSTOMER) == null) {
                Role customerRole = new Role();
                customerRole.setName("ROLE_CUSTOMER");
                customerRole.setType(RoleType.CUSTOMER);
                roleRepository.save(customerRole);
            }
            
            if (roleRepository.findByType(RoleType.EMPLOYEE) == null) {
                Role employeeRole = new Role();
                employeeRole.setName("ROLE_EMPLOYEE");
                employeeRole.setType(RoleType.EMPLOYEE);
                roleRepository.save(employeeRole);
            }

            if (roleRepository.findByType(RoleType.ADMIN) == null) {
                Role adminRole = new Role();
                adminRole.setName("ROLE_ADMIN");
                adminRole.setType(RoleType.ADMIN);
                roleRepository.save(adminRole);
            }
        };
    }
}