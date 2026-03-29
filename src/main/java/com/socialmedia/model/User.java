package com.socialmedia.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
@Table(name = "users")
@Data
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"followers", "following", "posts", "likes"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 20)
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Size(max = 50)
    private String fullName;

    @Size(max = 160)
    private String bio;

    private String profilePicture;

    private String location;

    private String website;

    @Column(nullable = false)
    private LocalDateTime joinedAt = LocalDateTime.now();

    private boolean active = true;

    // ---- RELATIONSHIPS ----

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("createdAt DESC")
    private List<Post> posts = new ArrayList<>();

    // Who this user follows
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_follows",
        joinColumns = @JoinColumn(name = "follower_id"),
        inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    private Set<User> following = new HashSet<>();

    // Who follows this user
    @ManyToMany(mappedBy = "following", fetch = FetchType.LAZY)
    private Set<User> followers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "post_likes",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> likes = new HashSet<>();

    // Helper methods
    public int getFollowersCount() {
        return followers != null ? followers.size() : 0;
    }

    public int getFollowingCount() {
        return following != null ? following.size() : 0;
    }

    public int getPostsCount() {
        return posts != null ? posts.size() : 0;
    }

    public boolean isFollowing(User other) {
        return following != null && following.contains(other);
    }
}
