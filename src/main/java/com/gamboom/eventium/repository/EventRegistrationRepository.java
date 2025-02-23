package com.gamboom.eventium.repository;

import com.gamboom.eventium.model.EventRegistration;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, UUID> {
//    @EntityGraph(attributePaths = {"user", "event"})  // Fetch related entities eagerly
//    List<EventRegistration> findAll();
}
