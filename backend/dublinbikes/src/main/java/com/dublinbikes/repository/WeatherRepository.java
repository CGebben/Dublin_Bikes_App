package com.dublinbikes.repository;

import com.dublinbikes.model.Weather;
import com.dublinbikes.model.WeatherId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, WeatherId> {
}