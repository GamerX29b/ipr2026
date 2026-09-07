package com.example.consumer.event;

import java.math.BigDecimal;

public record WeatherEvent(
        Long id,
        BigDecimal temperature,
        Integer humidity,
        BigDecimal windSpeed,
        String windDirection,
        String createdAt
) {
}
