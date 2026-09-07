package com.example.producer.dto;

import com.example.producer.domain.WindDirection;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WeatherRequest(

        @NotNull(message = "Поле должно быть заполнено")
        BigDecimal temperature,

        @NotNull(message = "Поле должно быть заполнено")
        @Min(value = 0, message = "Влажность должна быть в диапазоне от 0 до 100")
        @Max(value = 100, message = "Влажность должна быть в диапазоне от 0 до 100")
        Integer humidity,

        @NotNull(message = "Поле должно быть заполнено")
        @DecimalMin(value = "0.0", message = "Скорость ветра не может быть отрицательной")
        BigDecimal windSpeed,

        @NotNull(message = "Поле должно быть заполнено")
        WindDirection windDirection
) {
}
