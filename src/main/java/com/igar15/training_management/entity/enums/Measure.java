package com.igar15.training_management.entity.enums;

public enum Measure {

    TIMES("times"),
    SECONDS("seconds"),
    MINUTES("minutes"),
    KILOMETERS("kilometers"),
    METERS("meters");

    private String measure;

    private Measure(String measure) {
        this.measure = measure;
    }

    public String getMeasure() {
        return measure;
    }
}
