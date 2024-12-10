package com.isa.customer_service.model;

import com.isa.customer_service.enums.ServiceStatus;
import jakarta.persistence.*;
import java.util.Date;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "bookings")
public class Bookings {

    @Id
    @Column(name = "booking_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @NotNull(message = "User ID cannot be null")
    @Column(name = "user_id")
    private Long userId;

    @NotNull(message = "Slot ID cannot be null")
    @Min(value = 1, message = "Slot ID must be greater than 0")
    @Column(name = "slot_id")
    private Integer slotId;

    @NotBlank(message = "Service ID cannot be blank")
    @Column(name = "service_id")
    private Long serviceId;

    @NotBlank(message = "Service Name cannot be blank")
    @Column(name = "service_name")
    private String serviceName;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_accept_status", nullable = false)
    private ServiceStatus serviceAcceptStatus = ServiceStatus.PENDING;

    @NotNull(message = "Created At cannot be null")
    @Column(name = "created_at")
    private Date createdAt;

    @NotNull(message = "date cannot be null")
    private Date bookingDate;

    @Column(name = "rejection_message")
    private String rejectionMessage;

    @NotNull(message = "vehicle Number cannot be null")
    private String vehicleNo;


}
