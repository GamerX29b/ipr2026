package com.example.consumer.web;

import com.example.consumer.dto.WeatherResponse;
import com.example.consumer.repository.WeatherRecordRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherRecordRepository repository;

    public WeatherController(WeatherRecordRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/latest")
    public ResponseEntity<WeatherResponse> latest() {
        return repository.findFirstByOrderByCreatedAtDescIdDesc()
                .map(record -> ResponseEntity.ok(WeatherResponse.from(record)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeatherResponse> byId(@PathVariable Long id) {
        return repository.findById(id)
                .map(record -> ResponseEntity.ok(WeatherResponse.from(record)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
