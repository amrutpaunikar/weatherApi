package com.weather.controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocationController {
	

    @PostMapping("/save-location")
    public String saveLocation(@RequestBody Location location) {
        System.out.println("User's Location - Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
        return "Location saved successfully!";
    }

    public static class Location {
        private double latitude;
        private double longitude;

        // Getters and Setters
        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }
}

