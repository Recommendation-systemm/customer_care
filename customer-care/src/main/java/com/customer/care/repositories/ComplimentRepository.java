package com.customer.care.repositories;

import com.customer.care.entities.Compliment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplimentRepository extends JpaRepository<Compliment, Long> {
}
