package com.jospin.carconnect.dto;

import com.jospin.carconnect.enums.LocationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class LocationRequest {
    @NotBlank
    private String code;
    
    @NotBlank
    private String name;
    
    @NotNull
    private LocationType type;
    
    private UUID parentId;
}
