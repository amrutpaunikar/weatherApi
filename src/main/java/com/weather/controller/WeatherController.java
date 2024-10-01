package com.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weather.response.WeatherData;
import com.weather.service.WeatherService;

@RestController
@Controller
@RequestMapping("/weather")
public class WeatherController {
	@Autowired
    private WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    
    
    @GetMapping("/{city}")
    public ResponseEntity<WeatherData> getWeather(@PathVariable String city) throws Exception {
        String weatherData = weatherService.getWeather(city);
        System.out.println(weatherData);
        WeatherData WeatherData = weatherService.parseAndSaveWeatherData(weatherData);
        System.out.println(WeatherData);
        System.out.println(WeatherData.getLongitude());
        System.out.println(WeatherData.getLatitude());
        WeatherData.getDailyData().forEach(t->{System.out.println(t.toString());});
        WeatherData.getHourlyData().forEach(t->{System.out.println(t.getDate()+" "+t.getTemperature());});
        return ResponseEntity.ok(WeatherData);
    }
    
    
}
