package com.campusconnect.dto.request;

import com.campusconnect.entity.DifficultyLevel;
import com.campusconnect.entity.ProjectStatus;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * DTO for project update request
 * 
 * @author Campus Connect Team
 */
public class ProjectUpdateRequest {

    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    private String description;
    private String category;
    private DifficultyLevel difficultyLevel;
    private String expectedDuration;
    private String teamSize;
    private List<String> requiredSkills;
    private String requirements;
    private ProjectStatus status;
    private List<String> tags;
    private Boolean isFeatured;

    // Constructors
    public ProjectUpdateRequest() {}

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getExpectedDuration() {
        return expectedDuration;
    }

    public void setExpectedDuration(String expectedDuration) {
        this.expectedDuration = expectedDuration;
    }

    public String getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(String teamSize) {
        this.teamSize = teamSize;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "ProjectUpdateRequest{" +
                "title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", difficultyLevel=" + difficultyLevel +
                ", status=" + status +
                '}';
    }
}