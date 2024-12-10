package com.isa.customer_service.enums;

public enum ServiceStatus {
    PENDING("Pending"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected");
    private final String serviceStatus;
    ServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }
    public String getServiceStatus() {
        return this.serviceStatus;
    }
}
