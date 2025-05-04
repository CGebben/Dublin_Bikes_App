package com.dublinbikes.model;

import jakarta.persistence.*;

// JPA entity for Dublin bike stations.
// Each station is uniquely identified by its stationId.
@Entity
@Table(name = "station")
public class Station {

    @Id
    @Column(name = "Station_ID")
    private int stationId;

    @Column(name = "Contract_name")
    private String contractName;

    @Column(name = "Station_name")
    private String stationName;

    @Column(name = "Station_address")
    private String stationAddress;

    @Column(name = "Position_lat")
    private double latitude;

    @Column(name = "Position_long")
    private double longitude;

    @Column(name = "Banking")
    private int banking;

    @Column(name = "Bonus")
    private int bonus;

    @Column(name = "Bike_stands")
    private int bikeStands;

    @Column(name = "Last_update")
    private long lastUpdate;

    // Getters and setters for all fields
    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationAddress() {
        return stationAddress;
    }

    public void setStationAddress(String stationAddress) {
        this.stationAddress = stationAddress;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getBanking() {
        return banking;
    }

    public void setBanking(int banking) {
        this.banking = banking;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public int getBikeStands() {
        return bikeStands;
    }

    public void setBikeStands(int bikeStands) {
        this.bikeStands = bikeStands;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}