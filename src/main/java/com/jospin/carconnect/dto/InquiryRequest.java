package com.jospin.carconnect.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class InquiryRequest {
    @NotBlank
    private String message;
    @NotNull
    private String status;
    @NotNull
    private UUID carId;
    @NotNull
    private UUID inquirerId;
}
