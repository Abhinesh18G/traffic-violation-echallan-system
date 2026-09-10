package com.traffic.model;

public class Vehicle {

    private String vehicleNumber;
    private String ownerName;
    private String ownerPhone;
    private VehicleType vehicleType;

    private int violationCount;

    public Vehicle(String vehicleNumber,
                   String ownerName,
                   String ownerPhone,
                   VehicleType vehicleType) {

        this.vehicleNumber = vehicleNumber;
        this.ownerName = ownerName;
        this.ownerPhone = ownerPhone;
        this.vehicleType = vehicleType;
        this.violationCount = 0;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public int getViolationCount() {
        return violationCount;
    }

    public void incrementViolationCount() {
        violationCount++;
    }
}
