package com.isa.customer_service.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.isa.customer_service.enums.VehicleStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "timeslots")
public class TimeSlot {
    @Id
    private Integer slotId;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate serviceDate;

    @Column(nullable = false)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime serviceStartTime;

    @Column(nullable = false)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime serviceEndTime;

    @Column(nullable = false)
    private Integer maxNoVehicle;

    @Column(nullable = false)
    private Integer availableCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleStatus vehicleStatus = VehicleStatus.AVAILABLE;
}
