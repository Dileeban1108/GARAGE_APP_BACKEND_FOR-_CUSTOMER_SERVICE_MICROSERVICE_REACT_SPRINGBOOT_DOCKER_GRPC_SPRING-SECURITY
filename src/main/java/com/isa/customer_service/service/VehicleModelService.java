package com.isa.customer_service.service;

import com.isa.customer_service.model.VehiclesModels;
import com.isa.customer_service.repository.VehiclesModelsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleModelService {

    @Autowired
    private VehiclesModelsRepository vehiclesModelsRepository;

    public List<String> getAllVehicleTypes() {
        return vehiclesModelsRepository.findAll().stream()
                .map(VehiclesModels::getVehicleType)
                .distinct()
                .collect(Collectors.toList());
    }

    // Get all brands for a specific vehicle type
    public List<String> getBrandsByVehicleType(String vehicleType) {
        return vehiclesModelsRepository.findByVehicleType(vehicleType).stream()
                .map(VehiclesModels::getBrandName)
                .distinct()
                .collect(Collectors.toList());
    }

    // Get all models for a specific vehicle type and brand
    public List<String> getModelsByVehicleTypeAndBrand(String vehicleType, String brandName) {
        return vehiclesModelsRepository.findByVehicleTypeAndBrandName(vehicleType, brandName).stream()
                .map(VehiclesModels::getModelName)
                .collect(Collectors.toList());
    }
}
