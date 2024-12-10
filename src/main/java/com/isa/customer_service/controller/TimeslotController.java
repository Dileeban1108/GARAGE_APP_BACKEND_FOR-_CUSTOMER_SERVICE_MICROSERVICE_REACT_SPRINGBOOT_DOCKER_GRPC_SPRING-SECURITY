package com.isa.customer_service.controller;

import com.isa.customer_service.dto.TimeSlotDto;
import isa.example.garage.grpc.Timeslot;
import com.isa.customer_service.model.TimeSlot;
import com.isa.customer_service.service.TimeslotGrpcClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TimeslotController {

    private final TimeslotGrpcClientService timeslotGrpcClientService;

    @Autowired
    public TimeslotController(TimeslotGrpcClientService timeslotGrpcClientService) {
        this.timeslotGrpcClientService = timeslotGrpcClientService;
    }

    @GetMapping("/timeslots")
    public List<TimeSlotDto> getAvailableTimeslots(@RequestParam String serviceDate) {
        return timeslotGrpcClientService.getAvailableTimeSlots(serviceDate);
    }
}
