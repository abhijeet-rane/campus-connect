package com.campusconnect.repository;

import com.campusconnect.entity.Project;
import com.campusconnect.entity.ProjectLike;
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
 * Repository interface for ProjectLike entity operations
 * 
 * @author Campus Connect Team
 */
@Repository
public interface ProjectLikeRepository extends JpaRepository<ProjectLike, Long> {

    /**
     * Find like by user and project
     * @param user the user
     * @param project the project
     * @return Optional containing the like if found
     */
    Optional<ProjectLike> findByUserAndProject(User user, Project project);

    /**
     * Check if user has liked a project
     * @param user the user
     * @param project the project
     * @return true if user has liked the project, false otherwise
     */
    boolean existsByUserAndProject(User user, Project project);

    /**
     * Find likes by user
     * @param user the user
     * @param pageable pagination information
     * @return Page of likes by the user
     */
    Page<ProjectLike> findByUser(User user, Pageable pageable);

    /**
     * Find likes by project
     * @param project the project
     * @param pageable pagination information
     * @return Page of likes for the project
     */
    Page<ProjectLike> findByProject(Project project, Pageable pageable);

    /**
     * Count likes for a project
     * @param project the project
     * @return count of likes
     */
    long countByProject(Project project);

    /**
     * Count likes by user
     * @param user the user
     * @return count of likes by the user
     */
    long countByUser(User user);

    /**
     * Find users who liked a specific project
     * @param projectId the project ID
     * @return List of users who liked the project
     */
    @Query("SELECT pl.user FROM ProjectLike pl WHERE pl.project.id = :projectId")
    List<User> findUsersWhoLikedProject(@Param("projectId") Long projectId);

    /**
     * Find projects liked by a specific user
     * @param userId the user ID
     * @return List of projects liked by the user
     */
    @Query("SELECT pl.project FROM ProjectLike pl WHERE pl.user.id = :userId")
    List<Project> findProjectsLikedByUser(@Param("userId") Long userId);

    /**
     * Delete like by user and project
     * @param user the user
     * @param project the project
     */
    void deleteByUserAndProject(User user, Project project);
}