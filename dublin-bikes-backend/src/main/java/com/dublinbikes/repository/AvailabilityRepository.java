package com.dublinbikes.repository;

import com.dublinbikes.model.Availability;
import com.dublinbikes.model.AvailabilityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repository interface for Availability data.
// Uses composite key (AvailabilityId) for lookups and persistence.
@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, AvailabilityId> {

    // Returns the latest availability record per station (one per station).
    // Uses subquery to select the max timestamp for each station.
    @Query("""
                SELECT a FROM Availability a
                WHERE (a.id.stationId, a.id.scraperInputDateTime) IN (
                    SELECT a2.id.stationId, MAX(a2.id.scraperInputDateTime)
                    FROM Availability a2
                    GROUP BY a2.id.stationId
                )
            """)
    List<Availability> findLatestPerStation();

    // Returns the single most recent availability record in the table.
    // Used for conditional GET comparison.
    Optional<Availability> findTopByOrderById_ScraperInputDateTimeDesc();
}