package com.jospin.carconnect.controller;

import com.jospin.carconnect.dto.UserPatchRequest;
import com.jospin.carconnect.dto.UserRequest;
import com.jospin.carconnect.dto.UserDetailsResponse;
import com.jospin.carconnect.model.Location;
import com.jospin.carconnect.model.User;
import com.jospin.carconnect.service.LocationService;
import com.jospin.carconnect.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LocationService locationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDetailsResponse create(@Valid @RequestBody UserRequest request) {
        return userService.toDetails(userService.create(request));
    }

    @GetMapping
    public Page<UserDetailsResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return userService.getAll(page, size, sortBy, direction).map(userService::toDetails);
    }

    @GetMapping("/{id}")
    public UserDetailsResponse getById(@PathVariable UUID id) {
        return userService.toDetails(userService.getById(id));
    }

    @PutMapping("/{id}")
    public UserDetailsResponse update(@PathVariable UUID id, @Valid @RequestBody UserRequest request) {
        return userService.toDetails(userService.update(id, request));
    }

    @PatchMapping("/{id}")
    public UserDetailsResponse patch(@PathVariable UUID id, @RequestBody UserPatchRequest request) {
        return userService.toDetails(userService.patch(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        userService.delete(id);
    }

    @GetMapping("/exists/email/{email}")
    public Map<String, Boolean> existsByEmail(@PathVariable String email) {
        return Map.of("exists", userService.existsByEmail(email));
    }

    @GetMapping("/by-province")
    public Page<UserDetailsResponse> getByProvince(
            @RequestParam String province,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return userService.findByProvince(province, page, size, sortBy, direction).map(userService::toDetails);
    }
    
    @PostMapping("/{userId}/location/{locationId}")
    public UserDetailsResponse assignLocation(@PathVariable UUID userId, @PathVariable UUID locationId) {
        User user = userService.getById(userId);
        Location location = locationService.getById(locationId);
        user.setLocation(location);
        return userService.toDetails(userService.updateLocation(user));
    }
}
