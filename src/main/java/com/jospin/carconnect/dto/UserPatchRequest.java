package com.jospin.carconnect.dto;

import lombok.Data;

@Data
public class UserPatchRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private String userType;
}
