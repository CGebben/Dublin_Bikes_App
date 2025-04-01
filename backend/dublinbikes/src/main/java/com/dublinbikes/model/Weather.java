package com.dublinbikes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "weather")
public class Weather {

    @EmbeddedId
    private WeatherId id;

    private double lon;
    private double lat;

    @Column(name = "weather_id")
    private short weatherId;

    @Column(name = "weather_main")
    private String weatherMain;

    @Column(name = "weather_desc")
    private String weatherDesc;

    @Column(name = "main_temp")
    private float mainTemp;

    private float feelsLike;
    private float tempMin;
    private float tempMax;

    private short pressure;
    private short humidity;
    private short visibility;

    private float windSpeed;
    private short windDeg;

    private short clouds;
    private long dt;

    private short sysType;
    private short sysId;
    private String sysCountry;
    private int sysSunrise;
    private int sysSunset;
    private int timezone;

    private String cod;

    // Getters and setters
    public WeatherId getId() {
        return id;
    }

    public void setId(WeatherId id) {
        this.id = id;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public short getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(short weatherId) {
        this.weatherId = weatherId;
    }

    public String getWeatherMain() {
        return weatherMain;
    }

    public void setWeatherMain(String weatherMain) {
        this.weatherMain = weatherMain;
    }

    public String getWeatherDesc() {
        return weatherDesc;
    }

    public void setWeatherDesc(String weatherDesc) {
        this.weatherDesc = weatherDesc;
    }

    public float getMainTemp() {
        return mainTemp;
    }

    public void setMainTemp(float mainTemp) {
        this.mainTemp = mainTemp;
    }

    public float getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(float feelsLike) {
        this.feelsLike = feelsLike;
    }

    public float getTempMin() {
        return tempMin;
    }

    public void setTempMin(float tempMin) {
        this.tempMin = tempMin;
    }

    public float getTempMax() {
        return tempMax;
    }

    public void setTempMax(float tempMax) {
        this.tempMax = tempMax;
    }

    public short getPressure() {
        return pressure;
    }

    public void setPressure(short pressure) {
        this.pressure = pressure;
    }

    public short getHumidity() {
        return humidity;
    }

    public void setHumidity(short humidity) {
        this.humidity = humidity;
    }

    public short getVisibility() {
        return visibility;
    }

    public void setVisibility(short visibility) {
        this.visibility = visibility;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public short getWindDeg() {
        return windDeg;
    }

    public void setWindDeg(short windDeg) {
        this.windDeg = windDeg;
    }

    public short getClouds() {
        return clouds;
    }

    public void setClouds(short clouds) {
        this.clouds = clouds;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public short getSysType() {
        return sysType;
    }

    public void setSysType(short sysType) {
        this.sysType = sysType;
    }

    public short getSysId() {
        return sysId;
    }

    public void setSysId(short sysId) {
        this.sysId = sysId;
    }

    public String getSysCountry() {
        return sysCountry;
    }

    public void setSysCountry(String sysCountry) {
        this.sysCountry = sysCountry;
    }

    public int getSysSunrise() {
        return sysSunrise;
    }

    public void setSysSunrise(int sysSunrise) {
        this.sysSunrise = sysSunrise;
    }

    public int getSysSunset() {
        return sysSunset;
    }

    public void setSysSunset(int sysSunset) {
        this.sysSunset = sysSunset;
    }

    public int getTimezone() {
        return timezone;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }
}