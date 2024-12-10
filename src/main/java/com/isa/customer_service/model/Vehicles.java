package com.isa.customer_service.model;

import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
public class Vehicles {

    @Id
    @NotNull(message = "vehicle Number cannot be null")
    private String vehicleNo;
    private Long userId;
    @NotNull(message = "vehicle Type cannot be null")
    private String vehicleType;    // Vehicle Type (Car, Bike, Truck)
    @NotNull(message = "vehicle Brand cannot be null")
    private String vehicleBrand;   // Vehicle Brand
    @NotNull(message = "vehicle Model cannot be null")
    private String vehicleModel;   // Vehicle Model
    @NotNull(message = "Fuel Type cannot be null")
    private String fuelType;
}
