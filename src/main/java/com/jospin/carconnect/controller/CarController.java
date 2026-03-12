package com.jospin.carconnect.controller;

import com.jospin.carconnect.dto.CarDetailsResponse;
import com.jospin.carconnect.dto.CarPatchRequest;
import com.jospin.carconnect.dto.CarRequest;
import com.jospin.carconnect.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarDetailsResponse create(@Valid @RequestBody CarRequest request) {
        return carService.create(request);
    }

    @GetMapping
    public Page<CarDetailsResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return carService.getAll(page, size, sortBy, direction);
    }

    @GetMapping("/{id}")
    public CarDetailsResponse getById(@PathVariable UUID id) {
        return carService.getById(id);
    }

    @GetMapping("/plate/{plateNumber}")
    public CarDetailsResponse getByPlateNumber(@PathVariable String plateNumber) {
        return carService.getByPlateNumber(plateNumber);
    }

    @PutMapping("/{id}")
    public CarDetailsResponse update(@PathVariable UUID id, @Valid @RequestBody CarRequest request) {
        return carService.update(id, request);
    }

    @PatchMapping("/{id}")
    public CarDetailsResponse patch(@PathVariable UUID id, @RequestBody CarPatchRequest request) {
        return carService.patch(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        carService.delete(id);
    }

    @GetMapping("/owner/{ownerId}")
    public Page<CarDetailsResponse> getByOwnerId(
            @PathVariable UUID ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return carService.getByOwnerId(ownerId, page, size, sortBy, direction);
    }
}
