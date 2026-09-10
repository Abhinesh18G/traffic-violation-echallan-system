package com.traffic.service;

import com.traffic.exception.DuplicateChallanException;
import com.traffic.exception.InvalidVehicleException;
import com.traffic.model.*;

import java.util.*;

public class ChallanService {

    private final Map<String, Vehicle> vehicles =
            new HashMap<>();

    private final Map<String, String> challans =
            new HashMap<>();

    private final Map<String, Double> fines =
            new HashMap<>();

    private final Map<String, PaymentStatus> paymentStatus =
            new HashMap<>();

    private final Map<String, ViolationType> violationTypes =
            new HashMap<>();

    public void registerVehicle(Vehicle vehicle) {

        validateVehicle(vehicle);

        if (vehicles.containsKey(
                vehicle.getVehicleNumber())) {

            throw new InvalidVehicleException(
                    "Vehicle already registered."
            );
        }

        vehicles.put(
                vehicle.getVehicleNumber(),
                vehicle
        );

        System.out.println(
                "Vehicle registered: "
                        + vehicle.getVehicleNumber()
        );
    }

    private void validateVehicle(Vehicle vehicle) {

        if (vehicle == null) {

            throw new InvalidVehicleException(
                    "Vehicle cannot be null."
            );
        }

        if (vehicle.getVehicleNumber() == null ||
                vehicle.getVehicleNumber().isEmpty()) {

            throw new InvalidVehicleException(
                    "Invalid vehicle number."
            );
        }

        if (vehicle.getOwnerName() == null ||
                vehicle.getOwnerName().isEmpty()) {

            throw new InvalidVehicleException(
                    "Owner name is required."
            );
        }

        if (vehicle.getVehicleType() == null) {

            throw new InvalidVehicleException(
                    "Vehicle type is required."
            );
        }
    }

    public String generateChallan(
            String vehicleNumber,
            ViolationType violationType,
            String location,
            String timestamp,
            double speed,
            double permittedSpeed) {

        if (!vehicles.containsKey(vehicleNumber)) {

            throw new InvalidVehicleException(
                    "Vehicle is not registered."
            );
        }

        if (violationType == null) {

            throw new InvalidVehicleException(
                    "Violation type is required."
            );
        }

        if (location == null ||
                location.isEmpty()) {

            throw new InvalidVehicleException(
                    "Violation location is required."
            );
        }

        if (timestamp == null ||
                timestamp.isEmpty()) {

            throw new InvalidVehicleException(
                    "Timestamp is required."
            );
        }

        if (speed < 0 ||
                permittedSpeed < 0) {

            throw new InvalidVehicleException(
                    "Speed cannot be negative."
            );
        }

        String eventKey =
                vehicleNumber + "|"
                        + violationType + "|"
                        + location + "|"
                        + timestamp;

        if (challans.containsKey(eventKey)) {

            throw new DuplicateChallanException(
                    "Duplicate challan for the same violation event."
            );
        }

        Vehicle vehicle =
                vehicles.get(vehicleNumber);

        vehicle.incrementViolationCount();

        double fine =
                calculateFine(
                        vehicle,
                        violationType,
                        speed,
                        permittedSpeed
                );

        String challanId =
                "CH-" + (challans.size() + 1001);

        challans.put(
                eventKey,
                challanId
        );

        fines.put(
                challanId,
                fine
        );

        paymentStatus.put(
                challanId,
                PaymentStatus.UNPAID
        );

        violationTypes.put(
                challanId,
                violationType
        );

        System.out.println(
                "E-Challan generated: "
                        + challanId
        );

        System.out.println(
                "Fine amount: ₹"
                        + fine
        );

        return challanId;
    }

    private double calculateFine(
            Vehicle vehicle,
            ViolationType violationType,
            double speed,
            double permittedSpeed) {

        double fine =
                violationType.getBaseFine();

        if (violationType ==
                ViolationType.OVER_SPEEDING) {

            double excess =
                    speed - permittedSpeed;

            if (excess >= 20) {

                fine = fine * 2;

            } else if (excess >= 10) {

                fine = fine * 1.5;
            }
        }

        if (vehicle.getViolationCount() >= 3) {

            fine = fine * 2;
        } else if (vehicle.getViolationCount() == 2) {

            fine = fine * 1.5;
        }

        return fine;
    }

    public void payChallan(String challanId) {

        if (!fines.containsKey(challanId)) {

            throw new InvalidVehicleException(
                    "Challan ID not found."
            );
        }

        if (paymentStatus.get(challanId)
                == PaymentStatus.PAID) {

            throw new InvalidVehicleException(
                    "Challan is already paid."
            );
        }

        paymentStatus.put(
                challanId,
                PaymentStatus.PAID
        );

        System.out.println(
                "Payment successful for "
                        + challanId
        );
    }

    public double getOutstandingFine(
            String vehicleNumber) {

        double total = 0;

        for (Map.Entry<String, String> entry :
                challans.entrySet()) {

            String eventKey =
                    entry.getKey();

            if (eventKey.startsWith(
                    vehicleNumber + "|")) {

                String challanId =
                        entry.getValue();

                if (paymentStatus.get(challanId)
                        == PaymentStatus.UNPAID) {

                    total += fines.get(challanId);
                }
            }
        }

        return total;
    }

    public VehicleClassification classifyVehicle(
            String vehicleNumber) {

        Vehicle vehicle =
                vehicles.get(vehicleNumber);

        if (vehicle == null) {

            throw new InvalidVehicleException(
                    "Vehicle not found."
            );
        }

        int count =
                vehicle.getViolationCount();

        if (count == 0) {

            return VehicleClassification.CLEAN;

        } else if (count <= 2) {

            return VehicleClassification.LOW_RISK;

        } else if (count <= 4) {

            return VehicleClassification.HIGH_RISK;

        } else {

            return VehicleClassification.REPEAT_OFFENDER;
        }
    }

    public PaymentStatus getPaymentStatus(
            String challanId) {

        return paymentStatus.get(challanId);
    }

    public double getFine(String challanId) {

        return fines.get(challanId);
    }

    public int getVehicleCount() {

        return vehicles.size();
    }

    public int getChallanCount() {

        return challans.size();
    }
}
