package com.isa.customer_service.service;

import com.isa.customer_service.enums.ServiceStatus;
import com.isa.customer_service.model.Bookings;
//import com.isa.customer_service.model.TimeSlot;
import com.isa.customer_service.model.User;
import com.isa.customer_service.model.UserHistory;
import com.isa.customer_service.model.Vehicles;
import com.isa.customer_service.repository.*;
//import com.isa.customer_service.repository.ServiceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
//import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private UserHistoryRepository userHistoryRepository;
    @Autowired
    private ServiceRepository serviceRepository;

    private final UserRepository userRepository;
    public BookingService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public Bookings createBooking(Bookings bookings) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> currentUser = userRepository.findByEmail(email); // Fetch the user by email from the repository

        // Step 3: Get the current time and set the createdAt field for the booking
        LocalDateTime now = LocalDateTime.now(); // Get current time
        Date createdAt = Date.from(now.atZone(ZoneId.systemDefault()).toInstant()); // Convert to Date
        bookings.setCreatedAt(createdAt); // Set the createdAt field of the booking

        // Step 4: Check if the user has already booked the same service within the selected date range
        LocalDate selectedDate = bookings.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date startDate = java.sql.Date.valueOf(selectedDate.minusDays(1)); // Start of the booking date range (1 day before)
        Date endDate = java.sql.Date.valueOf(selectedDate.plusDays(1)); // End of the booking date range (1 day after)

        // Step 5: Check for any existing booking for the same user and service within the date range
        Optional<Bookings> existingBooking = bookingRepository.findExistingBookingForUser(
                currentUser.get().getUserId(),
                bookings.getVehicleNo(),
                bookings.getServiceId(),
                startDate,
                endDate
        );

        // Step 6: If a booking already exists, throw an exception
        if (existingBooking.isPresent()) {
            throw new IllegalStateException("You have already booked this service for the selected date.");
        }

        bookings.setUserId(currentUser.get().getUserId()); // Set the user ID for the booking
        // Step 9: Save the booking to the repository and return the saved booking
        Bookings savedBookings = bookingRepository.save(bookings);
        return savedBookings;
    }



    public Vehicles addVehicle(Vehicles vehicles) {
        // Get the currently authenticated user's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Fetch the user from the database by email
        Optional<User> currentUser = userRepository.findByEmail(email);

        // If the user doesn't exist, throw an exception (optional, depends on your use case)
        if (!currentUser.isPresent()) {
            throw new IllegalArgumentException("User not found.");
        }

        // Check if the vehicle with the same vehicle number exists for the specific user
        Vehicles existingVehicle = vehicleRepository.findByVehicleNoAndUserId(vehicles.getVehicleNo(), currentUser.get().getUserId());

        // If a vehicle already exists for this user, throw an exception
        if (existingVehicle != null) {
            throw new IllegalArgumentException("A vehicle with the same vehicle number already exists for this user.");
        }

        // Set the user ID for the vehicle
        vehicles.setUserId(currentUser.get().getUserId());

        // Save the new vehicle to the repository
        Vehicles savedVehicle = vehicleRepository.save(vehicles);

        // Return the saved vehicle
        return savedVehicle;
    }




    public List<UserHistory> getBookings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> currentUser = userRepository.findByEmail(email); // Fetch the user by email from the repository

        if (currentUser.isEmpty()) {
            throw new RuntimeException("User not found"); // Handle user not found
        }

        // Fetch the vehicles and bookings that belong to this user
        List<Vehicles> vehicles = vehicleRepository.findByUserId(currentUser.get().getUserId());
        List<Bookings> bookings = bookingRepository.findByUserId(currentUser.get().getUserId());

        // Check if there are no vehicles or bookings
        if (vehicles.isEmpty() || bookings.isEmpty()) {
            return new ArrayList<UserHistory>(); // Return an empty list if no bookings or vehicles are found
        }

        // Map bookings to UserHistory, including vehicle details
        List<UserHistory> userHistories = bookings.stream().map(booking -> {
                    // Check if the vehicle still exists in the vehicles list
                    Vehicles vehicle = vehicles.stream()
                            .filter(v -> v.getVehicleNo().equals(booking.getVehicleNo())) // Correct the reference to 'booking.getVehicleNo()'
                            .findFirst()
                            .orElse(null); // Return null if no vehicle is found

                    if (vehicle == null) {
                        // Log a warning and skip this booking if the vehicle is missing
                        System.out.println("Warning: Vehicle with vehicleNo " + booking.getVehicleNo() + " not found.");
                        return null; // Skip this booking
                    }

                    // Create and populate UserHistory entity
                    UserHistory userHistory = new UserHistory();
                    userHistory.setBookingId(booking.getBookingId());
                    userHistory.setUserId(booking.getUserId());
                    userHistory.setSlotId(booking.getSlotId());
                    userHistory.setServiceId(booking.getServiceId());
                    userHistory.setServiceName(booking.getServiceName());
                    userHistory.setServiceAcceptStatus(booking.getServiceAcceptStatus());
                    userHistory.setCreatedAt(booking.getCreatedAt());
                    userHistory.setBookingDate(booking.getBookingDate());

                    // Set vehicle-related data in the UserHistory entity
                    userHistory.setFuelType(vehicle.getFuelType());
                    userHistory.setVehicleBrand(vehicle.getVehicleBrand());
                    userHistory.setVehicleModel(vehicle.getVehicleModel());
                    userHistory.setVehicleNo(vehicle.getVehicleNo());
                    userHistory.setVehicleType(vehicle.getVehicleType());

                    // Set the rejection message based on the service accept status
                    String reason = "";
                    if (booking.getServiceAcceptStatus() == ServiceStatus.PENDING) {
                        reason = "Pending";
                    } else if (booking.getServiceAcceptStatus() == ServiceStatus.ACCEPTED) {
                        reason = "Accepted";
                    } else if (booking.getServiceAcceptStatus() == ServiceStatus.REJECTED) {
                        reason = booking.getRejectionMessage() != null ? booking.getRejectionMessage() : "Rejected";
                    }
                    userHistory.setRejectionMessage(reason);

                    // Save the UserHistory entity to the repository (if needed)
                    userHistoryRepository.save(userHistory);

                    // Return the UserHistory object
                    return userHistory;
                }).filter(Objects::nonNull) // Filter out null values if a vehicle is missing
                .collect(Collectors.toList());

        // Return the list of UserHistory entities
        return userHistories;
    }

        public List<Vehicles> getVehicles(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> currentUser = userRepository.findByEmail(email); // Fetch the user by email from the repository

        if (currentUser.isEmpty()) {
            throw new RuntimeException("User not found"); // Handle user not found
        }
            // Handle case when the booking is not found

        // Fetch the bookings that belong to this user
        return vehicleRepository.findByUserId(currentUser.get().getUserId()); // Assuming a method like this exists
    }

    public List<UserHistory> getAllBookings() {
        return userHistoryRepository.findAll();// Assuming a method like this exists
    }

    public void deleteVehicle(String vehicleNo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> currentUser = userRepository.findByEmail(email); // Fetch the user by email from the repository

        if (currentUser.isEmpty()) {
            throw new RuntimeException("User not found"); // Handle user not found
        }

        Vehicles vehicle = vehicleRepository.findByVehicleNo(vehicleNo);

        if (vehicle != null) {
            // Delete the vehicle
            vehicleRepository.delete(vehicle);
        } else {
            // Optionally, handle the case when the vehicle is not found
            throw new EntityNotFoundException("Vehicle with number " + vehicleNo + " not found");
        }
    }
    public void deleteBooking(Long bookingId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> currentUser = userRepository.findByEmail(email); // Fetch the user by email from the repository

        if (currentUser.isEmpty()) {
            throw new RuntimeException("User not found"); // Handle user not found
        }
        // Fetch the booking by bookingId
        Optional<Bookings> booking = bookingRepository.findByBookingId(bookingId);

        if (booking == null) {
            throw new EntityNotFoundException("Booking with ID " + bookingId + " not found");
        }

        Bookings bookingToDelete = booking.get();
        // Delete the booking
        bookingRepository.delete(bookingToDelete);
    }


}
