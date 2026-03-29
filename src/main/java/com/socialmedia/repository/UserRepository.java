package com.socialmedia.repository;

import com.socialmedia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // Search users by username or fullName
    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<User> searchUsers(@Param("query") String query);

    // Get suggested users (not followed by currentUser, excluding self)
    @Query("SELECT u FROM User u WHERE u.id != :userId AND u NOT IN " +
           "(SELECT f FROM User me JOIN me.following f WHERE me.id = :userId) " +
           "ORDER BY SIZE(u.followers) DESC")
    List<User> findSuggestedUsers(@Param("userId") Long userId);
}
