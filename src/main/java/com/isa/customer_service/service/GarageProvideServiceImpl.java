package com.isa.customer_service.service;

import com.isa.customer_service.dto.ServiceTypeDto;
import isa.example.garage.grpc.GarageProvideServiceGrpc;
import isa.example.garage.grpc.GarageServiceRequest;
import isa.example.garage.grpc.GarageServiceResponse;
import isa.example.garage.grpc.ProvideService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GarageProvideServiceImpl {

    // Use the blocking stub for making gRPC requests
    private final GarageProvideServiceGrpc.GarageProvideServiceBlockingStub garageProvideServiceStub;

    // Constructor accepting the blocking stub
    public GarageProvideServiceImpl(GarageProvideServiceGrpc.GarageProvideServiceBlockingStub garageProvideServiceStub) {
        this.garageProvideServiceStub = garageProvideServiceStub;
    }

    // Method to get the garage services and return them as ServiceTypeDto objects
    public List<ServiceTypeDto> getGarageServices() {
        // Create the request (if needed, add data to the request)
        GarageServiceRequest request = GarageServiceRequest.newBuilder().build();

        // Call the gRPC service and get the response
        GarageServiceResponse response = garageProvideServiceStub.getGarageService(request);

        // Map the response to a list of ServiceTypeDto
        return mapToServiceTypeDtoList(response);
    }

    // Helper method to map a single ProvideService object to ServiceTypeDto
    private ServiceTypeDto mapToServiceTypeDto(ProvideService provideService) {
        return new ServiceTypeDto(
                provideService.getServiceId(),
                provideService.getServiceName(),
                provideService.getVehicleType()
        );
    }

    // Helper method to map the entire response to a list of ServiceTypeDto
    private List<ServiceTypeDto> mapToServiceTypeDtoList(GarageServiceResponse response) {
        return response.getProvideServiceList().stream()
                .map(this::mapToServiceTypeDto)
                .collect(Collectors.toList());
    }
}
