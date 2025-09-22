package com.campusconnect.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * DTO for event update request
 * 
 * @author Campus Connect Team
 */
public class EventUpdateRequest {

    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    private String description;
    private String longDescription;
    private String category;

    @Future(message = "Event date must be in the future")
    private LocalDate eventDate;

    private LocalTime startTime;
    private LocalTime endTime;

    @Size(max = 255, message = "Location must not exceed 255 characters")
    private String location;

    @Positive(message = "Maximum attendees must be positive")
    private Integer maxAttendees;

    private String requirements;
    private List<String> tags;
    private Boolean isFeatured;
    private LocalDateTime registrationDeadline;

    // Constructors
    public EventUpdateRequest() {}

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getMaxAttendees() {
        return maxAttendees;
    }

    public void setMaxAttendees(Integer maxAttendees) {
        this.maxAttendees = maxAttendees;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public LocalDateTime getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(LocalDateTime registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    @Override
    public String toString() {
        return "EventUpdateRequest{" +
                "title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", eventDate=" + eventDate +
                ", location='" + location + '\'' +
                ", maxAttendees=" + maxAttendees +
                '}';
    }
}