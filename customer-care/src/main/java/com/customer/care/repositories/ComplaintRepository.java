package com.customer.care.repositories;

import com.customer.care.entities.Complaint;
import com.customer.care.entities.Status;
import com.customer.care.entities.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByStatus(Status status);
    List<Complaint> findByUser(AppUser user);

    List<Complaint> findByCreatedBy(AppUser user);

    Page<Complaint> findByDescriptionContainingIgnoreCase(String keyword, Pageable pageable);

    List<Complaint> findByCreatedAtBetween(Date fromDate, Date toDate);

    Optional<Complaint> findByUuid(UUID uuid);

    List<Complaint> findAllByOrderByCreatedAtDesc();
}


