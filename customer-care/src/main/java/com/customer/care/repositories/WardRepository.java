package com.customer.care.repositories;

import com.customer.care.entities.SubCounty;
import com.customer.care.entities.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward,Long> {
    List<Ward> findAllBySubCounty(SubCounty subCounty);
}
