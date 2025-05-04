package com.dublinbikes.controller;

import com.dublinbikes.model.Weather;
import com.dublinbikes.repository.WeatherRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

// REST controller for weather-related endpoints.
// Supports retrieving all weather records or the most recent one with conditional caching.
@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherRepository weatherRepository;

    // Returns all weather records from the database.
    @GetMapping
    public List<Weather> getAllWeather() {
        return weatherRepository.findAll();
    }

    // Adds a new weather record manually (used for testing).
    @PostMapping
    public Weather addWeather(@RequestBody Weather weather) {
        return weatherRepository.save(weather);
    }

    // Returns the latest weather record.
    // If client provides 'If-Modified-Since' and no new data exists, responds with
    // 304 Not Modified.
    @GetMapping("/latest")
    public ResponseEntity<Weather> getLatestWeatherWithConditional(
            @RequestHeader(value = "If-Modified-Since", required = false) String ifModifiedSince) {

        // Get most recent weather record
        Weather latest = weatherRepository.findLatestWeather();
        ZonedDateTime latestTimestamp = latest.getId().getScraperInputDateTime().atZone(ZoneOffset.UTC);

        if (ifModifiedSince != null) {
            try {
                // Parse client-side timestamp and compare
                ZonedDateTime clientTime = ZonedDateTime.parse(ifModifiedSince, DateTimeFormatter.RFC_1123_DATE_TIME);
                if (!latestTimestamp.isAfter(clientTime)) {
                    return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
                }
            } catch (DateTimeParseException e) {
                System.err.println("⚠️ Invalid If-Modified-Since header: " + ifModifiedSince);
            }
        }

        // Set Last-Modified header to support frontend caching
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.LAST_MODIFIED, DateTimeFormatter.RFC_1123_DATE_TIME.format(latestTimestamp));

        return ResponseEntity.ok()
                .headers(headers)
                .body(latest);
    }
}