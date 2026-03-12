package com.jospin.carconnect.repository;

import com.jospin.carconnect.enums.LocationType;
import com.jospin.carconnect.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
    Optional<Location> findByCode(String code);
    boolean existsByCode(String code);
    List<Location> findByType(LocationType type);
    List<Location> findByParentId(UUID parentId);
    
    @Query("SELECT l FROM Location l WHERE l.type = 'PROVINCE'")
    List<Location> findAllProvinces();
}
