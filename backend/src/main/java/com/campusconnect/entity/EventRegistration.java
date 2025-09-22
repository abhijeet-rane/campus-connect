package com.campusconnect.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * EventRegistration entity representing user registrations for events
 * 
 * @author Campus Connect Team
 */
@Entity
@Table(name = "event_registrations", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "event_id"}),
       indexes = {
           @Index(name = "idx_event_registrations_user", columnList = "user_id"),
           @Index(name = "idx_event_registrations_event", columnList = "event_id")
       })
@EntityListeners(AuditingEntityListener.class)
public class EventRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_status", nullable = false)
    private AttendanceStatus attendanceStatus = AttendanceStatus.REGISTERED;

    @CreatedDate
    @Column(name = "registration_date", nullable = false, updatable = false)
    private LocalDateTime registrationDate;

    // Constructors
    public EventRegistration() {}

    public EventRegistration(User user, Event event) {
        this.user = user;
        this.event = event;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "EventRegistration{" +
                "id=" + id +
                ", userId=" + (user != null ? user.getId() : null) +
                ", eventId=" + (event != null ? event.getId() : null) +
                ", attendanceStatus=" + attendanceStatus +
                ", registrationDate=" + registrationDate +
                '}';
    }
}

/**
 * Enumeration for attendance status
 */
enum AttendanceStatus {
    REGISTERED("Registered"),
    ATTENDED("Attended"),
    NO_SHOW("No Show"),
    CANCELLED("Cancelled");

    private final String displayName;

    AttendanceStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}