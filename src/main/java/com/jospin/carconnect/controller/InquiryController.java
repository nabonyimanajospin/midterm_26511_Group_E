package com.jospin.carconnect.controller;

import com.jospin.carconnect.dto.InquiryPatchRequest;
import com.jospin.carconnect.dto.InquiryRequest;
import com.jospin.carconnect.model.Inquiry;
import com.jospin.carconnect.service.InquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Inquiry create(@Valid @RequestBody InquiryRequest request) {
        return inquiryService.create(request);
    }

    @GetMapping
    public Page<Inquiry> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return inquiryService.getAll(page, size, sortBy, direction);
    }

    @GetMapping("/{id}")
    public Inquiry getById(@PathVariable UUID id) {
        return inquiryService.getById(id);
    }

    @PutMapping("/{id}")
    public Inquiry update(@PathVariable UUID id, @Valid @RequestBody InquiryRequest request) {
        return inquiryService.update(id, request);
    }

    @PatchMapping("/{id}")
    public Inquiry patch(@PathVariable UUID id, @RequestBody InquiryPatchRequest request) {
        return inquiryService.patch(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        inquiryService.delete(id);
    }
}
