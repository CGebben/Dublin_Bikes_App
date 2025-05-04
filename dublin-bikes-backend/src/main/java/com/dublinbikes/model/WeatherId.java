package com.dublinbikes.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

// Composite key for Weather entity.
// Combines station name and scrape timestamp.
@Embeddable
public class WeatherId implements Serializable {

    private String stationName;
    private LocalDateTime scraperInputDateTime;

    public WeatherId() {
    }

    public WeatherId(String stationName, LocalDateTime scraperInputDateTime) {
        this.stationName = stationName;
        this.scraperInputDateTime = scraperInputDateTime;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public LocalDateTime getScraperInputDateTime() {
        return scraperInputDateTime;
    }

    public void setScraperInputDateTime(LocalDateTime scraperInputDateTime) {
        this.scraperInputDateTime = scraperInputDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof WeatherId))
            return false;
        WeatherId that = (WeatherId) o;
        return Objects.equals(stationName, that.stationName) &&
                Objects.equals(scraperInputDateTime, that.scraperInputDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationName, scraperInputDateTime);
    }
}