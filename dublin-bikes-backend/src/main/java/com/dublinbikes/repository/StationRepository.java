package com.dublinbikes.repository;

import com.dublinbikes.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// JPA repository for Station entities.
// Uses Integer as the ID type (stationId).
@Repository
public interface StationRepository extends JpaRepository<Station, Integer> {
}