package com.campusconnect.repository;

import com.campusconnect.entity.Event;
import com.campusconnect.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Event entity operations
 * 
 * @author Campus Connect Team
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Find event by ID and active status
     * @param id the event ID
     * @param isActive the active status
     * @return Optional containing the event if found
     */
    Optional<Event> findByIdAndIsActive(Long id, Boolean isActive);

    /**
     * Find events by category
     * @param category the event category
     * @param pageable pagination information
     * @return Page of events in the specified category
     */
    Page<Event> findByCategory(String category, Pageable pageable);

    /**
     * Find events by category and active status
     * @param category the event category
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of active events in the specified category
     */
    Page<Event> findByCategoryAndIsActive(String category, Boolean isActive, Pageable pageable);

    /**
     * Find featured events
     * @param isFeatured the featured status
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of featured events
     */
    Page<Event> findByIsFeaturedAndIsActive(Boolean isFeatured, Boolean isActive, Pageable pageable);

    /**
     * Find events by organizer
     * @param organizer the event organizer
     * @param pageable pagination information
     * @return Page of events organized by the specified user
     */
    Page<Event> findByOrganizer(User organizer, Pageable pageable);

    /**
     * Find events by organizer and active status
     * @param organizer the event organizer
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of active events organized by the specified user
     */
    Page<Event> findByOrganizerAndIsActive(User organizer, Boolean isActive, Pageable pageable);

    /**
     * Find events by date range
     * @param startDate the start date
     * @param endDate the end date
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of events within the date range
     */
    Page<Event> findByEventDateBetweenAndIsActive(LocalDate startDate, LocalDate endDate, Boolean isActive, Pageable pageable);

    /**
     * Find upcoming events
     * @param currentDate the current date
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of upcoming events
     */
    Page<Event> findByEventDateGreaterThanEqualAndIsActive(LocalDate currentDate, Boolean isActive, Pageable pageable);

    /**
     * Find past events
     * @param currentDate the current date
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of past events
     */
    Page<Event> findByEventDateLessThanAndIsActive(LocalDate currentDate, Boolean isActive, Pageable pageable);

    /**
     * Search events by title or description
     * @param searchTerm the search term
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of events matching the search criteria
     */
    @Query("SELECT e FROM Event e WHERE " +
           "(LOWER(e.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "e.isActive = :isActive")
    Page<Event> searchEvents(@Param("searchTerm") String searchTerm, @Param("isActive") Boolean isActive, Pageable pageable);

    /**
     * Find events with available spots
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of events with available registration spots
     */
    @Query("SELECT e FROM Event e WHERE e.currentAttendees < e.maxAttendees AND e.isActive = :isActive")
    Page<Event> findEventsWithAvailableSpots(@Param("isActive") Boolean isActive, Pageable pageable);

    /**
     * Find events by tags
     * @param tag the tag to search for
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of events containing the specified tag
     */
    @Query("SELECT e FROM Event e WHERE :tag MEMBER OF e.tags AND e.isActive = :isActive")
    Page<Event> findByTagsContaining(@Param("tag") String tag, @Param("isActive") Boolean isActive, Pageable pageable);

    /**
     * Find events registered by a specific user
     * @param userId the user ID
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of events the user is registered for
     */
    @Query("SELECT e FROM Event e JOIN e.registrations r WHERE r.user.id = :userId AND e.isActive = :isActive")
    Page<Event> findEventsByUserRegistration(@Param("userId") Long userId, @Param("isActive") Boolean isActive, Pageable pageable);

    /**
     * Update event attendee count
     * @param eventId the event ID
     * @param attendeeCount the new attendee count
     */
    @Modifying
    @Query("UPDATE Event e SET e.currentAttendees = :attendeeCount WHERE e.id = :eventId")
    void updateAttendeeCount(@Param("eventId") Long eventId, @Param("attendeeCount") Integer attendeeCount);

    /**
     * Increment event attendee count
     * @param eventId the event ID
     */
    @Modifying
    @Query("UPDATE Event e SET e.currentAttendees = e.currentAttendees + 1 WHERE e.id = :eventId")
    void incrementAttendeeCount(@Param("eventId") Long eventId);

    /**
     * Decrement event attendee count
     * @param eventId the event ID
     */
    @Modifying
    @Query("UPDATE Event e SET e.currentAttendees = e.currentAttendees - 1 WHERE e.id = :eventId AND e.currentAttendees > 0")
    void decrementAttendeeCount(@Param("eventId") Long eventId);

    /**
     * Find most popular events by registration count
     * @param isActive the active status
     * @param pageable pagination information
     * @return List of events ordered by registration count
     */
    @Query("SELECT e FROM Event e WHERE e.isActive = :isActive ORDER BY e.currentAttendees DESC")
    List<Event> findMostPopularEvents(@Param("isActive") Boolean isActive, Pageable pageable);

    /**
     * Find events happening today
     * @param today today's date
     * @param isActive the active status
     * @return List of events happening today
     */
    List<Event> findByEventDateAndIsActive(LocalDate today, Boolean isActive);

    /**
     * Find events happening this week
     * @param startOfWeek the start of the week
     * @param endOfWeek the end of the week
     * @param isActive the active status
     * @return List of events happening this week
     */
    List<Event> findByEventDateBetweenAndIsActiveOrderByEventDateAsc(LocalDate startOfWeek, LocalDate endOfWeek, Boolean isActive);

    /**
     * Count events by category
     * @param category the event category
     * @param isActive the active status
     * @return count of events in the category
     */
    long countByCategoryAndIsActive(String category, Boolean isActive);

    /**
     * Count events by organizer
     * @param organizer the event organizer
     * @param isActive the active status
     * @return count of events organized by the user
     */
    long countByOrganizerAndIsActive(User organizer, Boolean isActive);

    /**
     * Find all distinct categories
     * @param isActive the active status
     * @return List of event categories
     */
    @Query("SELECT DISTINCT e.category FROM Event e WHERE e.isActive = :isActive")
    List<String> findAllCategories(@Param("isActive") Boolean isActive);

    /**
     * Find events with registration deadline approaching
     * @param deadline the deadline threshold
     * @param isActive the active status
     * @return List of events with approaching registration deadline
     */
    List<Event> findByRegistrationDeadlineBetweenAndIsActive(LocalDateTime start, LocalDateTime deadline, Boolean isActive);
}