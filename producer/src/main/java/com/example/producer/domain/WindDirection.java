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
}
