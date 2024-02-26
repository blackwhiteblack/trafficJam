package com.deichmann.model;

public enum VehicleType {
    CAR("/OO\\  "),
    BUS("-/OOOO\\  "),
    PICKUP("/O\\__  "),
    TRUCK("/O|___  ");

    private final String symbol;

    VehicleType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}

