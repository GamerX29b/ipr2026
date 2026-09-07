package com.example.consumer.dto;

import com.example.consumer.domain.WeatherRecord;
import com.example.consumer.domain.WindDirection;

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
                record.getWindDirection(),
                WindDirection.labelOf(record.getWindDirection()),
                record.getCreatedAt()
        );
    }
}
