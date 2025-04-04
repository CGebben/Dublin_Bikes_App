package com.dublinbikes.controller;

import com.dublinbikes.model.Availability;
import com.dublinbikes.repository.AvailabilityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}