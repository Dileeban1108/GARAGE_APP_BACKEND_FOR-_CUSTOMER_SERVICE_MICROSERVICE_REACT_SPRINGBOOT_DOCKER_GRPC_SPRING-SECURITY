package com.isa.customer_service.service;
import com.isa.customer_service.model.Vehicles;
import com.isa.customer_service.repository.VehicleRepository;
import isa.example.garage.grpc.*;
import com.isa.customer_service.model.Bookings;
import com.isa.customer_service.repository.BookingRepository;
import io.grpc.stub.StreamObserver;
import isa.example.garage.grpc.BookingServiceGrpc;
import isa.example.garage.grpc.GetBookingsByDateRequest;
import isa.example.garage.grpc.GetBookingsByDateResponse;
import isa.example.garage.grpc.GetAcceptedBookingRequest;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;
@GrpcService
public class BookingServiceImpl extends BookingServiceGrpc.BookingServiceImplBase {

    private final BookingRepository bookingRepo;  // Repository for accessing bookings data
    private final VehicleRepository vehicleRepository;  // Repository for accessing vehicle data

    // Constructor to initialize the repositories
    public BookingServiceImpl(BookingRepository bookingRepo, VehicleRepository vehicleRepository) {
        this.bookingRepo = bookingRepo;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public void getBookingsByDate(GetBookingsByDateRequest request, StreamObserver<GetBookingsByDateResponse> responseObserver) {
        // Fetch bookings for a specific date (this can be refined later to use the request date)
        List<Bookings> bookingsList = bookingRepo.findByServiceAcceptStatus(com.isa.customer_service.enums.ServiceStatus.PENDING);  // Fetch only pending bookings

        // Convert bookings to protobuf objects
        List<Booking> bookingProtos = bookingsList.stream().map(bookings -> {
            // Assuming that there is a `vehicleNo` or `vehicleId` in the `Bookings` object
            List<Vehicles> vehiclesList = vehicleRepository.findByUserId(bookings.getUserId());

            if (vehiclesList.isEmpty()) {
                // If no vehicle is found for the user, handle accordingly (you can log, return null, etc.)
                return null;  // Skipping this booking if no vehicle is found
            }

            // For simplicity, we can use the first vehicle in the list (you can refine this logic as needed)
            Vehicles vehicle = vehiclesList.get(0);  // Select the first vehicle in the list

            // Convert the ServiceAcceptStatus to the gRPC enum
            ServiceStatus grpcStatus = convertToProtoStatus(bookings.getServiceAcceptStatus());

            return Booking.newBuilder()
                    .setBookingId(bookings.getBookingId())
                    .setUserId(bookings.getUserId())
                    .setSlotId(bookings.getSlotId())
                    .setServiceId(bookings.getServiceId().toString())
                    .setServiceName(bookings.getServiceName())
                    .setServiceAcceptStatus(grpcStatus)  // Use the converted status
                    .setCreatedAt(bookings.getCreatedAt().toString())
                    .setFuelType(vehicle.getFuelType())
                    .setVehicleBrand(vehicle.getVehicleBrand())
                    .setVehicleModel(vehicle.getVehicleModel())
                    .setVehicleNo(vehicle.getVehicleNo())
                    .setVehicleType(vehicle.getVehicleType())
                    .setBookingDate(bookings.getBookingDate().toString()) // Assuming `Bookings` has `bookingDate`
                    .build();
        }).filter(booking -> booking != null).collect(Collectors.toList());

        // Build the response with the list of bookings
        GetBookingsByDateResponse response = GetBookingsByDateResponse.newBuilder()
                .addAllBookings(bookingProtos)
                .build();

        // Send the response to the client
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    @Override
    public void getAcceptBookings(GetAcceptedBookingRequest request, StreamObserver<GetAcceptedBookingResponse> responseObserver) {
        // Fetch bookings for a specific date (this can be refined later to use the request date)
        List<Bookings> bookingsList = bookingRepo.findByServiceAcceptStatus(com.isa.customer_service.enums.ServiceStatus.ACCEPTED);  // Fetch only pending bookings

        // Convert bookings to protobuf objects
        List<Booking> bookingProtos = bookingsList.stream().map(bookings -> {
            // Assuming that there is a `vehicleNo` or `vehicleId` in the `Bookings` object
            List<Vehicles> vehiclesList = vehicleRepository.findByUserId(bookings.getUserId());

            if (vehiclesList.isEmpty()) {
                // If no vehicle is found for the user, handle accordingly (you can log, return null, etc.)
                return null;  // Skipping this booking if no vehicle is found
            }

            // For simplicity, we can use the first vehicle in the list (you can refine this logic as needed)
            Vehicles vehicle = vehiclesList.get(0);  // Select the first vehicle in the list

            // Convert the ServiceAcceptStatus to the gRPC enum
            ServiceStatus grpcStatus = convertToProtoStatus(bookings.getServiceAcceptStatus());

            return Booking.newBuilder()
                    .setBookingId(bookings.getBookingId())
                    .setUserId(bookings.getUserId())
                    .setSlotId(bookings.getSlotId())
                    .setServiceId(bookings.getServiceId().toString())
                    .setServiceName(bookings.getServiceName())
                    .setServiceAcceptStatus(grpcStatus)  // Use the converted status
                    .setCreatedAt(bookings.getCreatedAt().toString())
                    .setFuelType(vehicle.getFuelType())
                    .setVehicleBrand(vehicle.getVehicleBrand())
                    .setVehicleModel(vehicle.getVehicleModel())
                    .setVehicleNo(vehicle.getVehicleNo())
                    .setVehicleType(vehicle.getVehicleType())
                    .setBookingDate(bookings.getBookingDate().toString()) // Assuming `Bookings` has `bookingDate`
                    .build();
        }).filter(booking -> booking != null).collect(Collectors.toList());

        // Build the response with the list of bookings
        GetAcceptedBookingResponse response = GetAcceptedBookingResponse.newBuilder()
                .addAllBookings(bookingProtos)
                .build();

        // Send the response to the client
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    public ServiceStatus convertToProtoStatus(com.isa.customer_service.enums.ServiceStatus serviceStatus) {
        switch (serviceStatus) {
            case PENDING:
                return ServiceStatus.PENDING;
            case ACCEPTED:
                return ServiceStatus.ACCEPTED;
            case REJECTED:
                return ServiceStatus.REJECTED;
            default:
                throw new IllegalArgumentException("Unknown service status: " + serviceStatus);
        }
    }

    @Override
    public void bookingAccept(GetBookingAcceptRequest request, StreamObserver<GetBookingAcceptResponse> responseObserver){
        long bookingId = request.getBookingId();
        long userId = request.getUserId();

        // Find the booking by ID and check if it exists
        Bookings booking = bookingRepo.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Check if the user matches the one who made the request
        if (booking.getUserId() != userId) {
            responseObserver.onError(new RuntimeException("Unauthorized request: This booking does not belong to the user"));
            return;
        }

        // Change the booking status to ACCEPTED
        booking.setServiceAcceptStatus(com.isa.customer_service.enums.ServiceStatus.ACCEPTED);
        // Save the updated booking (this depends on your persistence setup)
        bookingRepo.save(booking);

        // Send a response (empty as per your definition in the proto)
        responseObserver.onNext(GetBookingAcceptResponse.getDefaultInstance());
        responseObserver.onCompleted();
    }
    @Override
    public void bookingReject(GetBookingRejectRequest request, StreamObserver<GetBookingRejectResponse> responseObserver){
        long bookingId = request.getBookingId();
        long userId = request.getUserId();
        String rejectionMessage = request.getMessage();  // Get the rejection message from the request
        // Find the booking by ID and check if it exists
        Bookings booking = bookingRepo.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Check if the user matches the one who made the request
        if (booking.getUserId() != userId) {
            responseObserver.onError(new RuntimeException("Unauthorized request: This booking does not belong to the user"));
            return;
        }
        booking.setRejectionMessage(rejectionMessage);  // Store the rejection message
        // Change the booking status to ACCEPTED
        booking.setServiceAcceptStatus(com.isa.customer_service.enums.ServiceStatus.REJECTED);
        // Save the updated booking (this depends on your persistence setup)
        bookingRepo.save(booking);
        responseObserver.onNext(GetBookingRejectResponse.getDefaultInstance());
        responseObserver.onCompleted();
    }
}


