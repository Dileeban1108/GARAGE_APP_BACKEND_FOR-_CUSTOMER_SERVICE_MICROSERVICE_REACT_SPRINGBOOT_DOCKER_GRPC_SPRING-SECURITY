package com.isa.customer_service.dto;
import com.isa.customer_service.enums.VehicleStatus;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class TimeSlotDto implements Serializable {
    private Integer slotId;
    private LocalDate serviceDate;
    private LocalTime serviceStartTime;
    private LocalTime serviceEndTime;
    private Integer maxNoVehicle;
    private Integer availableCount;
    private VehicleStatus vehicleStatus = VehicleStatus.AVAILABLE;
}
