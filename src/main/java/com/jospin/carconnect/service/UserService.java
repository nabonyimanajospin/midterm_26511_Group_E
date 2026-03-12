package com.jospin.carconnect.service;

import com.jospin.carconnect.dto.UserPatchRequest;
import com.jospin.carconnect.dto.UserRequest;
import com.jospin.carconnect.dto.LocationDetailsResponse;
import com.jospin.carconnect.dto.UserDetailsResponse;
import com.jospin.carconnect.enums.UserType;
import com.jospin.carconnect.exception.BadRequestException;
import com.jospin.carconnect.exception.ResourceNotFoundException;
import com.jospin.carconnect.model.Location;
import com.jospin.carconnect.model.User;
import com.jospin.carconnect.repository.LocationRepository;
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
public class UserService {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    public User create(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(request.getPassword())
                .userType(UserType.valueOf(request.getUserType().toUpperCase()))
                .build();
        
        // Assign location if provided
        if (request.getLocationId() != null) {
            Location location = locationRepository.findById(request.getLocationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Location not found"));
            user.setLocation(location);
        }

        return userRepository.save(user);
    }

    public Page<User> getAll(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return userRepository.findAll(pageable);
    }

    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User update(UUID id, UserRequest request) {
        User user = getById(id);

        if (!user.getEmail().equalsIgnoreCase(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(request.getPassword());
        user.setUserType(UserType.valueOf(request.getUserType().toUpperCase()));
        
        // Update location if provided
        if (request.getLocationId() != null) {
            Location location = locationRepository.findById(request.getLocationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Location not found"));
            user.setLocation(location);
        }

        return userRepository.save(user);
    }

    public User patch(UUID id, UserPatchRequest request) {
        User user = getById(id);

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getUserType() != null) user.setUserType(UserType.valueOf(request.getUserType().toUpperCase()));

        return userRepository.save(user);
    }

    public void delete(UUID id) {
        User user = getById(id);
        userRepository.delete(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Page<User> findByProvince(String provinceIdentifier, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAllByProvinceCodeOrName(provinceIdentifier, provinceIdentifier, pageable);
    }
    
    public User updateLocation(User user) {
        return userRepository.save(user);
    }

    public UserDetailsResponse toDetails(User user) {
        return UserDetailsResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .userType(user.getUserType().name())
                .location(buildLocation(user))
                .build();
    }

    private LocationDetailsResponse buildLocation(User user) {
        if (user.getLocation() == null) {
            return null;
        }

        Location village = user.getLocation();
        Location cell = village.getParent();
        Location sector = cell != null ? cell.getParent() : null;
        Location district = sector != null ? sector.getParent() : null;
        Location province = district != null ? district.getParent() : null;

        return LocationDetailsResponse.builder()
                .userId(user.getId())
                .villageId(village.getId())
                .villageCode(village.getCode())
                .villageName(village.getName())
                .cellId(cell != null ? cell.getId() : null)
                .cellCode(cell != null ? cell.getCode() : null)
                .cellName(cell != null ? cell.getName() : null)
                .sectorId(sector != null ? sector.getId() : null)
                .sectorCode(sector != null ? sector.getCode() : null)
                .sectorName(sector != null ? sector.getName() : null)
                .districtId(district != null ? district.getId() : null)
                .districtCode(district != null ? district.getCode() : null)
                .districtName(district != null ? district.getName() : null)
                .provinceId(province != null ? province.getId() : null)
                .provinceCode(province != null ? province.getCode() : null)
                .provinceName(province != null ? province.getName() : null)
                .build();
    }
}
