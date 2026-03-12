package com.jospin.carconnect.repository;

import com.jospin.carconnect.enums.InquiryStatus;
import com.jospin.carconnect.model.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InquiryRepository extends JpaRepository<Inquiry, UUID> {
    Page<Inquiry> findByCarId(UUID carId, Pageable pageable);
    Page<Inquiry> findByInquirerId(UUID inquirerId, Pageable pageable);
    Page<Inquiry> findByStatus(InquiryStatus status, Pageable pageable);
}
