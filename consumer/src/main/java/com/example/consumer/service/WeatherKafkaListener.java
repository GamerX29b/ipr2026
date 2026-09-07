package com.example.consumer.service;

import com.example.consumer.domain.WeatherRecord;
import com.example.consumer.event.WeatherEvent;
import com.example.consumer.repository.WeatherRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class WeatherKafkaListener {

    private static final Logger log = LoggerFactory.getLogger(WeatherKafkaListener.class);

    private final WeatherRecordRepository repository;

    public WeatherKafkaListener(WeatherRecordRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void onWeatherEvent(WeatherEvent event) {
        WeatherRecord record = new WeatherRecord();
        record.setTemperature(event.temperature());
        record.setHumidity(event.humidity());
        record.setWindSpeed(event.windSpeed());
        record.setWindDirection(event.windDirection());
        record.setCreatedAt(LocalDateTime.parse(event.createdAt()));
        repository.save(record);
        log.info("Сохранена погодная запись id={}, температура={}, влажность={}",
                record.getId(), record.getTemperature(), record.getHumidity());
    }
}
