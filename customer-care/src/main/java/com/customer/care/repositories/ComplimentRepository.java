package com.customer.care.repositories;

import com.customer.care.entities.AppUser;
import com.customer.care.entities.Complaint;
import com.customer.care.entities.Compliment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplimentRepository extends JpaRepository<Compliment, Long> {
    List<Compliment> findByCreatedBy(AppUser user);

    List<Compliment> findAllByOrderByCreatedAtDesc();
}
