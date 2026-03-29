package com.socialmedia.repository;

import com.socialmedia.model.Post;
import com.socialmedia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Posts by a specific user, newest first
    List<Post> findByAuthorOrderByCreatedAtDesc(User author);

    // Feed: posts from users that the given user follows + own posts
    @Query("SELECT p FROM Post p WHERE p.author IN " +
           "(SELECT f FROM User u JOIN u.following f WHERE u.id = :userId) " +
           "OR p.author.id = :userId " +
           "ORDER BY p.createdAt DESC")
    List<Post> findFeedPostsByUserId(@Param("userId") Long userId);

    // All posts ordered by newest (explore/global feed)
    List<Post> findAllByOrderByCreatedAtDesc();

    // Search posts by content
    @Query("SELECT p FROM Post p WHERE LOWER(p.content) LIKE LOWER(CONCAT('%', :query, '%')) ORDER BY p.createdAt DESC")
    List<Post> searchPosts(@Param("query") String query);
}
