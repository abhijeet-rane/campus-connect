package com.campusconnect.repository;

import com.campusconnect.entity.User;
import com.campusconnect.entity.UserRole;
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
 * Repository interface for User entity operations
 * 
 * @author Campus Connect Team
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email address
     * @param email the email address
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by email and active status
     * @param email the email address
     * @param isActive the active status
     * @return Optional containing the user if found
     */
    Optional<User> findByEmailAndIsActive(String email, Boolean isActive);

    /**
     * Check if user exists by email
     * @param email the email address
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Find users by role
     * @param role the user role
     * @param pageable pagination information
     * @return Page of users with the specified role
     */
    Page<User> findByRole(UserRole role, Pageable pageable);

    /**
     * Find users by department
     * @param department the department name
     * @param pageable pagination information
     * @return Page of users in the specified department
     */
    Page<User> findByDepartment(String department, Pageable pageable);

    /**
     * Find users by role and department
     * @param role the user role
     * @param department the department name
     * @param pageable pagination information
     * @return Page of users matching the criteria
     */
    Page<User> findByRoleAndDepartment(UserRole role, String department, Pageable pageable);

    /**
     * Find active users
     * @param isActive the active status
     * @param pageable pagination information
     * @return Page of active users
     */
    Page<User> findByIsActive(Boolean isActive, Pageable pageable);

    /**
     * Search users by name or email
     * @param searchTerm the search term
     * @param pageable pagination information
     * @return Page of users matching the search criteria
     */
    @Query("SELECT u FROM User u WHERE " +
           "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "u.isActive = true")
    Page<User> searchUsers(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find users who joined recently
     * @param since the date since when to look for new users
     * @param pageable pagination information
     * @return Page of recently joined users
     */
    Page<User> findByCreatedAtAfterAndIsActive(LocalDateTime since, Boolean isActive, Pageable pageable);

    /**
     * Find users by academic year
     * @param academicYear the academic year
     * @param pageable pagination information
     * @return Page of users in the specified academic year
     */
    Page<User> findByAcademicYear(String academicYear, Pageable pageable);

    /**
     * Count users by role
     * @param role the user role
     * @return count of users with the specified role
     */
    long countByRole(UserRole role);

    /**
     * Count active users
     * @param isActive the active status
     * @return count of active users
     */
    long countByIsActive(Boolean isActive);

    /**
     * Update user's last login time
     * @param userId the user ID
     * @param lastLogin the last login timestamp
     */
    @Modifying
    @Query("UPDATE User u SET u.lastLogin = :lastLogin WHERE u.id = :userId")
    void updateLastLogin(@Param("userId") Long userId, @Param("lastLogin") LocalDateTime lastLogin);

    /**
     * Find users with most projects
     * @param limit the maximum number of results
     * @return List of users ordered by project count
     */
    @Query("SELECT u FROM User u LEFT JOIN u.ownedProjects p " +
           "WHERE u.isActive = true " +
           "GROUP BY u " +
           "ORDER BY COUNT(p) DESC")
    List<User> findTopProjectCreators(Pageable pageable);

    /**
     * Find users with most event registrations
     * @param limit the maximum number of results
     * @return List of users ordered by event registration count
     */
    @Query("SELECT u FROM User u LEFT JOIN u.eventRegistrations er " +
           "WHERE u.isActive = true " +
           "GROUP BY u " +
           "ORDER BY COUNT(er) DESC")
    List<User> findMostActiveEventParticipants(Pageable pageable);

    /**
     * Find all distinct departments
     * @return List of department names
     */
    @Query("SELECT DISTINCT u.department FROM User u WHERE u.department IS NOT NULL AND u.isActive = true")
    List<String> findAllDepartments();

    /**
     * Find all distinct academic years
     * @return List of academic years
     */
    @Query("SELECT DISTINCT u.academicYear FROM User u WHERE u.academicYear IS NOT NULL AND u.isActive = true")
    List<String> findAllAcademicYears();
}