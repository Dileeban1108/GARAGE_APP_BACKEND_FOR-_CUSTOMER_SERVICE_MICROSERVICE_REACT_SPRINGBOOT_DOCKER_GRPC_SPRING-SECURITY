package com.isa.customer_service.enums;

public enum VehicleStatus {
    AVAILABLE("Available"),
    NOT_AVAILABLE("Not Available");
    private final String
            vehicleStatus;
    VehicleStatus(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }
    public String getVehicleStatus() {
        return this.vehicleStatus;
    }

}
