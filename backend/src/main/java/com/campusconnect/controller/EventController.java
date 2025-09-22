package com.campusconnect.controller;

import com.campusconnect.dto.request.EventCreateRequest;
import com.campusconnect.dto.response.EventResponse;
import com.campusconnect.security.UserPrincipal;
import com.campusconnect.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Event Controller
 * Handles event management operations
 * 
 * @author Campus Connect Team
 */
@RestController
@RequestMapping("/api/v1/events")
@Tag(name = "Events", description = "Event management endpoints")
public class EventController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
        logger.info("EventController initialized");
    }

    /**
     * Get all events
     */
    @GetMapping
    @Operation(summary = "Get all events", description = "Get paginated list of all active events")
    public ResponseEntity<Page<EventResponse>> getAllEvents(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "eventDate") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir,
            Authentication authentication) {
        
        logger.debug("Getting all events - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                    page, size, sortBy, sortDir);
        
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Long userId = getUserId(authentication);
            Page<EventResponse> events = eventService.getAllEvents(pageable, userId);
            
            logger.debug("Successfully retrieved {} events", events.getTotalElements());
            return ResponseEntity.ok(events);
            
        } catch (Exception e) {
            logger.error("Error getting all events", e);
            throw e;
        }
    }

    /**
     * Get event by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get event by ID", description = "Get event details by ID")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id, Authentication authentication) {
        logger.debug("Getting event by ID: {}", id);
        
        try {
            Long userId = getUserId(authentication);
            EventResponse event = eventService.getEventById(id, userId);
            return ResponseEntity.ok(event);
            
        } catch (Exception e) {
            logger.error("Error getting event by ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Create new event (Admin only)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create event", description = "Create a new event (Admin only)")
    public ResponseEntity<EventResponse> createEvent(
            @Valid @RequestBody EventCreateRequest request,
            Authentication authentication) {
        
        logger.info("Creating new event: {}", request.getTitle());
        
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            EventResponse event = eventService.createEvent(request, userPrincipal.getId());
            
            logger.info("Successfully created event with ID: {}", event.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(event);
            
        } catch (Exception e) {
            logger.error("Error creating event: {}", request.getTitle(), e);
            throw e;
        }
    }

    /**
     * Update event (Admin or organizer only)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update event", description = "Update event by ID (Admin or organizer only)")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventCreateRequest request,
            Authentication authentication) {
        
        logger.info("Updating event with ID: {}", id);
        
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            EventResponse event = eventService.updateEvent(id, request, userPrincipal.getId());
            
            logger.info("Successfully updated event with ID: {}", id);
            return ResponseEntity.ok(event);
            
        } catch (Exception e) {
            logger.error("Error updating event with ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Delete event (Admin or organizer only)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete event", description = "Delete event by ID (Admin or organizer only)")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id, Authentication authentication) {
        logger.info("Deleting event with ID: {}", id);
        
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            eventService.deleteEvent(id, userPrincipal.getId());
            
            logger.info("Successfully deleted event with ID: {}", id);
            return ResponseEntity.noContent().build();
            
        } catch (Exception e) {
            logger.error("Error deleting event with ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Register for event
     */
    @PostMapping("/{id}/register")
    @Operation(summary = "Register for event", description = "Register current user for event")
    public ResponseEntity<Void> registerForEvent(@PathVariable Long id, Authentication authentication) {
        logger.info("Registering for event with ID: {}", id);
        
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            eventService.registerForEvent(id, userPrincipal.getId());
            
            logger.info("Successfully registered for event with ID: {}", id);
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            logger.error("Error registering for event with ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Unregister from event
     */
    @DeleteMapping("/{id}/register")
    @Operation(summary = "Unregister from event", description = "Unregister current user from event")
    public ResponseEntity<Void> unregisterFromEvent(@PathVariable Long id, Authentication authentication) {
        logger.info("Unregistering from event with ID: {}", id);
        
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            eventService.unregisterFromEvent(id, userPrincipal.getId());
            
            logger.info("Successfully unregistered from event with ID: {}", id);
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            logger.error("Error unregistering from event with ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Get events by category
     */
    @GetMapping("/category/{category}")
    @Operation(summary = "Get events by category", description = "Get events filtered by category")
    public ResponseEntity<Page<EventResponse>> getEventsByCategory(
            @PathVariable String category,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        
        logger.debug("Getting events by category: {}", category);
        
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("eventDate").ascending());
            Long userId = getUserId(authentication);
            Page<EventResponse> events = eventService.getEventsByCategory(category, pageable, userId);
            
            logger.debug("Found {} events in category: {}", events.getTotalElements(), category);
            return ResponseEntity.ok(events);
            
        } catch (Exception e) {
            logger.error("Error getting events by category: {}", category, e);
            throw e;
        }
    }

    /**
     * Get featured events (public endpoint)
     */
    @GetMapping("/featured")
    @Operation(summary = "Get featured events", description = "Get featured events (public endpoint)")
    public ResponseEntity<Page<EventResponse>> getFeaturedEvents(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        
        logger.debug("Getting featured events");
        
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("eventDate").ascending());
            Long userId = getUserId(authentication);
            Page<EventResponse> events = eventService.getFeaturedEvents(pageable, userId);
            
            logger.debug("Found {} featured events", events.getTotalElements());
            return ResponseEntity.ok(events);
            
        } catch (Exception e) {
            logger.error("Error getting featured events", e);
            throw e;
        }
    }

    /**
     * Search events
     */
    @GetMapping("/search")
    @Operation(summary = "Search events", description = "Search events by title or description")
    public ResponseEntity<Page<EventResponse>> searchEvents(
            @Parameter(description = "Search term") @RequestParam String q,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        
        logger.debug("Searching events with query: '{}'", q);
        
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("eventDate").ascending());
            Long userId = getUserId(authentication);
            Page<EventResponse> events = eventService.searchEvents(q, pageable, userId);
            
            logger.debug("Found {} events matching query: '{}'", events.getTotalElements(), q);
            return ResponseEntity.ok(events);
            
        } catch (Exception e) {
            logger.error("Error searching events with query: '{}'", q, e);
            throw e;
        }
    }

    /**
     * Get upcoming events
     */
    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming events", description = "Get upcoming events")
    public ResponseEntity<Page<EventResponse>> getUpcomingEvents(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        
        logger.debug("Getting upcoming events");
        
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("eventDate").ascending());
            Long userId = getUserId(authentication);
            Page<EventResponse> events = eventService.getUpcomingEvents(pageable, userId);
            
            logger.debug("Found {} upcoming events", events.getTotalElements());
            return ResponseEntity.ok(events);
            
        } catch (Exception e) {
            logger.error("Error getting upcoming events", e);
            throw e;
        }
    }

    /**
     * Get my registered events
     */
    @GetMapping("/my-registrations")
    @Operation(summary = "Get my registered events", description = "Get events registered by current user")
    public ResponseEntity<Page<EventResponse>> getMyRegisteredEvents(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        
        logger.debug("Getting registered events for current user");
        
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Pageable pageable = PageRequest.of(page, size, Sort.by("eventDate").ascending());
            Page<EventResponse> events = eventService.getEventsRegisteredByUser(userPrincipal.getId(), pageable);
            
            logger.debug("Found {} registered events for user ID: {}", events.getTotalElements(), userPrincipal.getId());
            return ResponseEntity.ok(events);
            
        } catch (Exception e) {
            logger.error("Error getting registered events for current user", e);
            throw e;
        }
    }

    /**
     * Get event categories
     */
    @GetMapping("/categories")
    @Operation(summary = "Get event categories", description = "Get list of all event categories")
    public ResponseEntity<List<String>> getEventCategories() {
        logger.debug("Getting event categories");
        
        try {
            List<String> categories = eventService.getEventCategories();
            
            logger.debug("Found {} event categories", categories.size());
            return ResponseEntity.ok(categories);
            
        } catch (Exception e) {
            logger.error("Error getting event categories", e);
            throw e;
        }
    }

    /**
     * Helper method to get user ID from authentication
     */
    private Long getUserId(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            return ((UserPrincipal) authentication.getPrincipal()).getId();
        }
        return null;
    }
}