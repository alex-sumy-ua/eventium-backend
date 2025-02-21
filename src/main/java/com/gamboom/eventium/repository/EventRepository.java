package com.gamboom.eventium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gamboom.eventium.model.Event;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

}
