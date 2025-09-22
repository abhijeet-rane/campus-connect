package com.campusconnect.service;

import com.campusconnect.dto.request.EventCreateRequest;
import com.campusconnect.dto.response.EventResponse;
import com.campusconnect.entity.Event;
import com.campusconnect.entity.EventRegistration;
import com.campusconnect.entity.User;
import com.campusconnect.exception.BadRequestException;
import com.campusconnect.exception.BusinessLogicException;
import com.campusconnect.exception.ResourceNotFoundException;
import com.campusconnect.repository.EventRegistrationRepository;
import com.campusconnect.repository.EventRepository;
import com.campusconnect.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for Event entity operations
 * 
 * @author Campus Connect Team
 */
@Service
@Transactional
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;
    private final EventRegistrationRepository eventRegistrationRepository;
    private final UserRepository userRepository;

    @Autowired
    public EventService(EventRepository eventRepository,
                       EventRegistrationRepository eventRegistrationRepository,
                       UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.eventRegistrationRepository = eventRegistrationRepository;
        this.userRepository = userRepository;
        logger.info("EventService initialized successfully");
    }

    /**
     * Create a new event
     */
    public EventResponse createEvent(EventCreateRequest request, Long organizerId) {
        logger.info("Creating new event: {} by organizer ID: {}", request.getTitle(), organizerId);
        
        try {
            User organizer = userRepository.findById(organizerId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", organizerId));

            // Validate event times
            if (request.getEndTime().isBefore(request.getStartTime())) {
                throw new BadRequestException("End time must be after start time");
            }

            Event event = new Event();
            event.setTitle(request.getTitle());
            event.setDescription(request.getDescription());
            event.setLongDescription(request.getLongDescription());
            event.setCategory(request.getCategory());
            event.setEventDate(request.getEventDate());
            event.setStartTime(request.getStartTime());
            event.setEndTime(request.getEndTime());
            event.setLocation(request.getLocation());
            event.setMaxAttendees(request.getMaxAttendees());
            event.setOrganizer(organizer);
            event.setRequirements(request.getRequirements());
            event.setTags(request.getTags());
            event.setIsFeatured(request.getIsFeatured());
            event.setRegistrationDeadline(request.getRegistrationDeadline());
            event.setIsActive(true);

            Event savedEvent = eventRepository.save(event);
            logger.info("Successfully created event with ID: {}", savedEvent.getId());
            
            return convertToEventResponse(savedEvent, null);
            
        } catch (Exception e) {
            logger.error("Error creating event: {}", request.getTitle(), e);
            throw e;
        }
    }

    /**
     * Get event by ID
     */
    @Transactional(readOnly = true)
    public EventResponse getEventById(Long eventId, Long userId) {
        logger.debug("Fetching event with ID: {}", eventId);
        
        try {
            Event event = eventRepository.findByIdAndIsActive(eventId, true)
                    .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));
            
            return convertToEventResponse(event, userId);
            
        } catch (Exception e) {
            logger.error("Error fetching event with ID: {}", eventId, e);
            throw e;
        }
    }

    /**
     * Get all events with pagination
     */
    @Transactional(readOnly = true)
    public Page<EventResponse> getAllEvents(Pageable pageable, Long userId) {
        logger.debug("Fetching all events with pagination");
        
        try {
            Page<Event> events = eventRepository.findByIsActive(true, pageable);
            return events.map(event -> convertToEventResponse(event, userId));
            
        } catch (Exception e) {
            logger.error("Error fetching all events", e);
            throw e;
        }
    }

    /**
     * Get events by category
     */
    @Transactional(readOnly = true)
    public Page<EventResponse> getEventsByCategory(String category, Pageable pageable, Long userId) {
        logger.debug("Fetching events by category: {}", category);
        
        try {
            Page<Event> events = eventRepository.findByCategoryAndIsActive(category, true, pageable);
            return events.map(event -> convertToEventResponse(event, userId));
            
        } catch (Exception e) {
            logger.error("Error fetching events by category: {}", category, e);
            throw e;
        }
    }

    /**
     * Get featured events
     */
    @Transactional(readOnly = true)
    public Page<EventResponse> getFeaturedEvents(Pageable pageable, Long userId) {
        logger.debug("Fetching featured events");
        
        try {
            Page<Event> events = eventRepository.findByIsFeaturedAndIsActive(true, true, pageable);
            return events.map(event -> convertToEventResponse(event, userId));
            
        } catch (Exception e) {
            logger.error("Error fetching featured events", e);
            throw e;
        }
    }

    /**
     * Search events
     */
    @Transactional(readOnly = true)
    public Page<EventResponse> searchEvents(String searchTerm, Pageable pageable, Long userId) {
        logger.debug("Searching events with term: '{}'", searchTerm);
        
        try {
            Page<Event> events = eventRepository.searchEvents(searchTerm, true, pageable);
            return events.map(event -> convertToEventResponse(event, userId));
            
        } catch (Exception e) {
            logger.error("Error searching events with term: '{}'", searchTerm, e);
            throw e;
        }
    }

    /**
     * Register user for event
     */
    public void registerForEvent(Long eventId, Long userId) {
        logger.info("Registering user ID: {} for event ID: {}", userId, eventId);
        
        try {
            Event event = eventRepository.findByIdAndIsActive(eventId, true)
                    .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));
            
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

            // Check if already registered
            if (eventRegistrationRepository.existsByUserAndEvent(user, event)) {
                throw new BusinessLogicException("User is already registered for this event");
            }

            // Check if event is full
            if (event.isFull()) {
                throw new BusinessLogicException("Event is full, no more registrations allowed");
            }

            // Check registration deadline
            if (!event.isRegistrationOpen()) {
                throw new BusinessLogicException("Registration deadline has passed");
            }

            // Check if event is in the past
            if (event.isPastEvent()) {
                throw new BusinessLogicException("Cannot register for past events");
            }

            EventRegistration registration = new EventRegistration(user, event);
            eventRegistrationRepository.save(registration);
            
            // Update attendee count
            eventRepository.incrementAttendeeCount(eventId);
            
            logger.info("Successfully registered user ID: {} for event ID: {}", userId, eventId);
            
        } catch (Exception e) {
            logger.error("Error registering user ID: {} for event ID: {}", userId, eventId, e);
            throw e;
        }
    }

    /**
     * Unregister user from event
     */
    public void unregisterFromEvent(Long eventId, Long userId) {
        logger.info("Unregistering user ID: {} from event ID: {}", userId, eventId);
        
        try {
            Event event = eventRepository.findByIdAndIsActive(eventId, true)
                    .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));
            
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

            EventRegistration registration = eventRegistrationRepository.findByUserAndEvent(user, event)
                    .orElseThrow(() -> new BusinessLogicException("User is not registered for this event"));

            eventRegistrationRepository.delete(registration);
            
            // Update attendee count
            eventRepository.decrementAttendeeCount(eventId);
            
            logger.info("Successfully unregistered user ID: {} from event ID: {}", userId, eventId);
            
        } catch (Exception e) {
            logger.error("Error unregistering user ID: {} from event ID: {}", userId, eventId, e);
            throw e;
        }
    }

    /**
     * Get events registered by user
     */
    @Transactional(readOnly = true)
    public Page<EventResponse> getEventsRegisteredByUser(Long userId, Pageable pageable) {
        logger.debug("Fetching events registered by user ID: {}", userId);
        
        try {
            Page<Event> events = eventRepository.findEventsByUserRegistration(userId, true, pageable);
            return events.map(event -> convertToEventResponse(event, userId));
            
        } catch (Exception e) {
            logger.error("Error fetching events registered by user ID: {}", userId, e);
            throw e;
        }
    }

    /**
     * Update event
     */
    public EventResponse updateEvent(Long eventId, EventCreateRequest request, Long organizerId) {
        logger.info("Updating event ID: {} by organizer ID: {}", eventId, organizerId);
        
        try {
            Event event = eventRepository.findByIdAndIsActive(eventId, true)
                    .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

            // Check if user is the organizer or admin
            if (!event.getOrganizer().getId().equals(organizerId)) {
                User user = userRepository.findById(organizerId)
                        .orElseThrow(() -> new ResourceNotFoundException("User", "id", organizerId));
                if (!user.isAdmin()) {
                    throw new BusinessLogicException("Only the organizer or admin can update this event");
                }
            }

            // Update event fields
            if (request.getTitle() != null) event.setTitle(request.getTitle());
            if (request.getDescription() != null) event.setDescription(request.getDescription());
            if (request.getLongDescription() != null) event.setLongDescription(request.getLongDescription());
            if (request.getCategory() != null) event.setCategory(request.getCategory());
            if (request.getEventDate() != null) event.setEventDate(request.getEventDate());
            if (request.getStartTime() != null) event.setStartTime(request.getStartTime());
            if (request.getEndTime() != null) event.setEndTime(request.getEndTime());
            if (request.getLocation() != null) event.setLocation(request.getLocation());
            if (request.getMaxAttendees() != null) event.setMaxAttendees(request.getMaxAttendees());
            if (request.getRequirements() != null) event.setRequirements(request.getRequirements());
            if (request.getTags() != null) event.setTags(request.getTags());
            if (request.getIsFeatured() != null) event.setIsFeatured(request.getIsFeatured());
            if (request.getRegistrationDeadline() != null) event.setRegistrationDeadline(request.getRegistrationDeadline());

            Event updatedEvent = eventRepository.save(event);
            logger.info("Successfully updated event ID: {}", eventId);
            
            return convertToEventResponse(updatedEvent, organizerId);
            
        } catch (Exception e) {
            logger.error("Error updating event ID: {}", eventId, e);
            throw e;
        }
    }

    /**
     * Delete event
     */
    public void deleteEvent(Long eventId, Long organizerId) {
        logger.info("Deleting event ID: {} by organizer ID: {}", eventId, organizerId);
        
        try {
            Event event = eventRepository.findByIdAndIsActive(eventId, true)
                    .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

            // Check if user is the organizer or admin
            if (!event.getOrganizer().getId().equals(organizerId)) {
                User user = userRepository.findById(organizerId)
                        .orElseThrow(() -> new ResourceNotFoundException("User", "id", organizerId));
                if (!user.isAdmin()) {
                    throw new BusinessLogicException("Only the organizer or admin can delete this event");
                }
            }

            event.setIsActive(false);
            eventRepository.save(event);
            
            logger.info("Successfully deleted event ID: {}", eventId);
            
        } catch (Exception e) {
            logger.error("Error deleting event ID: {}", eventId, e);
            throw e;
        }
    }

    /**
     * Get upcoming events
     */
    @Transactional(readOnly = true)
    public Page<EventResponse> getUpcomingEvents(Pageable pageable, Long userId) {
        logger.debug("Fetching upcoming events");
        
        try {
            Page<Event> events = eventRepository.findByEventDateGreaterThanEqualAndIsActive(
                    LocalDate.now(), true, pageable);
            return events.map(event -> convertToEventResponse(event, userId));
            
        } catch (Exception e) {
            logger.error("Error fetching upcoming events", e);
            throw e;
        }
    }

    /**
     * Get event categories
     */
    @Transactional(readOnly = true)
    public List<String> getEventCategories() {
        logger.debug("Fetching event categories");
        
        try {
            return eventRepository.findAllCategories(true);
        } catch (Exception e) {
            logger.error("Error fetching event categories", e);
            throw e;
        }
    }

    /**
     * Convert Event entity to EventResponse DTO
     */
    private EventResponse convertToEventResponse(Event event, Long userId) {
        EventResponse response = new EventResponse();
        response.setId(event.getId());
        response.setTitle(event.getTitle());
        response.setDescription(event.getDescription());
        response.setLongDescription(event.getLongDescription());
        response.setCategory(event.getCategory());
        response.setEventDate(event.getEventDate());
        response.setStartTime(event.getStartTime());
        response.setEndTime(event.getEndTime());
        response.setLocation(event.getLocation());
        response.setMaxAttendees(event.getMaxAttendees());
        response.setCurrentAttendees(event.getCurrentAttendees());
        response.setRequirements(event.getRequirements());
        response.setTags(event.getTags());
        response.setIsFeatured(event.getIsFeatured());
        response.setIsActive(event.getIsActive());
        response.setRegistrationDeadline(event.getRegistrationDeadline());
        response.setCreatedAt(event.getCreatedAt());
        response.setUpdatedAt(event.getUpdatedAt());

        // Set organizer info
        User organizer = event.getOrganizer();
        response.setOrganizer(new EventResponse.OrganizerInfo(
                organizer.getId(),
                organizer.getFullName(),
                organizer.getEmail()
        ));

        // Set computed fields
        response.setAvailableSpots(event.getAvailableSpots());
        response.setIsRegistrationOpen(event.isRegistrationOpen());
        response.setIsPastEvent(event.isPastEvent());

        // Set registration status for current user
        if (userId != null) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                response.setIsRegistered(eventRegistrationRepository.existsByUserAndEvent(user, event));
            }
        }

        return response;
    }
}