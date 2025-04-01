package com.dublinbikes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "availability")
public class Availability {

    @EmbeddedId
    private AvailabilityId id;

    @ManyToOne
    @MapsId("stationId") // ties this field to the composite key part
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