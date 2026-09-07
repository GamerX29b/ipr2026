package com.example.producer.web;

import com.example.producer.dto.WeatherRequest;
import com.example.producer.dto.WeatherResponse;
import com.example.producer.service.WeatherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService service;

    public WeatherController(WeatherService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WeatherResponse submit(@Valid @RequestBody WeatherRequest request) {
        return service.submit(request);
    }

    @GetMapping("/history")
    public List<WeatherResponse> history() {
        return service.history();
    }
}
