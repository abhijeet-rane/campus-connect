package com.campusconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * ProjectComment entity representing comments on projects
 * 
 * @author Campus Connect Team
 */
@Entity
@Table(name = "project_comments",
       indexes = {
           @Index(name = "idx_project_comments_project", columnList = "project_id")
       })
@EntityListeners(AuditingEntityListener.class)
public class ProjectComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Comment content is required")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private ProjectComment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProjectComment> replies = new HashSet<>();

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public ProjectComment() {}

    public ProjectComment(Project project, User user, String content) {
        this.project = project;
        this.user = user;
        this.content = content;
    }

    public ProjectComment(Project project, User user, String content, ProjectComment parentComment) {
        this.project = project;
        this.user = user;
        this.content = content;
        this.parentComment = parentComment;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ProjectComment getParentComment() {
        return parentComment;
    }

    public void setParentComment(ProjectComment parentComment) {
        this.parentComment = parentComment;
    }

    public Set<ProjectComment> getReplies() {
        return replies;
    }

    public void setReplies(Set<ProjectComment> replies) {
        this.replies = replies;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Utility methods
    public boolean isReply() {
        return parentComment != null;
    }

    @Override
    public String toString() {
        return "ProjectComment{" +
                "id=" + id +
                ", projectId=" + (project != null ? project.getId() : null) +
                ", userId=" + (user != null ? user.getId() : null) +
                ", content='" + content + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}