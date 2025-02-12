package com.customer.care.repositories;

import com.customer.care.entities.ComplaintFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ComplaintFileRepository extends JpaRepository<ComplaintFile, Long> {
    Optional<ComplaintFile> findByUuid(UUID uuid);
}

