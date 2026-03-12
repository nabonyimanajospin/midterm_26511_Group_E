package com.jospin.carconnect.repository;

import com.jospin.carconnect.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    Optional<User> findByEmailIgnoreCase(String email);

    @Query("""
            SELECT DISTINCT u
            FROM User u
            JOIN u.location village
            WHERE village.parent.parent.parent.parent.code = :provinceCode
               OR LOWER(village.parent.parent.parent.parent.name) = LOWER(:provinceName)
            """)
    Page<User> findAllByProvinceCodeOrName(
            @Param("provinceCode") String provinceCode,
            @Param("provinceName") String provinceName,
            Pageable pageable);
}
