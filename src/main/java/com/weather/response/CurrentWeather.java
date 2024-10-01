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

@Entity
@Table(name = "current_weather")
@Getter @Setter
public class CurrentWeather{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String icon;
    private String icon_num;
    private String summary;
    private double temperature;

    @OneToOne(cascade = CascadeType.ALL)
    private Wind wind;

    @OneToOne(cascade = CascadeType.ALL)
    private Precipitation precipitation;

    private int cloudCover;

}
