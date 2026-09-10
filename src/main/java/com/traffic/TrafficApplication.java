package com.traffic;

import com.traffic.model.*;
import com.traffic.service.ChallanService;

public class TrafficApplication {

    public static void main(String[] args) {

        ChallanService service =
                new ChallanService();

        System.out.println(
                "===== TRAFFIC VIOLATION AND E-CHALLAN SYSTEM ====="
        );

        Vehicle vehicle =
                new Vehicle(
                        "TN01AB1234",
                        "Arun Kumar",
                        "9876543210",
                        VehicleType.CAR
                );

        service.registerVehicle(vehicle);

        System.out.println();

        String challan1 =
                service.generateChallan(
                        "TN01AB1234",
                        ViolationType.OVER_SPEEDING,
                        "Katpadi Road",
                        "2026-09-10 10:30",
                        80,
                        50
                );

        System.out.println();

        String challan2 =
                service.generateChallan(
                        "TN01AB1234",
                        ViolationType.SIGNAL_VIOLATION,
                        "Vellore Signal",
                        "2026-09-10 11:00",
                        40,
                        40
                );

        System.out.println();

        System.out.println(
                "Outstanding Fine: ₹"
                        + service.getOutstandingFine(
                        "TN01AB1234"
                )
        );

        System.out.println(
                "Vehicle Classification: "
                        + service.classifyVehicle(
                        "TN01AB1234"
                )
        );

        System.out.println();

        service.payChallan(challan1);

        System.out.println(
                "Outstanding Fine After Payment: ₹"
                        + service.getOutstandingFine(
                        "TN01AB1234"
                )
        );

        System.out.println(
                "Payment Status: "
                        + service.getPaymentStatus(
                        challan1
                )
        );

        System.out.println(
                "Total Challans: "
                        + service.getChallanCount()
        );
    }
}
