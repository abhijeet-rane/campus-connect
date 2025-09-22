package com.campusconnect.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for project comment response
 * 
 * @author Campus Connect Team
 */
public class ProjectCommentResponse {

    private Long id;
    private Long projectId;
    private UserResponse user;
    private String content;
    private Long parentCommentId;
    private List<ProjectCommentResponse> replies;
    private Boolean isActive;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    // Constructors
    public ProjectCommentResponse() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public List<ProjectCommentResponse> getReplies() {
        return replies;
    }

    public void setReplies(List<ProjectCommentResponse> replies) {
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

    @Override
    public String toString() {
        return "ProjectCommentResponse{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", content='" + content + '\'' +
                ", parentCommentId=" + parentCommentId +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}