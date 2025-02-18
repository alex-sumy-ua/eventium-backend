package com.gamboom.eventium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gamboom.eventium.model.Event;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

}
