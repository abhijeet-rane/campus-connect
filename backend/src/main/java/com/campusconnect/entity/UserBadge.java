package com.campusconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * UserBadge entity representing user achievements and badges
 * 
 * @author Campus Connect Team
 */
@Entity
@Table(name = "user_badges",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "badge_name"}),
       indexes = {
           @Index(name = "idx_user_badges_user", columnList = "user_id")
       })
@EntityListeners(AuditingEntityListener.class)
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "badge_name", nullable = false, length = 100)
    @NotBlank(message = "Badge name is required")
    private String badgeName;

    @Column(name = "badge_description", columnDefinition = "TEXT")
    private String badgeDescription;

    @Column(name = "badge_icon", length = 100)
    private String badgeIcon;

    @Column(nullable = false)
    @Min(value = 0, message = "Progress cannot be negative")
    @Max(value = 100, message = "Progress cannot exceed 100")
    private Integer progress = 0;

    @Column(name = "max_progress", nullable = false)
    @Min(value = 1, message = "Max progress must be at least 1")
    private Integer maxProgress = 100;

    @Column(name = "is_earned", nullable = false)
    private Boolean isEarned = false;

    @Column(name = "earned_date")
    private LocalDateTime earnedDate;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Constructors
    public UserBadge() {}

    public UserBadge(User user, String badgeName, String badgeDescription, String badgeIcon) {
        this.user = user;
        this.badgeName = badgeName;
        this.badgeDescription = badgeDescription;
        this.badgeIcon = badgeIcon;
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

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public String getBadgeDescription() {
        return badgeDescription;
    }

    public void setBadgeDescription(String badgeDescription) {
        this.badgeDescription = badgeDescription;
    }

    public String getBadgeIcon() {
        return badgeIcon;
    }

    public void setBadgeIcon(String badgeIcon) {
        this.badgeIcon = badgeIcon;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
        // Auto-earn badge if progress reaches max
        if (progress >= maxProgress && !isEarned) {
            this.isEarned = true;
            this.earnedDate = LocalDateTime.now();
        }
    }

    public Integer getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(Integer maxProgress) {
        this.maxProgress = maxProgress;
    }

    public Boolean getIsEarned() {
        return isEarned;
    }

    public void setIsEarned(Boolean isEarned) {
        this.isEarned = isEarned;
        if (isEarned && earnedDate == null) {
            this.earnedDate = LocalDateTime.now();
        }
    }

    public LocalDateTime getEarnedDate() {
        return earnedDate;
    }

    public void setEarnedDate(LocalDateTime earnedDate) {
        this.earnedDate = earnedDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Utility methods
    public void incrementProgress(int amount) {
        this.progress = Math.min(this.progress + amount, this.maxProgress);
        if (this.progress >= this.maxProgress && !this.isEarned) {
            this.isEarned = true;
            this.earnedDate = LocalDateTime.now();
        }
    }

    public double getProgressPercentage() {
        return (double) progress / maxProgress * 100;
    }

    @Override
    public String toString() {
        return "UserBadge{" +
                "id=" + id +
                ", userId=" + (user != null ? user.getId() : null) +
                ", badgeName='" + badgeName + '\'' +
                ", progress=" + progress +
                ", maxProgress=" + maxProgress +
                ", isEarned=" + isEarned +
                '}';
    }
}