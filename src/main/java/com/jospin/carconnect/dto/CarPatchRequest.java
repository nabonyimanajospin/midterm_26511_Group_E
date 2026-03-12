package com.jospin.carconnect.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class CarPatchRequest {
    private String plateNumber;
    private String brand;
    private String model;
    private Integer year;
    private BigDecimal price;
    private Integer mileage;
    private String status;
    private String description;
    private List<UUID> categoryIds;
}
