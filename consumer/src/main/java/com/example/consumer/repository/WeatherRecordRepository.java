package com.example.consumer.repository;

import com.example.consumer.domain.WeatherRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeatherRecordRepository extends JpaRepository<WeatherRecord, Long> {

    Optional<WeatherRecord> findFirstByOrderByCreatedAtDescIdDesc();
}
