package com.dublinbikes.service;

import com.dublinbikes.model.Station;
import com.dublinbikes.model.Availability;
import com.dublinbikes.model.AvailabilityId;
import com.dublinbikes.repository.StationRepository;
import com.dublinbikes.repository.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;

// Service that fetches station and availability data from JCDecaux API.
// Runs every 5 minutes using a Spring @Scheduled task.
@Service
public class DublinBikesScraper {

    @Value("${jcdecaux.api.key}")
    private String apiKey;

    private final StationRepository stationRepo;
    private final AvailabilityRepository availabilityRepo;

    // Used for making REST API calls
    private final RestTemplate restTemplate = new RestTemplate();

    public DublinBikesScraper(StationRepository stationRepo, AvailabilityRepository availabilityRepo) {
        this.stationRepo = stationRepo;
        this.availabilityRepo = availabilityRepo;
    }

    // Fetches JSON from JCDecaux and saves both Station and Availability records.
    public void fetchAndStoreData() {
        String url = "https://api.jcdecaux.com/vls/v1/stations?contract=dublin&apiKey=" + apiKey;

        try {
            // Request station data from JCDecaux API
            @SuppressWarnings("unchecked")
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.getForEntity(
                    url, (Class<List<Map<String, Object>>>) (Class<?>) List.class);

            List<Map<String, Object>> stations = response.getBody();

            if (stations == null) {
                System.out.println("No data returned from JCDecaux API.");
                return;
            }

            for (Map<String, Object> stationData : stations) {
                int number = (int) stationData.get("number");
                String name = (String) stationData.get("name");
                String address = (String) stationData.get("address");

                // Extract latitude and longitude
                @SuppressWarnings("unchecked")
                Map<String, Double> position = (Map<String, Double>) stationData.get("position");
                double lat = position.get("lat");
                double lng = position.get("lng");

                // Save Station entity only if it doesn't already exist
                if (!stationRepo.existsById(number)) {
                    Station station = new Station();
                    station.setStationId(number);
                    station.setStationName(name);
                    station.setStationAddress(address);
                    station.setLatitude(lat);
                    station.setLongitude(lng);
                    stationRepo.save(station);
                }

                // Lookup existing Station entity (should exist now)
                Station station = stationRepo.findById(number).orElse(null);
                if (station == null)
                    continue;

                // Parse availability data
                int stands = (int) stationData.get("available_bike_stands");
                int bikes = (int) stationData.get("available_bikes");
                String status = (String) stationData.get("status");
                LocalDateTime timestamp = LocalDateTime.now();

                // Create and save Availability record
                Availability availability = new Availability();
                availability.setId(new AvailabilityId(number, timestamp));
                availability.setStation(station);
                availability.setAvailableBikeStands(stands);
                availability.setAvailableBikes(bikes);
                availability.setStatus(status);

                availabilityRepo.save(availability);
            }

            System.out.println("✅ Successfully fetched and saved JCDecaux data.");

        } catch (Exception e) {
            System.err.println("❌ Error fetching JCDecaux data: " + e.getMessage());
        }
    }

    // Runs the scraper every 5 minutes (300,000 ms)
    @Scheduled(fixedRate = 300000)
    public void runScheduledScraper() {
        System.out.println("🔁 Running scheduled scraper...");
        fetchAndStoreData();
    }
}