//package com.isa.customer_service.service;
//import com.isa.customer_service.model.TimeSlot;
//import com.isa.customer_service.dto.BookingDto;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.stereotype.Service;
//import org.springframework.util.concurrent.ListenableFutureCallback;
//import java.util.concurrent.CompletableFuture;
//
//@Service
//public class BookingProducer {
//    private final KafkaTemplate<String, BookingDto> kafkaTemplate;
//    private static final Logger LOGGER = LoggerFactory.getLogger(BookingProducer.class);
//    public BookingProducer(KafkaTemplate<String, BookingDto> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//    public void sendBookingToServiceManager(BookingDto bookingDto)
//    {
//        LOGGER.info("Message send {}", bookingDto);
//        CompletableFuture<SendResult<String, BookingDto>> future = kafkaTemplate.send("garageapp",bookingDto);
//        future.whenComplete((result, ex) -> {
//            if (ex != null) {
//                LOGGER.error("Failed to send message: " + ex.getMessage());
//            } else {
//                LOGGER.info("Message sent successfully: " + result.getRecordMetadata());
//            }
//        });
//    }}