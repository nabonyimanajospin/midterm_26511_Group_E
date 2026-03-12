package com.jospin.carconnect.repository;

import com.jospin.carconnect.enums.CarStatus;
import com.jospin.carconnect.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {
    Page<Car> findByStatus(CarStatus status, Pageable pageable);
    Page<Car> findByOwnerId(UUID ownerId, Pageable pageable);
    boolean existsByPlateNumberIgnoreCase(String plateNumber);
    Optional<Car> findByPlateNumberIgnoreCase(String plateNumber);
}
