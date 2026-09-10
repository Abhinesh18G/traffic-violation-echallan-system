package com.traffic.model;

public enum ViolationType {

    OVER_SPEEDING(1000),
    SIGNAL_VIOLATION(1500),
    ILLEGAL_PARKING(500);

    private final double baseFine;

    ViolationType(double baseFine) {
        this.baseFine = baseFine;
    }

    public double getBaseFine() {
        return baseFine;
    }
}
