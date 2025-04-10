package com.dublinbikes.controller;

import com.dublinbikes.model.Availability;
import com.dublinbikes.repository.AvailabilityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/availability")
public class AvailabilityController {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @GetMapping
    public List<Availability> getAllAvailability() {
        return availabilityRepository.findAll();
    }

    // For endpoint testing.
    @PostMapping
    public Availability addAvailability(@RequestBody Availability availability) {
        return availabilityRepository.save(availability);
    }

    @GetMapping("/latest")
    public List<Availability> getLatestAvailabilityPerStation() {
        return availabilityRepository.findLatestPerStation();
    }

    @GetMapping("/latest-timestamp")
    public Map<String, String> getLatestScrapeTimestamp() {
        return availabilityRepository.findTopByOrderById_ScraperInputDateTimeDesc()
                .map(a -> Map.of("timestamp", a.getId().getScraperInputDateTime().toString()))
                .orElse(Map.of("timestamp", ""));
    }
}