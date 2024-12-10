package com.isa.customer_service.service;

import com.isa.customer_service.dto.TimeSlotDto;
import isa.example.garage.grpc.*;
import com.isa.customer_service.enums.VehicleStatus; // Import your internal enum
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeslotGrpcClientService {

    @Autowired
    private TimeslotServiceGrpc.TimeslotServiceBlockingStub timeslotServiceBlockingStub;

    public List<TimeSlotDto> getAvailableTimeSlots(String serviceDate) {
        try {
            // Create the request with the service date
            TimeslotRequest request = TimeslotRequest.newBuilder()
                    .setServiceDate(serviceDate)
                    .build();

            // Fetch the response from the service
            TimeslotResponse response = timeslotServiceBlockingStub.getTimeslots(request);

            // Log the entire response (or specific fields)
            System.out.println("Received TimeslotResponse: " + response);

            // Ensure the response contains the expected list of timeslots
            if (response == null || response.getTimeslotsList().isEmpty()) {
                throw new RuntimeException("No timeslots available for the given date.");
            }

            // Filter the timeslots for those with VehicleStatus.AVAILABLE
            List<TimeSlotDto> availableTimeSlots = response.getTimeslotsList().stream()
                    .filter(timeslot -> timeslot.getVehicleStatus() == isa.example.garage.grpc.VehicleStatus.AVAILABLE)  // Only get available slots
                    .map(this::convertToTimeSlotDto)  // Convert each Timeslot to TimeSlotDto
                    .collect(Collectors.toList());

            return availableTimeSlots;
        } catch (Exception e) {
            // Add detailed logging for exceptions
            System.err.println("Error fetching timeslots: " + e.getMessage());
            throw new RuntimeException("Failed to fetch timeslots: " + e.getMessage(), e);
        }
    }

    private TimeSlotDto convertToTimeSlotDto(Timeslot timeslot) {
        TimeSlotDto dto = new TimeSlotDto();

        // Convert fields from the GRPC Timeslot to TimeSlotDto
        dto.setSlotId(timeslot.getSlotId());
        dto.setServiceDate(LocalDate.parse(timeslot.getServiceDate())); // Assuming serviceDate is an ISO string
        dto.setServiceStartTime(LocalTime.parse(timeslot.getStartTime())); // Assuming serviceStartTime is an ISO string
        dto.setServiceEndTime(LocalTime.parse(timeslot.getEndTime())); // Assuming serviceEndTime is an ISO string
        dto.setMaxNoVehicle(timeslot.getMaxNoVehicle());
        dto.setAvailableCount(timeslot.getAvailableCount());
        dto.setVehicleStatus(mapGrpcVehicleStatus(timeslot.getVehicleStatus()));  // Use the correct mapping function
        return dto;
    }

    // Method to map GRPC VehicleStatus to internal VehicleStatus
    private VehicleStatus mapGrpcVehicleStatus(isa.example.garage.grpc.VehicleStatus grpcStatus) {
        // Map the GRPC enum to your internal enum
        switch (grpcStatus) {
            case AVAILABLE:
                return VehicleStatus.AVAILABLE;
            case NOT_AVAILABLE:
                return VehicleStatus.NOT_AVAILABLE;
            default:
                throw new IllegalArgumentException("Unknown VehicleStatus: " + grpcStatus);
        }
    }
}
