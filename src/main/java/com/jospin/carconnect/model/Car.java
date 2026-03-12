package com.jospin.carconnect.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jospin.carconnect.enums.CarStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cars", uniqueConstraints = {
        @UniqueConstraint(name = "uk_car_plate_number", columnNames = "plate_number")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(max = 20)
    @Column(name = "plate_number", nullable = false, length = 20)
    private String plateNumber;

    @NotBlank
    @Size(max = 80)
    @Column(name = "brand", nullable = false, length = 80)
    private String brand;

    @NotBlank
    @Size(max = 80)
    @Column(name = "model", nullable = false, length = 80)
    private String model;

    @NotNull
    @Column(name = "year", nullable = false)
    private Integer year;

    @NotNull
    @Positive
    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @NotNull
    @Column(name = "mileage", nullable = false)
    private Integer mileage;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private CarStatus status;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "car_categories",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inquiry> inquiries = new ArrayList<>();
}
