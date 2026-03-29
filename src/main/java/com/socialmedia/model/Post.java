package com.socialmedia.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts")
@Data
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"likedBy", "comments"})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 500)
    @Column(nullable = false, length = 500)
    private String content;

    private String imageUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    // ---- RELATIONSHIPS ----

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @ManyToMany(mappedBy = "likes", fetch = FetchType.LAZY)
    private Set<User> likedBy = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("createdAt ASC")
    private List<Comment> comments = new ArrayList<>();

    // Helper methods
    public int getLikesCount() {
        return likedBy != null ? likedBy.size() : 0;
    }

    public int getCommentsCount() {
        return comments != null ? comments.size() : 0;
    }

    public boolean isLikedBy(User user) {
        return likedBy != null && likedBy.contains(user);
    }
}
