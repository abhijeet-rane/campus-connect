package com.campusconnect.repository;

import com.campusconnect.entity.DifficultyLevel;
import com.campusconnect.entity.Project;
import com.campusconnect.entity.ProjectStatus;
import com.campusconnect.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Project entity operations
 * 
 * @author Campus Connect Team
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * Find project by ID and active status
     * @param id the project ID
     * @param isActive the active status
     * @return Optional containing the project if found
     */
    Optional<Project> findByIdAndIsActive(Long id, Boolean isActive);

    /**
     * Find projects by category
     * @param category the project category
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of projects in the specified category
     */
    Page<Project> findByCategoryAndIsActive(String category, Boolean isActive, Pageable pageable);

    /**
     * Find projects by difficulty level
     * @param difficultyLevel the difficulty level
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of projects with the specified difficulty level
     */
    Page<Project> findByDifficultyLevelAndIsActive(DifficultyLevel difficultyLevel, Boolean isActive, Pageable pageable);

    /**
     * Find projects by status
     * @param status the project status
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of projects with the specified status
     */
    Page<Project> findByStatusAndIsActive(ProjectStatus status, Boolean isActive, Pageable pageable);

    /**
     * Find featured projects
     * @param isFeatured the featured status
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of featured projects
     */
    Page<Project> findByIsFeaturedAndIsActive(Boolean isFeatured, Boolean isActive, Pageable pageable);

    /**
     * Find projects by owner
     * @param owner the project owner
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of projects owned by the specified user
     */
    Page<Project> findByOwnerAndIsActive(User owner, Boolean isActive, Pageable pageable);

    /**
     * Search projects by title or description
     * @param searchTerm the search term
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of projects matching the search criteria
     */
    @Query("SELECT p FROM Project p WHERE " +
           "(LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "p.isActive = :isActive")
    Page<Project> searchProjects(@Param("searchTerm") String searchTerm, @Param("isActive") Boolean isActive, Pageable pageable);

    /**
     * Find projects by tags
     * @param tag the tag to search for
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of projects containing the specified tag
     */
    @Query("SELECT p FROM Project p WHERE :tag MEMBER OF p.tags AND p.isActive = :isActive")
    Page<Project> findByTagsContaining(@Param("tag") String tag, @Param("isActive") Boolean isActive, Pageable pageable);

    /**
     * Find projects by required skills
     * @param skill the skill to search for
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of projects requiring the specified skill
     */
    @Query("SELECT p FROM Project p WHERE :skill MEMBER OF p.requiredSkills AND p.isActive = :isActive")
    Page<Project> findByRequiredSkillsContaining(@Param("skill") String skill, @Param("isActive") Boolean isActive, Pageable pageable);

    /**
     * Find projects liked by a specific user
     * @param userId the user ID
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of projects liked by the user
     */
    @Query("SELECT p FROM Project p JOIN p.likes l WHERE l.user.id = :userId AND p.isActive = :isActive")
    Page<Project> findProjectsLikedByUser(@Param("userId") Long userId, @Param("isActive") Boolean isActive, Pageable pageable);

    /**
     * Find projects where user is a collaborator
     * @param userId the user ID
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of projects where the user is a collaborator
     */
    @Query("SELECT p FROM Project p JOIN p.collaborators c WHERE c.user.id = :userId AND c.isActive = true AND p.isActive = :isActive")
    Page<Project> findProjectsByCollaborator(@Param("userId") Long userId, @Param("isActive") Boolean isActive, Pageable pageable);

    /**
     * Find trending projects (most liked recently)
     * @param since the date since when to consider likes
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of trending projects
     */
    @Query("SELECT p FROM Project p LEFT JOIN p.likes l " +
           "WHERE p.isActive = :isActive AND (l.createdAt >= :since OR l.createdAt IS NULL) " +
           "GROUP BY p " +
           "ORDER BY COUNT(l) DESC, p.createdAt DESC")
    Page<Project> findTrendingProjects(@Param("since") LocalDateTime since, @Param("isActive") Boolean isActive, Pageable pageable);

    /**
     * Find most liked projects
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of most liked projects
     */
    @Query("SELECT p FROM Project p WHERE p.isActive = :isActive ORDER BY p.likesCount DESC, p.createdAt DESC")
    Page<Project> findMostLikedProjects(@Param("isActive") Boolean isActive, Pageable pageable);

    /**
     * Find most viewed projects
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of most viewed projects
     */
    @Query("SELECT p FROM Project p WHERE p.isActive = :isActive ORDER BY p.viewsCount DESC, p.createdAt DESC")
    Page<Project> findMostViewedProjects(@Param("isActive") Boolean isActive, Pageable pageable);

    /**
     * Find recently created projects
     * @param since the date since when to look for new projects
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of recently created projects
     */
    Page<Project> findByCreatedAtAfterAndIsActive(LocalDateTime since, Boolean isActive, Pageable pageable);

    /**
     * Increment project views count
     * @param projectId the project ID
     */
    @Modifying
    @Query("UPDATE Project p SET p.viewsCount = p.viewsCount + 1 WHERE p.id = :projectId")
    void incrementViewsCount(@Param("projectId") Long projectId);

    /**
     * Increment project likes count
     * @param projectId the project ID
     */
    @Modifying
    @Query("UPDATE Project p SET p.likesCount = p.likesCount + 1 WHERE p.id = :projectId")
    void incrementLikesCount(@Param("projectId") Long projectId);

    /**
     * Decrement project likes count
     * @param projectId the project ID
     */
    @Modifying
    @Query("UPDATE Project p SET p.likesCount = p.likesCount - 1 WHERE p.id = :projectId AND p.likesCount > 0")
    void decrementLikesCount(@Param("projectId") Long projectId);

    /**
     * Increment project comments count
     * @param projectId the project ID
     */
    @Modifying
    @Query("UPDATE Project p SET p.commentsCount = p.commentsCount + 1 WHERE p.id = :projectId")
    void incrementCommentsCount(@Param("projectId") Long projectId);

    /**
     * Decrement project comments count
     * @param projectId the project ID
     */
    @Modifying
    @Query("UPDATE Project p SET p.commentsCount = p.commentsCount - 1 WHERE p.id = :projectId AND p.commentsCount > 0")
    void decrementCommentsCount(@Param("projectId") Long projectId);

    /**
     * Count projects by category
     * @param category the project category
     * @param isActive the active status
     * @return count of projects in the category
     */
    long countByCategoryAndIsActive(String category, Boolean isActive);

    /**
     * Count projects by status
     * @param status the project status
     * @param isActive the active status
     * @return count of projects with the status
     */
    long countByStatusAndIsActive(ProjectStatus status, Boolean isActive);

    /**
     * Count projects by owner
     * @param owner the project owner
     * @param isActive the active status
     * @return count of projects owned by the user
     */
    long countByOwnerAndIsActive(User owner, Boolean isActive);

    /**
     * Find all distinct categories
     * @param isActive the active status
     * @return List of project categories
     */
    @Query("SELECT DISTINCT p.category FROM Project p WHERE p.isActive = :isActive")
    List<String> findAllCategories(@Param("isActive") Boolean isActive);

    /**
     * Find all distinct tags
     * @param isActive the active status
     * @return List of project tags
     */
    @Query("SELECT DISTINCT tag FROM Project p JOIN p.tags tag WHERE p.isActive = :isActive")
    List<String> findAllTags(@Param("isActive") Boolean isActive);

    /**
     * Find all distinct required skills
     * @param isActive the active status
     * @return List of required skills
     */
    @Query("SELECT DISTINCT skill FROM Project p JOIN p.requiredSkills skill WHERE p.isActive = :isActive")
    List<String> findAllRequiredSkills(@Param("isActive") Boolean isActive);
}