package com.example.consumer.domain;

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

    public static String labelOf(String name) {
        for (WindDirection direction : values()) {
            if (direction.name().equalsIgnoreCase(name)) {
                return direction.label;
            }
        }
        return name;
    }
}
