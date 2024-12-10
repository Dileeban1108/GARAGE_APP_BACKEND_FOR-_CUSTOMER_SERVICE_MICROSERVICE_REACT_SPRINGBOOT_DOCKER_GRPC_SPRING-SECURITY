package com.isa.customer_service.repository;

import com.isa.customer_service.model.VehiclesModels;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehiclesModelsRepository extends JpaRepository<VehiclesModels, Integer> {
    List<VehiclesModels> findByVehicleType(String vehicleType);

    // Fetch all records by vehicle type and brand name
    List<VehiclesModels> findByVehicleTypeAndBrandName(String vehicleType, String brandName);
}
