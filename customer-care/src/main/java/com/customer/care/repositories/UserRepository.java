package com.customer.care.repositories;

import com.customer.care.entities.Role;
import com.customer.care.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}



