package com.campusconnect.service;

import com.campusconnect.dto.request.ProjectCreateRequest;
import com.campusconnect.dto.response.ProjectResponse;
import com.campusconnect.entity.*;
import com.campusconnect.exception.BadRequestException;
import com.campusconnect.exception.BusinessLogicException;
import com.campusconnect.exception.ResourceNotFoundException;
import com.campusconnect.repository.ProjectLikeRepository;
import com.campusconnect.repository.ProjectRepository;
import com.campusconnect.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for Project entity operations
 * 
 * @author Campus Connect Team
 */
@Service
@Transactional
public class ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    private final ProjectRepository projectRepository;
    private final ProjectLikeRepository projectLikeRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository,
                         ProjectLikeRepository projectLikeRepository,
                         UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.projectLikeRepository = projectLikeRepository;
        this.userRepository = userRepository;
        logger.info("ProjectService initialized successfully");
    }

    /**
     * Create a new project
     */
    public ProjectResponse createProject(ProjectCreateRequest request, Long ownerId) {
        logger.info("Creating new project: {} by owner ID: {}", request.getTitle(), ownerId);
        
        try {
            User owner = userRepository.findById(ownerId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", ownerId));

            Project project = new Project();
            project.setTitle(request.getTitle());
            project.setDescription(request.getDescription());
            project.setCategory(request.getCategory());
            project.setDifficultyLevel(request.getDifficultyLevel());
            project.setExpectedDuration(request.getExpectedDuration());
            project.setTeamSize(request.getTeamSize());
            project.setRequiredSkills(request.getRequiredSkills());
            project.setRequirements(request.getRequirements());
            project.setOwner(owner);
            project.setTags(request.getTags());
            project.setIsFeatured(request.getIsFeatured());
            project.setIsActive(true);
            project.setStatus(ProjectStatus.SEEKING_COLLABORATORS);

            Project savedProject = projectRepository.save(project);
            logger.info("Successfully created project with ID: {}", savedProject.getId());
            
            return convertToProjectResponse(savedProject, ownerId);
            
        } catch (Exception e) {
            logger.error("Error creating project: {}", request.getTitle(), e);
            throw e;
        }
    }

    /**
     * Get project by ID
     */
    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long projectId, Long userId) {
        logger.debug("Fetching project with ID: {}", projectId);
        
        try {
            Project project = projectRepository.findByIdAndIsActive(projectId, true)
                    .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
            
            // Increment view count
            projectRepository.incrementViewsCount(projectId);
            
            return convertToProjectResponse(project, userId);
            
        } catch (Exception e) {
            logger.error("Error fetching project with ID: {}", projectId, e);
            throw e;
        }
    }

    /**
     * Get all projects with pagination
     */
    @Transactional(readOnly = true)
    public Page<ProjectResponse> getAllProjects(Pageable pageable, Long userId) {
        logger.debug("Fetching all projects with pagination");
        
        try {
            Page<Project> projects = projectRepository.findByIsActive(true, pageable);
            return projects.map(project -> convertToProjectResponse(project, userId));
            
        } catch (Exception e) {
            logger.error("Error fetching all projects", e);
            throw e;
        }
    }

    /**
     * Get projects by category
     */
    @Transactional(readOnly = true)
    public Page<ProjectResponse> getProjectsByCategory(String category, Pageable pageable, Long userId) {
        logger.debug("Fetching projects by category: {}", category);
        
        try {
            Page<Project> projects = projectRepository.findByCategoryAndIsActive(category, true, pageable);
            return projects.map(project -> convertToProjectResponse(project, userId));
            
        } catch (Exception e) {
            logger.error("Error fetching projects by category: {}", category, e);
            throw e;
        }
    }

    /**
     * Get featured projects
     */
    @Transactional(readOnly = true)
    public Page<ProjectResponse> getFeaturedProjects(Pageable pageable, Long userId) {
        logger.debug("Fetching featured projects");
        
        try {
            Page<Project> projects = projectRepository.findByIsFeaturedAndIsActive(true, true, pageable);
            return projects.map(project -> convertToProjectResponse(project, userId));
            
        } catch (Exception e) {
            logger.error("Error fetching featured projects", e);
            throw e;
        }
    }

    /**
     * Search projects
     */
    @Transactional(readOnly = true)
    public Page<ProjectResponse> searchProjects(String searchTerm, Pageable pageable, Long userId) {
        logger.debug("Searching projects with term: '{}'", searchTerm);
        
        try {
            Page<Project> projects = projectRepository.searchProjects(searchTerm, true, pageable);
            return projects.map(project -> convertToProjectResponse(project, userId));
            
        } catch (Exception e) {
            logger.error("Error searching projects with term: '{}'", searchTerm, e);
            throw e;
        }
    }

    /**
     * Get projects by difficulty level
     */
    @Transactional(readOnly = true)
    public Page<ProjectResponse> getProjectsByDifficulty(DifficultyLevel difficulty, Pageable pageable, Long userId) {
        logger.debug("Fetching projects by difficulty: {}", difficulty);
        
        try {
            Page<Project> projects = projectRepository.findByDifficultyLevelAndIsActive(difficulty, true, pageable);
            return projects.map(project -> convertToProjectResponse(project, userId));
            
        } catch (Exception e) {
            logger.error("Error fetching projects by difficulty: {}", difficulty, e);
            throw e;
        }
    }

    /**
     * Get projects by status
     */
    @Transactional(readOnly = true)
    public Page<ProjectResponse> getProjectsByStatus(ProjectStatus status, Pageable pageable, Long userId) {
        logger.debug("Fetching projects by status: {}", status);
        
        try {
            Page<Project> projects = projectRepository.findByStatusAndIsActive(status, true, pageable);
            return projects.map(project -> convertToProjectResponse(project, userId));
            
        } catch (Exception e) {
            logger.error("Error fetching projects by status: {}", status, e);
            throw e;
        }
    }

    /**
     * Get trending projects
     */
    @Transactional(readOnly = true)
    public Page<ProjectResponse> getTrendingProjects(Pageable pageable, Long userId) {
        logger.debug("Fetching trending projects");
        
        try {
            LocalDateTime since = LocalDateTime.now().minusDays(7); // Last 7 days
            Page<Project> projects = projectRepository.findTrendingProjects(since, true, pageable);
            return projects.map(project -> convertToProjectResponse(project, userId));
            
        } catch (Exception e) {
            logger.error("Error fetching trending projects", e);
            throw e;
        }
    }

    /**
     * Get most liked projects
     */
    @Transactional(readOnly = true)
    public Page<ProjectResponse> getMostLikedProjects(Pageable pageable, Long userId) {
        logger.debug("Fetching most liked projects");
        
        try {
            Page<Project> projects = projectRepository.findMostLikedProjects(true, pageable);
            return projects.map(project -> convertToProjectResponse(project, userId));
            
        } catch (Exception e) {
            logger.error("Error fetching most liked projects", e);
            throw e;
        }
    }

    /**
     * Get projects by owner
     */
    @Transactional(readOnly = true)
    public Page<ProjectResponse> getProjectsByOwner(Long ownerId, Pageable pageable, Long userId) {
        logger.debug("Fetching projects by owner ID: {}", ownerId);
        
        try {
            User owner = userRepository.findById(ownerId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", ownerId));
            
            Page<Project> projects = projectRepository.findByOwnerAndIsActive(owner, true, pageable);
            return projects.map(project -> convertToProjectResponse(project, userId));
            
        } catch (Exception e) {
            logger.error("Error fetching projects by owner ID: {}", ownerId, e);
            throw e;
        }
    }

    /**
     * Like/Unlike project
     */
    public void toggleProjectLike(Long projectId, Long userId) {
        logger.info("Toggling like for project ID: {} by user ID: {}", projectId, userId);
        
        try {
            Project project = projectRepository.findByIdAndIsActive(projectId, true)
                    .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
            
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

            boolean isLiked = projectLikeRepository.existsByUserAndProject(user, project);
            
            if (isLiked) {
                // Unlike
                projectLikeRepository.deleteByUserAndProject(user, project);
                projectRepository.decrementLikesCount(projectId);
                logger.info("User ID: {} unliked project ID: {}", userId, projectId);
            } else {
                // Like
                ProjectLike like = new ProjectLike(project, user);
                projectLikeRepository.save(like);
                projectRepository.incrementLikesCount(projectId);
                logger.info("User ID: {} liked project ID: {}", userId, projectId);
            }
            
        } catch (Exception e) {
            logger.error("Error toggling like for project ID: {} by user ID: {}", projectId, userId, e);
            throw e;
        }
    }

    /**
     * Update project
     */
    public ProjectResponse updateProject(Long projectId, ProjectCreateRequest request, Long userId) {
        logger.info("Updating project ID: {} by user ID: {}", projectId, userId);
        
        try {
            Project project = projectRepository.findByIdAndIsActive(projectId, true)
                    .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

            // Check if user is the owner
            if (!project.getOwner().getId().equals(userId)) {
                throw new BusinessLogicException("Only the project owner can update this project");
            }

            // Update project fields
            if (request.getTitle() != null) project.setTitle(request.getTitle());
            if (request.getDescription() != null) project.setDescription(request.getDescription());
            if (request.getCategory() != null) project.setCategory(request.getCategory());
            if (request.getDifficultyLevel() != null) project.setDifficultyLevel(request.getDifficultyLevel());
            if (request.getExpectedDuration() != null) project.setExpectedDuration(request.getExpectedDuration());
            if (request.getTeamSize() != null) project.setTeamSize(request.getTeamSize());
            if (request.getRequiredSkills() != null) project.setRequiredSkills(request.getRequiredSkills());
            if (request.getRequirements() != null) project.setRequirements(request.getRequirements());
            if (request.getTags() != null) project.setTags(request.getTags());

            Project updatedProject = projectRepository.save(project);
            logger.info("Successfully updated project ID: {}", projectId);
            
            return convertToProjectResponse(updatedProject, userId);
            
        } catch (Exception e) {
            logger.error("Error updating project ID: {}", projectId, e);
            throw e;
        }
    }

    /**
     * Delete project
     */
    public void deleteProject(Long projectId, Long userId) {
        logger.info("Deleting project ID: {} by user ID: {}", projectId, userId);
        
        try {
            Project project = projectRepository.findByIdAndIsActive(projectId, true)
                    .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

            // Check if user is the owner or admin
            if (!project.getOwner().getId().equals(userId)) {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
                if (!user.isAdmin()) {
                    throw new BusinessLogicException("Only the project owner or admin can delete this project");
                }
            }

            project.setIsActive(false);
            projectRepository.save(project);
            
            logger.info("Successfully deleted project ID: {}", projectId);
            
        } catch (Exception e) {
            logger.error("Error deleting project ID: {}", projectId, e);
            throw e;
        }
    }

    /**
     * Get project categories
     */
    @Transactional(readOnly = true)
    public List<String> getProjectCategories() {
        logger.debug("Fetching project categories");
        
        try {
            return projectRepository.findAllCategories(true);
        } catch (Exception e) {
            logger.error("Error fetching project categories", e);
            throw e;
        }
    }

    /**
     * Get project tags
     */
    @Transactional(readOnly = true)
    public List<String> getProjectTags() {
        logger.debug("Fetching project tags");
        
        try {
            return projectRepository.findAllTags(true);
        } catch (Exception e) {
            logger.error("Error fetching project tags", e);
            throw e;
        }
    }

    /**
     * Get required skills
     */
    @Transactional(readOnly = true)
    public List<String> getRequiredSkills() {
        logger.debug("Fetching required skills");
        
        try {
            return projectRepository.findAllRequiredSkills(true);
        } catch (Exception e) {
            logger.error("Error fetching required skills", e);
            throw e;
        }
    }

    /**
     * Convert Project entity to ProjectResponse DTO
     */
    private ProjectResponse convertToProjectResponse(Project project, Long userId) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setTitle(project.getTitle());
        response.setDescription(project.getDescription());
        response.setCategory(project.getCategory());
        response.setDifficultyLevel(project.getDifficultyLevel());
        response.setExpectedDuration(project.getExpectedDuration());
        response.setTeamSize(project.getTeamSize());
        response.setRequiredSkills(project.getRequiredSkills());
        response.setRequirements(project.getRequirements());
        response.setStatus(project.getStatus());
        response.setTags(project.getTags());
        response.setIsFeatured(project.getIsFeatured());
        response.setIsActive(project.getIsActive());
        response.setLikesCount(project.getLikesCount());
        response.setCommentsCount(project.getCommentsCount());
        response.setViewsCount(project.getViewsCount());
        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());

        // Set owner info
        User owner = project.getOwner();
        response.setOwner(new ProjectResponse.OwnerInfo(
                owner.getId(),
                owner.getFullName(),
                owner.getEmail(),
                owner.getAvatarUrl(),
                owner.getDepartment(),
                owner.getAcademicYear()
        ));

        // Set user interaction status
        if (userId != null) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                response.setIsLiked(projectLikeRepository.existsByUserAndProject(user, project));
                response.setIsOwner(project.getOwner().getId().equals(userId));
                // TODO: Set isCollaborator when collaborator functionality is implemented
                response.setIsCollaborator(false);
            }
        }

        return response;
    }
}