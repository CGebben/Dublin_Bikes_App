package com.dublinbikes.controller;

import com.dublinbikes.model.Station;
import com.dublinbikes.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST controller for station-related endpoints.
// Exposes GET and POST endpoints for station data.
@RestController
@RequestMapping("/stations")
public class StationController {

    @Autowired
    private StationRepository stationRepository;

    // Returns all stations in the database.
    @GetMapping
    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    // Adds a station manually (used for testing or seeding).
    @PostMapping
    public Station addStation(@RequestBody Station station) {
        return stationRepository.save(station);
    }
}