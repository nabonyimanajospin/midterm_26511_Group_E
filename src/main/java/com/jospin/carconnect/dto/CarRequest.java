package com.jospin.carconnect.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class CarRequest {
    @NotBlank
    private String plateNumber;
    @NotBlank
    private String brand;
    @NotBlank
    private String model;
    @NotNull
    private Integer year;
    @NotNull
    private BigDecimal price;
    @NotNull
    private Integer mileage;
    @NotNull
    private String status;
    private String description;
    @NotNull
    private UUID ownerId;
    private List<UUID> categoryIds;
}
