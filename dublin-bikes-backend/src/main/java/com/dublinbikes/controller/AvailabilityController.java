package com.dublinbikes.controller;

import com.dublinbikes.model.Availability;
import com.dublinbikes.repository.AvailabilityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/availability")
public class AvailabilityController {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @GetMapping
    public List<Availability> getAllAvailability() {
        return availabilityRepository.findAll();
    }

    @PostMapping
    public Availability addAvailability(@RequestBody Availability availability) {
        return availabilityRepository.save(availability);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<Availability>> getLatestAvailabilityWithConditional(
            @RequestHeader(value = "If-Modified-Since", required = false) String ifModifiedSince) {

        Optional<Availability> latestEntry = availabilityRepository.findTopByOrderById_ScraperInputDateTimeDesc();
        if (latestEntry.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        ZonedDateTime latestTimestamp = latestEntry.get().getId().getScraperInputDateTime().atZone(ZoneOffset.UTC);

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
                .body(availabilityRepository.findLatestPerStation());
    }
}