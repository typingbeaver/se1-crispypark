package de.hbrs.se1.team28.crispypark;

public enum DriverType {
    NORMAL ("#343a40"),     // grey dark
    DISABLED ("#0d6efd"),   // blue
    WOMEN ("#dc3545");      // red

    private final String COLOR;

    DriverType(String color) {
        this.COLOR = color;
    }

    public String getColor() {
        return this.COLOR;
    }
}