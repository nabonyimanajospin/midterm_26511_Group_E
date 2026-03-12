package com.jospin.carconnect.service;

import com.jospin.carconnect.enums.LocationType;
import com.jospin.carconnect.exception.BadRequestException;
import com.jospin.carconnect.exception.ResourceNotFoundException;
import com.jospin.carconnect.model.Location;
import com.jospin.carconnect.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public Location create(Location location, UUID parentId) {
        if (location.getCode() != null && locationRepository.existsByCode(location.getCode())) {
            throw new BadRequestException("Location code already exists");
        }
        
        if (parentId != null) {
            Location parent = locationRepository.findById(parentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Parent location not found"));
            location.setParent(parent);
        }
        
        return locationRepository.save(location);
    }

    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    public Location getById(UUID id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found"));
    }

    public List<Location> getByType(LocationType type) {
        return locationRepository.findByType(type);
    }

    public List<Location> getProvinces() {
        return locationRepository.findAllProvinces();
    }

    public List<Location> getByParentId(UUID parentId) {
        return locationRepository.findByParentId(parentId);
    }

    public boolean existsByCode(String code) {
        return locationRepository.existsByCode(code);
    }

    public void delete(UUID id) {
        locationRepository.delete(fetchById(id));
    }

    private Location fetchById(UUID id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found"));
    }
}
