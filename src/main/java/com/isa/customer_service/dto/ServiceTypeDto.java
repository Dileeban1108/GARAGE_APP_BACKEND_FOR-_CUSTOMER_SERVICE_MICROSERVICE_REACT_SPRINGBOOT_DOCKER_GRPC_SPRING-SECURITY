package com.isa.customer_service.dto;

import lombok.Data;

@Data
public class ServiceTypeDto {
    private Long serviceId;
    private String serviceName;
    private String vehicleType;

    public ServiceTypeDto(long serviceId, String serviceName, String vehicleType) {
        this.serviceId=serviceId;
        this.serviceName=serviceName;
        this.vehicleType=vehicleType;
    }

}
