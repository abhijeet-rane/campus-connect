package com.campusconnect.repository;

import com.campusconnect.entity.Event;
import com.campusconnect.entity.EventRegistration;
import com.campusconnect.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for EventRegistration entity operations
 * 
 * @author Campus Connect Team
 */
@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {

    /**
     * Find registration by user and event
     * @param user the user
     * @param event the event
     * @return Optional containing the registration if found
     */
    Optional<EventRegistration> findByUserAndEvent(User user, Event event);

    /**
     * Check if user is registered for event
     * @param user the user
     * @param event the event
     * @return true if user is registered, false otherwise
     */
    boolean existsByUserAndEvent(User user, Event event);

    /**
     * Find registrations by user
     * @param user the user
     * @param pageable pagination information
     * @return Page of registrations for the user
     */
    Page<EventRegistration> findByUser(User user, Pageable pageable);

    /**
     * Find registrations by event
     * @param event the event
     * @param pageable pagination information
     * @return Page of registrations for the event
     */
    Page<EventRegistration> findByEvent(Event event, Pageable pageable);

    /**
     * Count registrations for an event
     * @param event the event
     * @return count of registrations
     */
    long countByEvent(Event event);

    /**
     * Count registrations by user
     * @param user the user
     * @return count of registrations by the user
     */
    long countByUser(User user);

    /**
     * Find all registrations for events organized by a user
     * @param organizerId the organizer's user ID
     * @param pageable pagination information
     * @return Page of registrations for events organized by the user
     */
    @Query("SELECT er FROM EventRegistration er WHERE er.event.organizer.id = :organizerId")
    Page<EventRegistration> findRegistrationsForOrganizerEvents(@Param("organizerId") Long organizerId, Pageable pageable);

    /**
     * Find users registered for a specific event
     * @param eventId the event ID
     * @return List of users registered for the event
     */
    @Query("SELECT er.user FROM EventRegistration er WHERE er.event.id = :eventId")
    List<User> findUsersRegisteredForEvent(@Param("eventId") Long eventId);

    /**
     * Find events registered by a specific user
     * @param userId the user ID
     * @return List of events the user is registered for
     */
    @Query("SELECT er.event FROM EventRegistration er WHERE er.user.id = :userId")
    List<Event> findEventsRegisteredByUser(@Param("userId") Long userId);

    /**
     * Delete registration by user and event
     * @param user the user
     * @param event the event
     */
    void deleteByUserAndEvent(User user, Event event);
}