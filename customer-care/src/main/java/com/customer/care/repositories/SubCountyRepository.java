package com.customer.care.repositories;

import com.customer.care.entities.SubCounty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCountyRepository extends JpaRepository<SubCounty,Long> {
}
