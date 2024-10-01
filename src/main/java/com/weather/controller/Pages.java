package com.weather.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.weather.response.CurrentWeather;
import com.weather.response.DailyWeather;
import com.weather.response.WeatherData;
import com.weather.service.WeatherService;

@Controller
public class Pages {
	
	@Autowired
	private WeatherService weatherService;
	
	@GetMapping("/location")
	public String lot() {
		return "location";
	}
	
	@GetMapping("/forntend")
	public String k() {
		return "forntend";
	}

	@PostMapping("/forntend")
    public String frontend(@RequestParam("city") String city, Model model) throws Exception {
		String weatherData = weatherService.getWeather(city);
		
		WeatherData WeatherData = weatherService.parseAndSaveWeatherData(weatherData);
		CurrentWeather current = WeatherData.getCurrent();
		List<DailyWeather> dailyData = WeatherData.getDailyData();
		

	    if (current == null) {
	        // Handle null case (optional, you could also return an error view)
	        current = new CurrentWeather();
	        current.setIcon_num("not");
	        current.setSummary("No data available");
	        current.setTemperature(0.0);
	    }else{
	    	model.addAttribute("city", city);
	        model.addAttribute("current", current);
	        model.addAttribute("dailyData", dailyData);
			
	    }
		
		model.addAttribute("city", city);
        model.addAttribute("current", current);
        model.addAttribute("dailyData", dailyData);
		
    	return "forntend";
    }
}
