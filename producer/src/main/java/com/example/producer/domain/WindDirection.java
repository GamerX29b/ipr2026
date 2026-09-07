package com.example.producer.domain;

public enum WindDirection {

    NORTH("Север"),
    NORTH_EAST("Северо-восток"),
    EAST("Восток"),
    SOUTH_EAST("Юго-восток"),
    SOUTH("Юг"),
    SOUTH_WEST("Юго-запад"),
    WEST("Запад"),
    NORTH_WEST("Северо-запад");

    private final String label;

    WindDirection(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static WindDirection fromDegrees(double degrees) {
        double normalized = (degrees % 360 + 360) % 360;
        int index = (int) Math.round(normalized / 45.0) % 8;
        return values()[index];
    }
}
