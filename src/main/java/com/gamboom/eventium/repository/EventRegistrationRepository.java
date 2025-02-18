package com.gamboom.eventium.repository;

import com.gamboom.eventium.model.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, UUID> {

}
