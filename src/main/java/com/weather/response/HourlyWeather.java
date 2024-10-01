package com.weather.response;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity @Getter @Setter
@Table(name = "hourly_weather")
public class HourlyWeather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;
    private String weather;
    private String summary;
    private double temperature;

    @OneToOne(cascade = CascadeType.ALL)
    private Wind wind;

    private int cloudCover;

    @OneToOne(cascade = CascadeType.ALL)
    private Precipitation precipitation;

}
