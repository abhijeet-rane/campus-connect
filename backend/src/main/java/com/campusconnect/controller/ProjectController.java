package com.campusconnect.controller;

import com.campusconnect.dto.request.ProjectCreateRequest;
import com.campusconnect.dto.response.ProjectResponse;
import com.campusconnect.entity.DifficultyLevel;
import com.campusconnect.entity.ProjectStatus;
import com.campusconnect.security.UserPrincipal;
import com.campusconnect.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Project Controller
 * Handles project management operations
 * 
 * @author Campus Connect Team
 */
@RestController
@RequestMapping("/api/v1/projects")
@Tag(name = "Projects", description = "Project management endpoints")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
        logger.info("ProjectController initialized");
    }

    /**
     * Get all projects
     */
    @GetMapping
    @Operation(summary = "Get all projects", description = "Get paginated list of all active projects")
    public ResponseEntity<Page<ProjectResponse>> getAllProjects(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir,
            Authentication authentication) {
        
        logger.debug("Getting all projects - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                    page, size, sortBy, sortDir);
        
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Long userId = getUserId(authentication);
            Page<ProjectResponse> projects = projectService.getAllProjects(pageable, userId);
            
            logger.debug("Successfully retrieved {} projects", projects.getTotalElements());
            return ResponseEntity.ok(projects);
            
        } catch (Exception e) {
            logger.error("Error getting all projects", e);
            throw e;
        }
    }

    /**
     * Get project by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID", description = "Get project details by ID")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id, Authentication authentication) {
        logger.debug("Getting project by ID: {}", id);
        
        try {
            Long userId = getUserId(authentication);
            ProjectResponse project = projectService.getProjectById(id, userId);
            return ResponseEntity.ok(project);
            
        } catch (Exception e) {
            logger.error("Error getting project by ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Create new project
     */
    @PostMapping
    @Operation(summary = "Create project", description = "Create a new project")
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody ProjectCreateRequest createRequest,
            Authentication authentication) {
        
        logger.info("Creating new project: {}", createRequest.getTitle());
        
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            ProjectResponse project = projectService.createProject(createRequest, userPrincipal.getId());
            
            logger.info("Successfully created project with ID: {}", project.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(project);
            
        } catch (Exception e) {
            logger.error("Error creating project: {}", createRequest.getTitle(), e);
            throw e;
        }
    }

    /**
     * Update project
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update project", description = "Update project by ID")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectCreateRequest updateRequest,
            Authentication authentication) {
        
        logger.info("Updating project with ID: {}", id);
        
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            ProjectResponse project = projectService.updateProject(id, updateRequest, userPrincipal.getId());
            
            logger.info("Successfully updated project with ID: {}", id);
            return ResponseEntity.ok(project);
            
        } catch (Exception e) {
            logger.error("Error updating project with ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Delete project
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete project", description = "Delete project by ID")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id, Authentication authentication) {
        logger.info("Deleting project with ID: {}", id);
        
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            projectService.deleteProject(id, userPrincipal.getId());
            
            logger.info("Successfully deleted project with ID: {}", id);
            return ResponseEntity.noContent().build();
            
        } catch (Exception e) {
            logger.error("Error deleting project with ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Search projects
     */
    @GetMapping("/search")
    @Operation(summary = "Search projects", description = "Search projects by title or description")
    public ResponseEntity<Page<ProjectResponse>> searchProjects(
            @Parameter(description = "Search term") @RequestParam String q,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        
        logger.debug("Searching projects with query: '{}', page: {}, size: {}", q, page, size);
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Long userId = getUserId(authentication);
            Page<ProjectResponse> projects = projectService.searchProjects(q, pageable, userId);
            
            logger.debug("Found {} projects matching query: '{}'", projects.getTotalElements(), q);
            return ResponseEntity.ok(projects);
            
        } catch (Exception e) {
            logger.error("Error searching projects with query: '{}'", q, e);
            throw e;
        }
    }

    /**
     * Get projects by category
     */
    @GetMapping("/category/{category}")
    @Operation(summary = "Get projects by category", description = "Get projects filtered by category")
    public ResponseEntity<Page<ProjectResponse>> getProjectsByCategory(
            @PathVariable String category,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        
        logger.debug("Getting projects by category: {}, page: {}, size: {}", category, page, size);
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Long userId = getUserId(authentication);
            Page<ProjectResponse> projects = projectService.getProjectsByCategory(category, pageable, userId);
            
            logger.debug("Found {} projects in category: {}", projects.getTotalElements(), category);
            return ResponseEntity.ok(projects);
            
        } catch (Exception e) {
            logger.error("Error getting projects by category: {}", category, e);
            throw e;
        }
    }

    /**
     * Get projects by difficulty
     */
    @GetMapping("/difficulty/{difficulty}")
    @Operation(summary = "Get projects by difficulty", description = "Get projects filtered by difficulty level")
    public ResponseEntity<Page<ProjectResponse>> getProjectsByDifficulty(
            @PathVariable DifficultyLevel difficulty,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        
        logger.debug("Getting projects by difficulty: {}, page: {}, size: {}", difficulty, page, size);
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Long userId = getUserId(authentication);
            Page<ProjectResponse> projects = projectService.getProjectsByDifficulty(difficulty, pageable, userId);
            
            logger.debug("Found {} projects with difficulty: {}", projects.getTotalElements(), difficulty);
            return ResponseEntity.ok(projects);
            
        } catch (Exception e) {
            logger.error("Error getting projects by difficulty: {}", difficulty, e);
            throw e;
        }
    }

    /**
     * Get projects by status
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get projects by status", description = "Get projects filtered by status")
    public ResponseEntity<Page<ProjectResponse>> getProjectsByStatus(
            @PathVariable ProjectStatus status,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        
        logger.debug("Getting projects by status: {}, page: {}, size: {}", status, page, size);
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Long userId = getUserId(authentication);
            Page<ProjectResponse> projects = projectService.getProjectsByStatus(status, pageable, userId);
            
            logger.debug("Found {} projects with status: {}", projects.getTotalElements(), status);
            return ResponseEntity.ok(projects);
            
        } catch (Exception e) {
            logger.error("Error getting projects by status: {}", status, e);
            throw e;
        }
    }

    /**
     * Get featured projects
     */
    @GetMapping("/featured")
    @Operation(summary = "Get featured projects", description = "Get featured projects")
    public ResponseEntity<Page<ProjectResponse>> getFeaturedProjects(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        
        logger.debug("Getting featured projects - page: {}, size: {}", page, size);
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Long userId = getUserId(authentication);
            Page<ProjectResponse> projects = projectService.getFeaturedProjects(pageable, userId);
            
            logger.debug("Found {} featured projects", projects.getTotalElements());
            return ResponseEntity.ok(projects);
            
        } catch (Exception e) {
            logger.error("Error getting featured projects", e);
            throw e;
        }
    }

    /**
     * Get trending projects
     */
    @GetMapping("/trending")
    @Operation(summary = "Get trending projects", description = "Get trending projects")
    public ResponseEntity<Page<ProjectResponse>> getTrendingProjects(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        
        logger.debug("Getting trending projects - page: {}, size: {}", page, size);
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Long userId = getUserId(authentication);
            Page<ProjectResponse> projects = projectService.getTrendingProjects(pageable, userId);
            
            logger.debug("Found {} trending projects", projects.getTotalElements());
            return ResponseEntity.ok(projects);
            
        } catch (Exception e) {
            logger.error("Error getting trending projects", e);
            throw e;
        }
    }

    /**
     * Get most liked projects
     */
    @GetMapping("/most-liked")
    @Operation(summary = "Get most liked projects", description = "Get most liked projects")
    public ResponseEntity<Page<ProjectResponse>> getMostLikedProjects(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        
        logger.debug("Getting most liked projects - page: {}, size: {}", page, size);
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Long userId = getUserId(authentication);
            Page<ProjectResponse> projects = projectService.getMostLikedProjects(pageable, userId);
            
            logger.debug("Found {} most liked projects", projects.getTotalElements());
            return ResponseEntity.ok(projects);
            
        } catch (Exception e) {
            logger.error("Error getting most liked projects", e);
            throw e;
        }
    }

    /**
     * Get projects by owner
     */
    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "Get projects by owner", description = "Get projects by owner ID")
    public ResponseEntity<Page<ProjectResponse>> getProjectsByOwner(
            @PathVariable Long ownerId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        
        logger.debug("Getting projects by owner ID: {}, page: {}, size: {}", ownerId, page, size);
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Long userId = getUserId(authentication);
            Page<ProjectResponse> projects = projectService.getProjectsByOwner(ownerId, pageable, userId);
            
            logger.debug("Found {} projects owned by user ID: {}", projects.getTotalElements(), ownerId);
            return ResponseEntity.ok(projects);
            
        } catch (Exception e) {
            logger.error("Error getting projects by owner ID: {}", ownerId, e);
            throw e;
        }
    }

    /**
     * Like/Unlike project
     */
    @PostMapping("/{id}/like")
    @Operation(summary = "Toggle project like", description = "Like or unlike a project")
    public ResponseEntity<Void> toggleProjectLike(@PathVariable Long id, Authentication authentication) {
        logger.info("Toggling like for project ID: {}", id);
        
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            projectService.toggleProjectLike(id, userPrincipal.getId());
            
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            logger.error("Error toggling like for project ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Get project categories
     */
    @GetMapping("/categories")
    @Operation(summary = "Get project categories", description = "Get list of all project categories")
    public ResponseEntity<List<String>> getProjectCategories() {
        logger.debug("Getting project categories");
        
        try {
            List<String> categories = projectService.getProjectCategories();
            
            logger.debug("Found {} project categories", categories.size());
            return ResponseEntity.ok(categories);
            
        } catch (Exception e) {
            logger.error("Error getting project categories", e);
            throw e;
        }
    }

    /**
     * Get project tags
     */
    @GetMapping("/tags")
    @Operation(summary = "Get project tags", description = "Get list of all project tags")
    public ResponseEntity<List<String>> getProjectTags() {
        logger.debug("Getting project tags");
        
        try {
            List<String> tags = projectService.getProjectTags();
            
            logger.debug("Found {} project tags", tags.size());
            return ResponseEntity.ok(tags);
            
        } catch (Exception e) {
            logger.error("Error getting project tags", e);
            throw e;
        }
    }

    /**
     * Get required skills
     */
    @GetMapping("/skills")
    @Operation(summary = "Get required skills", description = "Get list of all required skills")
    public ResponseEntity<List<String>> getRequiredSkills() {
        logger.debug("Getting required skills");
        
        try {
            List<String> skills = projectService.getRequiredSkills();
            
            logger.debug("Found {} required skills", skills.size());
            return ResponseEntity.ok(skills);
            
        } catch (Exception e) {
            logger.error("Error getting required skills", e);
            throw e;
        }
    }

    /**
     * Helper method to get user ID from authentication
     */
    private Long getUserId(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            return ((UserPrincipal) authentication.getPrincipal()).getId();
        }
        return null;
    }
}