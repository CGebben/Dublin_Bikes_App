package com.dublinbikes.repository;

import com.dublinbikes.model.Availability;
import com.dublinbikes.model.AvailabilityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, AvailabilityId> {

    @Query("""
                SELECT a FROM Availability a
                WHERE (a.id.stationId, a.id.scraperInputDateTime) IN (
                    SELECT a2.id.stationId, MAX(a2.id.scraperInputDateTime)
                    FROM Availability a2
                    GROUP BY a2.id.stationId
                )
            """)
    List<Availability> findLatestPerStation();
}