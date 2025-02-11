package com.customer.care.repositories;

import com.customer.care.entities.Complaint;
import com.customer.care.entities.Status;
import com.customer.care.entities.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByStatus(Status status);
    List<Complaint> findByUser(AppUser user);

    List<Complaint> findByCreatedBy(AppUser user);

    Page<Complaint> findByDescriptionContainingIgnoreCase(String keyword, Pageable pageable);
}


