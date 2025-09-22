package com.campusconnect.entity;

/**
 * Enumeration for project status
 * 
 * @author Campus Connect Team
 */
public enum ProjectStatus {
    SEEKING_COLLABORATORS("Seeking Collaborators"),
    IN_DEVELOPMENT("In Development"),
    COMPLETED("Completed"),
    ON_HOLD("On Hold");

    private final String displayName;

    ProjectStatus(String displayName) {
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