package com.example.producer.service;

import com.example.producer.domain.WeatherRecord;
import com.example.producer.dto.WeatherRequest;
import com.example.producer.dto.WeatherResponse;
import com.example.producer.event.WeatherEvent;
import com.example.producer.repository.WeatherRecordRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WeatherService {

    private final WeatherRecordRepository repository;
    private final KafkaTemplate<String, WeatherEvent> kafkaTemplate;
    private final String topic;

    public WeatherService(WeatherRecordRepository repository,
                          KafkaTemplate<String, WeatherEvent> kafkaTemplate,
                          @Value("${app.kafka.topic}") String topic) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Transactional
    public WeatherResponse submit(WeatherRequest request) {
        WeatherRecord record = new WeatherRecord();
        record.setTemperature(request.temperature());
        record.setHumidity(request.humidity());
        record.setWindSpeed(request.windSpeed());
        record.setWindDirection(request.windDirection());
        record.setCreatedAt(LocalDateTime.now());
        record = repository.save(record);
        kafkaTemplate.send(topic, String.valueOf(record.getId()), toEvent(record));
        return WeatherResponse.from(record);
    }

    public List<WeatherResponse> history() {
        return repository.findAllByOrderByCreatedAtDescIdDesc().stream()
                .map(WeatherResponse::from)
                .toList();
    }

    private WeatherEvent toEvent(WeatherRecord record) {
        return new WeatherEvent(
                record.getId(),
                record.getTemperature(),
                record.getHumidity(),
                record.getWindSpeed(),
                record.getWindDirection().name(),
                record.getCreatedAt().toString()
        );
    }
}
