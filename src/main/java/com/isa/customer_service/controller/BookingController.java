package com.isa.customer_service.controller;

import com.isa.customer_service.dto.ServiceTypeDto;
import com.isa.customer_service.model.Bookings;
import com.isa.customer_service.model.UserHistory;
import com.isa.customer_service.model.Vehicles;
import com.isa.customer_service.service.BookingService;
import com.isa.customer_service.service.GarageProvideServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private GarageProvideServiceImpl garageProvideService;

    @Autowired
    public void BookingController(GarageProvideServiceImpl garageProvideService){
        this.garageProvideService=garageProvideService;
    }
    @PostMapping("/createBooking")
    public ResponseEntity<Bookings> createBooking(@RequestBody Bookings bookings) {
        try {
            System.out.println("Booking details: " + bookings);
            Bookings createdBookings = bookingService.createBooking(bookings);
            return ResponseEntity.ok(createdBookings);
        } catch (IllegalStateException e) {
            // Handle the case when the user has already booked the same service
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/addVehicle")
    public ResponseEntity<Vehicles> addVehicle(@RequestBody Vehicles vehicles) {
        try {
            Vehicles addVehicles = bookingService.addVehicle(vehicles);
            return ResponseEntity.ok(addVehicles);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



//    @PostMapping("/send")
//    public void testing(@RequestBody String msg) {
//        bookingService.send(msg);
//    }

    //get all bookings
    @GetMapping("/getAllBookings")
    public ResponseEntity<List<UserHistory>> getAllBookings() {
        // Call the service to get bookings for the current authenticated user
        List<UserHistory> bookings = bookingService.getAllBookings();

        if (bookings.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(bookings);
    }

    // Get all bookings for a user
    @GetMapping("/getBookings")
    public List<UserHistory> getBookingsByUserId() {
        // Call the service to get bookings for the current authenticated user
        List<UserHistory> bookingDtos = bookingService.getBookings();

        return bookingDtos; // Return 200 OK with the list of BookingDto
    }

    @GetMapping("/getVehicles")
    public ResponseEntity<List<Vehicles>> getVehiclesByUserId() {
        // Call the service to get bookings for the current authenticated user
        List<Vehicles> vehicles = bookingService.getVehicles();

        if (vehicles.isEmpty()) {
            return ResponseEntity.notFound().build(); // Return 404 if no bookings are found
        }

        return ResponseEntity.ok(vehicles); // Return 200 OK with the list of bookings
    }
    @DeleteMapping("/deleteVehicle/{vehicleNo}")
    public ResponseEntity<String> deleteVehicle(@PathVariable String vehicleNo) {
        // Logic to delete the vehicle by ID
        bookingService.deleteVehicle(vehicleNo);
        return ResponseEntity.ok("Vehicle removed successfully!");
    }
    @DeleteMapping("/deleteBooking/{bookingId}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long bookingId) {
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.ok("Booking removed successfully!");
    }
    @GetMapping("/services")
    public List<ServiceTypeDto> getGarageServices() {
        return garageProvideService.getGarageServices();
    }

}
