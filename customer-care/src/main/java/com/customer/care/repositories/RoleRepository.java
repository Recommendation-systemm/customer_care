package com.customer.care.repositories;

import com.customer.care.entities.Role;
import com.customer.care.entities.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
    Role findByType(RoleType type);
}
