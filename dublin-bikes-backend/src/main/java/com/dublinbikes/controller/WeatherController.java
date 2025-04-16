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

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherRepository weatherRepository;

    @GetMapping
    public List<Weather> getAllWeather() {
        return weatherRepository.findAll();
    }

    @PostMapping
    public Weather addWeather(@RequestBody Weather weather) {
        return weatherRepository.save(weather);
    }

    @GetMapping("/latest")
    public ResponseEntity<Weather> getLatestWeatherWithConditional(
            @RequestHeader(value = "If-Modified-Since", required = false) String ifModifiedSince) {

        Weather latest = weatherRepository.findLatestWeather();
        ZonedDateTime latestTimestamp = latest.getId().getScraperInputDateTime().atZone(ZoneOffset.UTC);

        if (ifModifiedSince != null) {
            try {
                ZonedDateTime clientTime = ZonedDateTime.parse(ifModifiedSince, DateTimeFormatter.RFC_1123_DATE_TIME);
                if (!latestTimestamp.isAfter(clientTime)) {
                    return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
                }
            } catch (DateTimeParseException e) {
                System.err.println("⚠️ Invalid If-Modified-Since header: " + ifModifiedSince);
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.LAST_MODIFIED, DateTimeFormatter.RFC_1123_DATE_TIME.format(latestTimestamp));

        return ResponseEntity.ok()
                .headers(headers)
                .body(latest);
    }
}