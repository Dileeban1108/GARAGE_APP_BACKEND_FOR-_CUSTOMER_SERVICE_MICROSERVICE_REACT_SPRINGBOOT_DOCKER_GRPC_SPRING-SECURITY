package com.isa.customer_service.dto;

import com.isa.customer_service.enums.ServiceStatus;
import lombok.Data;
import java.util.Date;
@Data
public class BookingDto {
    private Long bookingId;
    private Long userId;
    private Integer slotId;
    private String serviceId;
    private String serviceName;
    private ServiceStatus serviceAcceptStatus = ServiceStatus.PENDING;
    private Date createdAt;
    private String vehicleNo;
    private String vehicleType;
    private String vehicleBrand;
    private String vehicleModel;
    private String fuelType;
    private Date bookingDate;
}
