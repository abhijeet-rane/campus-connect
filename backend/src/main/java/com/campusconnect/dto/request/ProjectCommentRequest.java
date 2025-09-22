package com.campusconnect.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for project comment request
 * 
 * @author Campus Connect Team
 */
public class ProjectCommentRequest {

    @NotBlank(message = "Comment content is required")
    @Size(max = 2000, message = "Comment content must not exceed 2000 characters")
    private String content;

    private Long parentCommentId;

    // Constructors
    public ProjectCommentRequest() {}

    public ProjectCommentRequest(String content) {
        this.content = content;
    }

    public ProjectCommentRequest(String content, Long parentCommentId) {
        this.content = content;
        this.parentCommentId = parentCommentId;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return "ProjectCommentRequest{" +
                "content='" + content + '\'' +
                ", parentCommentId=" + parentCommentId +
                '}';
    }
}