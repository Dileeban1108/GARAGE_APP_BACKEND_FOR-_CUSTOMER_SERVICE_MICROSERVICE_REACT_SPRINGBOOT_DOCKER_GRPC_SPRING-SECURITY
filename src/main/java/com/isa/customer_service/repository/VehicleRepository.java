package com.isa.customer_service.repository;

import com.isa.customer_service.model.Vehicles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicles,String> {
    List<Vehicles> findByUserId(Long userId);  // To get all bookings for a specific user
    Vehicles findByVehicleNo(String vehicleNo);
    Vehicles findByVehicleNoAndUserId(String vehicleNo, Long userId);
}
