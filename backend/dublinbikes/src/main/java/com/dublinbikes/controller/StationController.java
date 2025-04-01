package com.dublinbikes.controller;

import com.dublinbikes.model.Station;
import com.dublinbikes.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stations")
public class StationController {

    @Autowired
    private StationRepository stationRepository;

    @GetMapping
    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    // For endpoint testing.
    @PostMapping
    public Station addStation(@RequestBody Station station) {
        return stationRepository.save(station);
    }
}