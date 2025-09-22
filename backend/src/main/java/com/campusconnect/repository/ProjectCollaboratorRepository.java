package com.campusconnect.repository;

import com.campusconnect.entity.Project;
import com.campusconnect.entity.ProjectCollaborator;
import com.campusconnect.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ProjectCollaborator entity operations
 * 
 * @author Campus Connect Team
 */
@Repository
public interface ProjectCollaboratorRepository extends JpaRepository<ProjectCollaborator, Long> {

    /**
     * Find collaborator by user and project
     * @param user the user
     * @param project the project
     * @return Optional containing the collaborator if found
     */
    Optional<ProjectCollaborator> findByUserAndProject(User user, Project project);

    /**
     * Check if user is collaborator on project
     * @param user the user
     * @param project the project
     * @return true if user is collaborator, false otherwise
     */
    boolean existsByUserAndProject(User user, Project project);

    /**
     * Find collaborators by project
     * @param project the project
     * @param pageable pagination information
     * @return Page of collaborators for the project
     */
    Page<ProjectCollaborator> findByProject(Project project, Pageable pageable);

    /**
     * Find active collaborators by project
     * @param project the project
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of active collaborators for the project
     */
    Page<ProjectCollaborator> findByProjectAndIsActive(Project project, Boolean isActive, Pageable pageable);

    /**
     * Find collaborations by user
     * @param user the user
     * @param pageable pagination information
     * @return Page of collaborations for the user
     */
    Page<ProjectCollaborator> findByUser(User user, Pageable pageable);

    /**
     * Find active collaborations by user
     * @param user the user
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of active collaborations for the user
     */
    Page<ProjectCollaborator> findByUserAndIsActive(User user, Boolean isActive, Pageable pageable);

    /**
     * Count collaborators for a project
     * @param project the project
     * @return count of collaborators
     */
    long countByProject(Project project);

    /**
     * Count active collaborators for a project
     * @param project the project
     * @param isActive the active status
     * @return count of active collaborators
     */
    long countByProjectAndIsActive(Project project, Boolean isActive);

    /**
     * Count collaborations by user
     * @param user the user
     * @return count of collaborations by the user
     */
    long countByUser(User user);

    /**
     * Find users collaborating on a specific project
     * @param projectId the project ID
     * @param isActive the active status
     * @return List of users collaborating on the project
     */
    @Query("SELECT pc.user FROM ProjectCollaborator pc WHERE pc.project.id = :projectId AND pc.isActive = :isActive")
    List<User> findUsersCollaboratingOnProject(@Param("projectId") Long projectId, @Param("isActive") Boolean isActive);

    /**
     * Find projects where user is collaborating
     * @param userId the user ID
     * @param isActive the active status
     * @return List of projects where the user is collaborating
     */
    @Query("SELECT pc.project FROM ProjectCollaborator pc WHERE pc.user.id = :userId AND pc.isActive = :isActive")
    List<Project> findProjectsWhereUserIsCollaborating(@Param("userId") Long userId, @Param("isActive") Boolean isActive);

    /**
     * Delete collaboration by user and project
     * @param user the user
     * @param project the project
     */
    void deleteByUserAndProject(User user, Project project);
}