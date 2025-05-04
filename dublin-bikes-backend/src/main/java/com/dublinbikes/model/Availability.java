package com.dublinbikes.model;

import jakarta.persistence.*;

// JPA entity for a single availability snapshot.
// Each record is uniquely identified by station ID and scrape timestamp.
@Entity
@Table(name = "availability")
public class Availability {

    @EmbeddedId
    private AvailabilityId id;

    // Relationship to Station entity (foreign key: Station_ID)
    // Also acts as part of the composite key
    @ManyToOne
    @MapsId("stationId")
    @JoinColumn(name = "Station_ID")
    private Station station;

    @Column(name = "Available_bike_stands")
    private int availableBikeStands;

    @Column(name = "Available_bikes")
    private int availableBikes;

    @Column(name = "Status")
    private String status;

    // Getters and setters
    public AvailabilityId getId() {
        return id;
    }

    public void setId(AvailabilityId id) {
        this.id = id;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public int getAvailableBikeStands() {
        return availableBikeStands;
    }

    public void setAvailableBikeStands(int stands) {
        this.availableBikeStands = stands;
    }

    public int getAvailableBikes() {
        return availableBikes;
    }

    public void setAvailableBikes(int bikes) {
        this.availableBikes = bikes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}