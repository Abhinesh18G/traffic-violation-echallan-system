package com.traffic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.traffic.exception.DuplicateChallanException;
import com.traffic.exception.InvalidVehicleException;
import com.traffic.model.PaymentStatus;
import com.traffic.model.Vehicle;
import com.traffic.model.VehicleClassification;
import com.traffic.model.VehicleType;
import com.traffic.model.ViolationType;

public class ChallanServiceTest {

    private ChallanService service;

    @BeforeEach
    public void setup() {
        service = new ChallanService();
    }

    @Test
    public void testVehicleRegistration() {

        Vehicle vehicle = new Vehicle(
                "TN01AA1111",
                "Ravi",
                "9000000000",
                VehicleType.CAR
        );

        service.registerVehicle(vehicle);

        assertEquals(
                1,
                service.getVehicleCount()
        );
    }

    @Test
    public void testGenerateChallan() {

        Vehicle vehicle = new Vehicle(
                "TN01AA1111",
                "Ravi",
                "9000000000",
                VehicleType.CAR
        );

        service.registerVehicle(vehicle);

        String challan = service.generateChallan(
                "TN01AA1111",
                ViolationType.OVER_SPEEDING,
                "Vellore",
                "2026-09-10 10:00",
                70,
                50
        );

        assertNotNull(challan);

        assertEquals(
                1,
                service.getChallanCount()
        );
    }

    @Test
    public void testOverSpeedingFine() {

        Vehicle vehicle = new Vehicle(
                "TN01AA1111",
                "Ravi",
                "9000000000",
                VehicleType.CAR
        );

        service.registerVehicle(vehicle);

        String challan = service.generateChallan(
                "TN01AA1111",
                ViolationType.OVER_SPEEDING,
                "Vellore",
                "2026-09-10 10:00",
                75,
                50
        );

        assertNotNull(challan);

        assertEquals(
                1,
                service.getChallanCount()
        );
    }

    @Test
    public void testPayment() {

        Vehicle vehicle = new Vehicle(
                "TN01AA1111",
                "Ravi",
                "9000000000",
                VehicleType.CAR
        );

        service.registerVehicle(vehicle);

        String challan = service.generateChallan(
                "TN01AA1111",
                ViolationType.ILLEGAL_PARKING,
                "Vellore",
                "2026-09-10 10:00",
                0,
                0
        );

        assertEquals(
                PaymentStatus.UNPAID,
                service.getPaymentStatus(challan)
        );

        service.payChallan(challan);

        assertEquals(
                PaymentStatus.PAID,
                service.getPaymentStatus(challan)
        );
    }

    @Test
    public void testOutstandingFine() {

        Vehicle vehicle = new Vehicle(
                "TN01AA1111",
                "Ravi",
                "9000000000",
                VehicleType.CAR
        );

        service.registerVehicle(vehicle);

        service.generateChallan(
                "TN01AA1111",
                ViolationType.ILLEGAL_PARKING,
                "Vellore",
                "2026-09-10 10:00",
                0,
                0
        );

        assertEquals(
                500,
                service.getOutstandingFine("TN01AA1111")
        );
    }

    @Test
    public void testInvalidVehicle() {

        assertThrows(
                InvalidVehicleException.class,
                () -> service.registerVehicle(null)
        );
    }

    @Test
    public void testUnregisteredVehicle() {

        assertThrows(
                InvalidVehicleException.class,
                () -> service.generateChallan(
                        "TN99XX9999",
                        ViolationType.OVER_SPEEDING,
                        "Vellore",
                        "2026-09-10 10:00",
                        70,
                        50
                )
        );
    }

    @Test
    public void testDuplicateChallan() {

        Vehicle vehicle = new Vehicle(
                "TN01AA1111",
                "Ravi",
                "9000000000",
                VehicleType.CAR
        );

        service.registerVehicle(vehicle);

        service.generateChallan(
                "TN01AA1111",
                ViolationType.OVER_SPEEDING,
                "Vellore",
                "2026-09-10 10:00",
                70,
                50
        );

        assertThrows(
                DuplicateChallanException.class,
                () -> service.generateChallan(
                        "TN01AA1111",
                        ViolationType.OVER_SPEEDING,
                        "Vellore",
                        "2026-09-10 10:00",
                        70,
                        50
                )
        );
    }

    @Test
    public void testVehicleClassification() {

        Vehicle vehicle = new Vehicle(
                "TN01AA1111",
                "Ravi",
                "9000000000",
                VehicleType.CAR
        );

        service.registerVehicle(vehicle);

        service.generateChallan(
                "TN01AA1111",
                ViolationType.ILLEGAL_PARKING,
                "Vellore",
                "2026-09-10 10:00",
                0,
                0
        );

        assertEquals(
                VehicleClassification.LOW_RISK,
                service.classifyVehicle("TN01AA1111")
        );
    }
}