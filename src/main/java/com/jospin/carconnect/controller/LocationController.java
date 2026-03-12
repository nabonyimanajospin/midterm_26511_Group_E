package com.jospin.carconnect.controller;

import com.jospin.carconnect.dto.LocationRequest;
import com.jospin.carconnect.enums.LocationType;
import com.jospin.carconnect.model.Location;
import com.jospin.carconnect.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Location create(@Valid @RequestBody LocationRequest request) {
        Location location = Location.builder()
                .code(request.getCode())
                .name(request.getName())
                .type(request.getType())
                .build();
        return locationService.create(location, request.getParentId());
    }

    @GetMapping
    public List<Location> getAll() {
        return locationService.getAll();
    }

    @GetMapping("/{id}")
    public Location getById(@PathVariable UUID id) {
        return locationService.getById(id);
    }

    @GetMapping("/type/{type}")
    public List<Location> getByType(@PathVariable LocationType type) {
        return locationService.getByType(type);
    }

    @GetMapping("/provinces")
    public List<Location> getProvinces() {
        return locationService.getProvinces();
    }

    @GetMapping("/parent/{parentId}")
    public List<Location> getByParentId(@PathVariable UUID parentId) {
        return locationService.getByParentId(parentId);
    }

    @GetMapping("/exists/{code}")
    public boolean existsByCode(@PathVariable String code) {
        return locationService.existsByCode(code);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        locationService.delete(id);
    }
}
