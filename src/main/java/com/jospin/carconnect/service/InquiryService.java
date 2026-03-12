package com.jospin.carconnect.service;

import com.jospin.carconnect.dto.InquiryPatchRequest;
import com.jospin.carconnect.dto.InquiryRequest;
import com.jospin.carconnect.enums.InquiryStatus;
import com.jospin.carconnect.exception.ResourceNotFoundException;
import com.jospin.carconnect.model.Car;
import com.jospin.carconnect.model.Inquiry;
import com.jospin.carconnect.model.User;
import com.jospin.carconnect.repository.CarRepository;
import com.jospin.carconnect.repository.InquiryRepository;
import com.jospin.carconnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    public Inquiry create(InquiryRequest request) {
        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
        User inquirer = userRepository.findById(request.getInquirerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Inquiry inquiry = Inquiry.builder()
                .message(request.getMessage())
                .status(InquiryStatus.valueOf(request.getStatus().toUpperCase()))
                .car(car)
                .inquirer(inquirer)
                .build();

        return inquiryRepository.save(inquiry);
    }

    public Page<Inquiry> getAll(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return inquiryRepository.findAll(pageable);
    }

    public Inquiry getById(UUID id) {
        return inquiryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inquiry not found"));
    }

    public Inquiry update(UUID id, InquiryRequest request) {
        Inquiry inquiry = getById(id);
        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
        User inquirer = userRepository.findById(request.getInquirerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        inquiry.setMessage(request.getMessage());
        inquiry.setStatus(InquiryStatus.valueOf(request.getStatus().toUpperCase()));
        inquiry.setCar(car);
        inquiry.setInquirer(inquirer);
        return inquiryRepository.save(inquiry);
    }

    public Inquiry patch(UUID id, InquiryPatchRequest request) {
        Inquiry inquiry = getById(id);
        if (request.getMessage() != null) inquiry.setMessage(request.getMessage());
        if (request.getStatus() != null) inquiry.setStatus(InquiryStatus.valueOf(request.getStatus().toUpperCase()));
        return inquiryRepository.save(inquiry);
    }

    public void delete(UUID id) {
        inquiryRepository.delete(getById(id));
    }
}
