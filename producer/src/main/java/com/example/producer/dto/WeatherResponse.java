package com.example.producer.dto;

import com.example.producer.domain.WeatherRecord;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WeatherResponse(
        Long id,
        BigDecimal temperature,
        Integer humidity,
        BigDecimal windSpeed,
        String windDirection,
        String windDirectionLabel,
        LocalDateTime createdAt
) {

    public static WeatherResponse from(WeatherRecord record) {
        return new WeatherResponse(
                record.getId(),
                record.getTemperature(),
                record.getHumidity(),
                record.getWindSpeed(),
                record.getWindDirection().name(),
                record.getWindDirection().getLabel(),
                record.getCreatedAt()
        );
    }
}
