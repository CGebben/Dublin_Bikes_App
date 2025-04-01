package com.dublinbikes.controller;

import com.dublinbikes.model.Weather;
import com.dublinbikes.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}