package com.jospin.carconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class LocationDetailsResponse {
    private UUID userId;
    private UUID villageId;
    private String villageCode;
    private String villageName;
    private UUID cellId;
    private String cellCode;
    private String cellName;
    private UUID sectorId;
    private String sectorCode;
    private String sectorName;
    private UUID districtId;
    private String districtCode;
    private String districtName;
    private UUID provinceId;
    private String provinceCode;
    private String provinceName;
}
