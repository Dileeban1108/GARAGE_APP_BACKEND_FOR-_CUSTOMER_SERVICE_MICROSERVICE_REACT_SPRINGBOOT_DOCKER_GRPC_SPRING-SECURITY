package com.isa.customer_service.controller;

import com.isa.customer_service.model.VehiclesModels;
import com.isa.customer_service.service.VehicleModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vehicle-models")
public class VehicleModelController {

    @Autowired
    private VehicleModelService vehicleModelService;

    // Fetch vehicle models by vehicle type and brand
    @GetMapping("/types")
    public List<String> getAllVehicleTypes() {
        return vehicleModelService.getAllVehicleTypes();
    }

    // Get all brands for a specific vehicle type
    @GetMapping("/brands")
    public List<String> getBrandsByVehicleType(@RequestParam String vehicleType) {
        return vehicleModelService.getBrandsByVehicleType(vehicleType);
    }

    // Get all models for a specific vehicle type and brand
    @GetMapping("/models")
    public List<String> getModelsByVehicleTypeAndBrand(@RequestParam String vehicleType, @RequestParam String brandName) {
        return vehicleModelService.getModelsByVehicleTypeAndBrand(vehicleType, brandName);
    }
}
