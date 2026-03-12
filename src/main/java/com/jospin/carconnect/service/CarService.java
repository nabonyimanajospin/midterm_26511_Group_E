package com.jospin.carconnect.service;

import com.jospin.carconnect.dto.CarDetailsResponse;
import com.jospin.carconnect.dto.CarPatchRequest;
import com.jospin.carconnect.dto.CarRequest;
import com.jospin.carconnect.dto.LocationDetailsResponse;
import com.jospin.carconnect.enums.CarStatus;
import com.jospin.carconnect.exception.BadRequestException;
import com.jospin.carconnect.exception.ResourceNotFoundException;
import com.jospin.carconnect.model.*;
import com.jospin.carconnect.repository.CarRepository;
import com.jospin.carconnect.repository.CategoryRepository;
import com.jospin.carconnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public CarDetailsResponse create(CarRequest request) {
        if (carRepository.existsByPlateNumberIgnoreCase(request.getPlateNumber())) {
            throw new BadRequestException("Plate number already exists");
        }

        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        Car car = Car.builder()
                .plateNumber(request.getPlateNumber())
                .brand(request.getBrand())
                .model(request.getModel())
                .year(request.getYear())
                .price(request.getPrice())
                .mileage(request.getMileage())
                .status(CarStatus.valueOf(request.getStatus().toUpperCase()))
                .description(request.getDescription())
                .owner(owner)
                .build();

        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
            car.setCategories(categories);
        }

        return toDetails(carRepository.save(car));
    }

    public Page<CarDetailsResponse> getAll(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return carRepository.findAll(pageable).map(this::toDetails);
    }

    public CarDetailsResponse getById(UUID id) {
        return toDetails(fetchById(id));
    }

    public CarDetailsResponse getByPlateNumber(String plateNumber) {
        Car car = carRepository.findByPlateNumberIgnoreCase(plateNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found for plate number"));
        return toDetails(car);
    }

    public CarDetailsResponse update(UUID id, CarRequest request) {
        Car car = fetchById(id);
        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        if (!car.getPlateNumber().equalsIgnoreCase(request.getPlateNumber())
                && carRepository.existsByPlateNumberIgnoreCase(request.getPlateNumber())) {
            throw new BadRequestException("Plate number already exists");
        }

        car.setPlateNumber(request.getPlateNumber());
        car.setBrand(request.getBrand());
        car.setModel(request.getModel());
        car.setYear(request.getYear());
        car.setPrice(request.getPrice());
        car.setMileage(request.getMileage());
        car.setStatus(CarStatus.valueOf(request.getStatus().toUpperCase()));
        car.setDescription(request.getDescription());
        car.setOwner(owner);
        car.setCategories(new ArrayList<>());

        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
            car.setCategories(categories);
        }

        return toDetails(carRepository.save(car));
    }

    public CarDetailsResponse patch(UUID id, CarPatchRequest request) {
        Car car = fetchById(id);

        if (request.getPlateNumber() != null) {
            if (!car.getPlateNumber().equalsIgnoreCase(request.getPlateNumber())
                    && carRepository.existsByPlateNumberIgnoreCase(request.getPlateNumber())) {
                throw new BadRequestException("Plate number already exists");
            }
            car.setPlateNumber(request.getPlateNumber());
        }

        if (request.getBrand() != null) car.setBrand(request.getBrand());
        if (request.getModel() != null) car.setModel(request.getModel());
        if (request.getYear() != null) car.setYear(request.getYear());
        if (request.getPrice() != null) car.setPrice(request.getPrice());
        if (request.getMileage() != null) car.setMileage(request.getMileage());
        if (request.getStatus() != null) car.setStatus(CarStatus.valueOf(request.getStatus().toUpperCase()));
        if (request.getDescription() != null) car.setDescription(request.getDescription());
        if (request.getCategoryIds() != null) car.setCategories(categoryRepository.findAllById(request.getCategoryIds()));

        return toDetails(carRepository.save(car));
    }

    public void delete(UUID id) {
        carRepository.delete(fetchById(id));
    }

    public Page<CarDetailsResponse> getByOwnerId(UUID ownerId, int page, int size, String sortBy, String direction) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return carRepository.findByOwnerId(ownerId, pageable).map(this::toDetails);
    }

    private Car fetchById(UUID id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
    }

    private CarDetailsResponse toDetails(Car car) {
        User owner = car.getOwner();
        LocationDetailsResponse locationDetails = buildLocation(owner);

        return CarDetailsResponse.builder()
                .id(car.getId())
                .plateNumber(car.getPlateNumber())
                .brand(car.getBrand())
                .model(car.getModel())
                .year(car.getYear())
                .price(car.getPrice())
                .mileage(car.getMileage())
                .status(car.getStatus().name())
                .description(car.getDescription())
                .ownerId(owner.getId())
                .ownerFirstName(owner.getFirstName())
                .ownerLastName(owner.getLastName())
                .ownerEmail(owner.getEmail())
                .ownerPhone(owner.getPhone())
                .categories(car.getCategories().stream().map(Category::getName).toList())
                .location(locationDetails)
                .build();
    }

    private LocationDetailsResponse buildLocation(User owner) {
        if (owner.getLocation() == null) {
            return null;
        }

        Location village = owner.getLocation();
        Location cell = village.getParent();
        Location sector = cell != null ? cell.getParent() : null;
        Location district = sector != null ? sector.getParent() : null;
        Location province = district != null ? district.getParent() : null;

        return LocationDetailsResponse.builder()
                .userId(owner.getId())
                .villageId(village.getId())
                .villageCode(village.getCode())
                .villageName(village.getName())
                .cellId(cell != null ? cell.getId() : null)
                .cellCode(cell != null ? cell.getCode() : null)
                .cellName(cell != null ? cell.getName() : null)
                .sectorId(sector != null ? sector.getId() : null)
                .sectorCode(sector != null ? sector.getCode() : null)
                .sectorName(sector != null ? sector.getName() : null)
                .districtId(district != null ? district.getId() : null)
                .districtCode(district != null ? district.getCode() : null)
                .districtName(district != null ? district.getName() : null)
                .provinceId(province != null ? province.getId() : null)
                .provinceCode(province != null ? province.getCode() : null)
                .provinceName(province != null ? province.getName() : null)
                .build();
    }
}
