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

// REST controller for availability-related endpoints.
// Handles retrieval, insertion, and optimized conditional fetching.
@RestController
@RequestMapping("/availability")
public class AvailabilityController {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    // Returns all availability records in the database.
    @GetMapping
    public List<Availability> getAllAvailability() {
        return availabilityRepository.findAll();
    }

    // Saves a new availability record from a JSON request body.
    @PostMapping
    public Availability addAvailability(@RequestBody Availability availability) {
        return availabilityRepository.save(availability);
    }

    // Returns the latest availability per station, with conditional caching
    // support.
    // If the client sends 'If-Modified-Since' and data hasn't changed, respond with
    // 304.
    @GetMapping("/latest")
    public ResponseEntity<List<Availability>> getLatestAvailabilityWithConditional(
            @RequestHeader(value = "If-Modified-Since", required = false) String ifModifiedSince) {

        // Get the most recent availability entry (by timestamp)
        Optional<Availability> latestEntry = availabilityRepository.findTopByOrderById_ScraperInputDateTimeDesc();
        if (latestEntry.isEmpty()) {
            // No data yet — return empty list
            return ResponseEntity.ok(List.of());
        }

        // Format the latest timestamp to UTC
        ZonedDateTime latestTimestamp = latestEntry.get().getId().getScraperInputDateTime().atZone(ZoneOffset.UTC);

        if (ifModifiedSince != null) {
            try {
                // Parse client's cached timestamp
                ZonedDateTime clientTime = ZonedDateTime.parse(ifModifiedSince, DateTimeFormatter.RFC_1123_DATE_TIME);

                // If client already has the latest data, return 304 Not Modified
                if (!latestTimestamp.isAfter(clientTime)) {
                    return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
                }

            } catch (DateTimeParseException e) {
                // Log and continue — fallback to sending full data
                System.err.println("⚠️ Invalid If-Modified-Since header: " + ifModifiedSince);
            }
        }

        // Add Last-Modified header to help future client-side caching
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.LAST_MODIFIED, DateTimeFormatter.RFC_1123_DATE_TIME.format(latestTimestamp));

        // Return full response with fresh data and headers
        return ResponseEntity.ok()
                .headers(headers)
                .body(availabilityRepository.findLatestPerStation());
    }
}