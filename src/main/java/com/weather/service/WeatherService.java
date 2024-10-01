package com.weather.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.repo.WeatherDataRepository;
import com.weather.response.CurrentWeather;
import com.weather.response.DailyWeather;
import com.weather.response.HourlyWeather;
import com.weather.response.Precipitation;
import com.weather.response.WeatherData;
import com.weather.response.Wind;

@Service
public class WeatherService {

	@Autowired
    private WeatherDataRepository weatherDataRepository;

	
    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;


    public WeatherService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getWeather(String city) {
        String url = String.format("%s%s&sections=all&timezone=UTC&language=en&units=metric&key=%s",apiUrl,city, apiKey);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String responseBody = response.getBody();
       return responseBody;
    }

    public WeatherData parseAndSaveWeatherData(String jsonResponse) throws Exception {
        JsonNode root = objectMapper.readTree(jsonResponse);

        WeatherData weatherData = new WeatherData();
        weatherData.setLatitude(root.path("lat").asText());
        weatherData.setLongitude(root.path("lon").asText());
        weatherData.setElevation(root.path("elevation").asInt());
        weatherData.setTimezone(root.path("timezone").asText());
        weatherData.setUnits(root.path("units").asText());

        
        JsonNode current = root.path("current");
        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setIcon(current.path("icon").asText());
        currentWeather.setIcon_num(current.path("icon_num").asText());
        currentWeather.setSummary(current.path("summary").asText());
        currentWeather.setTemperature(current.path("temperature").asDouble());
        
        
        JsonNode windNode = current.path("wind");
        Wind wind = new Wind();
        wind.setSpeed(windNode.path("speed").asDouble());
        wind.setAngle(windNode.path("angle").asInt());
        wind.setDirection(windNode.path("dir").asText());
        currentWeather.setWind(wind);

        
        JsonNode precipitationNode = current.path("precipitation");
        Precipitation precipitation = new Precipitation();
        precipitation.setTotal(precipitationNode.path("total").asDouble());
        precipitation.setType(precipitationNode.path("type").asText());
        currentWeather.setPrecipitation(precipitation);

        currentWeather.setCloudCover(current.path("cloud_cover").asInt());
        weatherData.setCurrent(currentWeather);

        
        JsonNode hourlyDataNode = root.path("hourly").path("data");
        List<HourlyWeather> hourlyWeatherList = new ArrayList<>();
        for (JsonNode hourlyNode : hourlyDataNode) {
            HourlyWeather hourlyWeather = new HourlyWeather();
            hourlyWeather.setDate(hourlyNode.path("date").asText());
            hourlyWeather.setWeather(hourlyNode.path("weather").asText());
            hourlyWeather.setSummary(hourlyNode.path("summary").asText());
            hourlyWeather.setTemperature(hourlyNode.path("temperature").asDouble());

            
            Wind hourlyWind = new Wind();
            hourlyWind.setSpeed(hourlyNode.path("wind").path("speed").asDouble());
            hourlyWind.setAngle(hourlyNode.path("wind").path("angle").asInt());
            hourlyWind.setDirection(hourlyNode.path("wind").path("dir").asText());
            hourlyWeather.setWind(hourlyWind);

            Precipitation hourlyPrecipitation = new Precipitation();
            hourlyPrecipitation.setTotal(hourlyNode.path("precipitation").path("total").asDouble());
            hourlyPrecipitation.setType(hourlyNode.path("precipitation").path("type").asText());
            hourlyWeather.setPrecipitation(hourlyPrecipitation);

            hourlyWeather.setCloudCover(hourlyNode.path("cloud_cover").path("total").asInt());
            hourlyWeatherList.add(hourlyWeather);
        }
        weatherData.setHourlyData(hourlyWeatherList);

        
        JsonNode dailyDataNode = root.path("daily").path("data");
        List<DailyWeather> dailyWeatherList = new ArrayList<>();
        for (JsonNode dailyNode : dailyDataNode) {
            DailyWeather dailyWeather = new DailyWeather();
            dailyWeather.setDay(dailyNode.path("day").asText());
            dailyWeather.setSummary(dailyNode.path("summary").asText());
            dailyWeather.setIcon(dailyNode.path("icon").asText());
            dailyWeather.setWeather(dailyNode.path("weather").asText());
            dailyWeather.setTemperatureMin(dailyNode.path("all_day").path("temperature_min").asDouble());
            dailyWeather.setTemperatureMax(dailyNode.path("all_day").path("temperature_max").asDouble());

            
            Wind dailyWind = new Wind();
            dailyWind.setSpeed(dailyNode.path("all_day").path("wind").path("speed").asDouble());
            dailyWind.setAngle(dailyNode.path("all_day").path("wind").path("angle").asInt());
            dailyWind.setDirection(dailyNode.path("all_day").path("wind").path("dir").asText());
            dailyWeather.setWind(dailyWind);

            Precipitation dailyPrecipitation = new Precipitation();
            dailyPrecipitation.setTotal(dailyNode.path("all_day").path("precipitation").path("total").asDouble());
            dailyPrecipitation.setType(dailyNode.path("all_day").path("precipitation").path("type").asText());
            dailyWeather.setPrecipitation(dailyPrecipitation);

            dailyWeather.setCloudCover(dailyNode.path("all_day").path("cloud_cover").path("total").asInt());
            dailyWeatherList.add(dailyWeather);
        }
        weatherData.setDailyData(dailyWeatherList);

        
        return weatherDataRepository.save(weatherData);
    }
}

