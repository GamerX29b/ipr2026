package com.example.producer.repository;

import com.example.producer.domain.WeatherRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeatherRecordRepository extends JpaRepository<WeatherRecord, Long> {

    List<WeatherRecord> findAllByOrderByCreatedAtDescIdDesc();
}
