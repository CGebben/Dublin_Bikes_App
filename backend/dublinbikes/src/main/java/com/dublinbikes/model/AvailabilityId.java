package com.dublinbikes.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class AvailabilityId implements Serializable {

    private int stationId;
    private LocalDateTime scraperInputDateTime;

    public AvailabilityId() {
    }

    public AvailabilityId(int stationId, LocalDateTime scraperInputDateTime) {
        this.stationId = stationId;
        this.scraperInputDateTime = scraperInputDateTime;
    }

    // Getters and setters
    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public LocalDateTime getScraperInputDateTime() {
        return scraperInputDateTime;
    }

    public void setScraperInputDateTime(LocalDateTime scraperInputDateTime) {
        this.scraperInputDateTime = scraperInputDateTime;
    }

    // Required for JPA composite keys
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AvailabilityId))
            return false;
        AvailabilityId that = (AvailabilityId) o;
        return stationId == that.stationId &&
                Objects.equals(scraperInputDateTime, that.scraperInputDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationId, scraperInputDateTime);
    }
}