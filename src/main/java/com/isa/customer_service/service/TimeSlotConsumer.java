//package com.isa.customer_service.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.isa.customer_service.dto.TimeSlotDto;
//import com.isa.customer_service.model.TimeSlot;
//import com.isa.customer_service.repository.ServiceRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//public class TimeSlotConsumer {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(TimeSlotConsumer.class);
//
//    private final ServiceRepository serviceRepository;
//
//    public TimeSlotConsumer(ServiceRepository serviceRepository) {
//        this.serviceRepository = serviceRepository;
//    }
//
//    @KafkaListener(topics = "times", groupId = "garage-group")
//    public void consumeBooking(TimeSlotDto timeSlotDto) {
//        LOGGER.info("Consumed Booking: {}", timeSlotDto);
//        if (timeSlotDto != null) {
//            TimeSlot timeSlot = new TimeSlot();
//            timeSlot.setSlotId(timeSlotDto.getSlotId());  // Example mapping
//            timeSlot.setServiceDate(timeSlotDto.getServiceDate());
//            timeSlot.setServiceStartTime(timeSlotDto.getServiceStartTime());
//            timeSlot.setMaxNoVehicle(timeSlotDto.getMaxNoVehicle());
//            timeSlot.setAvailableCount(timeSlotDto.getAvailableCount());
//            timeSlot.setVehicleStatus(timeSlotDto.getVehicleStatus());  // Assuming status is being sent
//
//            // Save to the repository
//            serviceRepository.save(timeSlot);
//
//            LOGGER.info("Booking saved to DB: {}", timeSlot);
//        } else {
//            LOGGER.warn("Received a null TimeSlotDto, no booking saved.");
//        }
//    }
//    private final List<TimeSlotDto> availableTimeSlots = new ArrayList<>();
//
//    @KafkaListener(topics = "times", groupId = "times-group")
//    public void availableTimeSlots(List<TimeSlotDto> availableTimeSlot){
//        try {
//            if (!availableTimeSlot.isEmpty()) {
//                String object = availableTimeSlot.toString();
//                String json = object.substring(1, object.length() - 1);
//                ObjectMapper objectMapper = new ObjectMapper();
//                TimeSlotDto resultObject = objectMapper.readValue(json, TimeSlotDto.class);
//                System.out.println("dileeban"+resultObject);
//                if (resultObject.getVehicleStatus().equals("available")) {
//                    availableTimeSlots.add(resultObject);
//                    TimeSlot availableTimeSlotsObject = TimeSlot.builder()
//                            .slotId(resultObject.getSlotId())
//                            .serviceDate(resultObject.getServiceDate())
//                            .serviceStartTime(resultObject.getServiceStartTime())
//                            .maxNoVehicle(resultObject.getMaxNoVehicle())
//                            .availableCount(resultObject.getAvailableCount())
//                            .vehicleStatus(resultObject.getVehicleStatus())
//                            .build();
//                    serviceRepository.save(availableTimeSlotsObject);
//                    System.out.println(availableTimeSlotsObject);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
