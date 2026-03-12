package com.jospin.carconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class CarDetailsResponse {
    private UUID id;
    private String plateNumber;
    private String brand;
    private String model;
    private Integer year;
    private BigDecimal price;
    private Integer mileage;
    private String status;
    private String description;

    private UUID ownerId;
    private String ownerFirstName;
    private String ownerLastName;
    private String ownerEmail;
    private String ownerPhone;

    private List<String> categories;
    private LocationDetailsResponse location;
}
