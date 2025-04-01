package com.dublinbikes.repository;

import com.dublinbikes.model.Availability;
import com.dublinbikes.model.AvailabilityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, AvailabilityId> {
}