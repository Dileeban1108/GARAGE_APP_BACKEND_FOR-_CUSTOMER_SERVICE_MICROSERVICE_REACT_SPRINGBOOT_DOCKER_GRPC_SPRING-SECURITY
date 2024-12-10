package com.isa.customer_service.model;

import jakarta.persistence.*;
import lombok.Data;

import lombok.Getter;

@Entity
@Data
public class VehiclesModels {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment behavior
    private int id;

    @Column(nullable = false, length = 255)
    private String vehicleType;

    @Column(nullable = false, length = 255)
    private String brandName;

    @Column(nullable = false, length = 255)
    private String modelName;

    @Column(nullable = false, length = 255)
    private String fuelType;

}
