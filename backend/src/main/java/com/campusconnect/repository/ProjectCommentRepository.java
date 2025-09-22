package com.campusconnect.repository;

import com.campusconnect.entity.Project;
import com.campusconnect.entity.ProjectComment;
import com.campusconnect.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for ProjectComment entity operations
 * 
 * @author Campus Connect Team
 */
@Repository
public interface ProjectCommentRepository extends JpaRepository<ProjectComment, Long> {

    /**
     * Find comments by project
     * @param project the project
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of comments for the project
     */
    Page<ProjectComment> findByProjectAndIsActive(Project project, Boolean isActive, Pageable pageable);

    /**
     * Find top-level comments by project (no parent comment)
     * @param project the project
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of top-level comments for the project
     */
    Page<ProjectComment> findByProjectAndParentCommentIsNullAndIsActive(Project project, Boolean isActive, Pageable pageable);

    /**
     * Find replies to a comment
     * @param parentComment the parent comment
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of replies to the comment
     */
    Page<ProjectComment> findByParentCommentAndIsActive(ProjectComment parentComment, Boolean isActive, Pageable pageable);

    /**
     * Find comments by user
     * @param user the user
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of comments by the user
     */
    Page<ProjectComment> findByUserAndIsActive(User user, Boolean isActive, Pageable pageable);

    /**
     * Count comments for a project
     * @param project the project
     * @param isActive the active status
     * @return count of comments
     */
    long countByProjectAndIsActive(Project project, Boolean isActive);

    /**
     * Count top-level comments for a project
     * @param project the project
     * @param isActive the active status
     * @return count of top-level comments
     */
    long countByProjectAndParentCommentIsNullAndIsActive(Project project, Boolean isActive);

    /**
     * Count replies to a comment
     * @param parentComment the parent comment
     * @param isActive the active status
     * @return count of replies
     */
    long countByParentCommentAndIsActive(ProjectComment parentComment, Boolean isActive);

    /**
     * Count comments by user
     * @param user the user
     * @param isActive the active status
     * @return count of comments by the user
     */
    long countByUserAndIsActive(User user, Boolean isActive);

    /**
     * Find recent comments for a project
     * @param projectId the project ID
     * @param isActive the active status
     * @param pageable pagination information
     * @return List of recent comments
     */
    @Query("SELECT pc FROM ProjectComment pc WHERE pc.project.id = :projectId AND pc.isActive = :isActive ORDER BY pc.createdAt DESC")
    List<ProjectComment> findRecentCommentsByProject(@Param("projectId") Long projectId, @Param("isActive") Boolean isActive, Pageable pageable);

    /**
     * Find comments with replies count
     * @param projectId the project ID
     * @param isActive the active status
     * @return List of comments with reply counts
     */
    @Query("SELECT pc, COUNT(reply) as replyCount FROM ProjectComment pc " +
           "LEFT JOIN pc.replies reply " +
           "WHERE pc.project.id = :projectId AND pc.parentComment IS NULL AND pc.isActive = :isActive " +
           "GROUP BY pc " +
           "ORDER BY pc.createdAt DESC")
    List<Object[]> findCommentsWithReplyCounts(@Param("projectId") Long projectId, @Param("isActive") Boolean isActive);
}