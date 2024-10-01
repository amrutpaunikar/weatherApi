package com.weather.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weather.response.WeatherData;

public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {}
