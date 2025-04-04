package com.dublinbikes.repository;

import com.dublinbikes.model.Weather;
import com.dublinbikes.model.WeatherId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, WeatherId> {

    @Query("SELECT w FROM Weather w WHERE w.id.scraperInputDateTime = (SELECT MAX(w2.id.scraperInputDateTime) FROM Weather w2)")
    Weather findLatestWeather();
}