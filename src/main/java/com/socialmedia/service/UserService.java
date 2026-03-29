package com.socialmedia.service;

import com.socialmedia.model.User;
import com.socialmedia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ---- Spring Security UserDetailsService ----
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .roles("USER")
            .build();
    }

    // ---- Registration ----
    public User register(String username, String email, String password, String fullName) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already taken: " + username);
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(fullName);
        user.setProfilePicture("https://api.dicebear.com/7.x/avataaars/svg?seed=" + username);
        return userRepository.save(user);
    }

    // ---- Get user ----
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    // ---- Update profile ----
    public User updateProfile(String username, String fullName, String bio, String location, String website) {
        User user = getUserByUsername(username);
        if (fullName != null) user.setFullName(fullName);
        if (bio != null) user.setBio(bio);
        if (location != null) user.setLocation(location);
        if (website != null) user.setWebsite(website);
        return userRepository.save(user);
    }

    // ---- Follow / Unfollow ----
    public boolean toggleFollow(String currentUsername, Long targetUserId) {
        User currentUser = getUserByUsername(currentUsername);
        User targetUser = getUserById(targetUserId);

        if (currentUser.getId().equals(targetUserId)) {
            throw new IllegalArgumentException("Cannot follow yourself");
        }

        if (currentUser.getFollowing().contains(targetUser)) {
            currentUser.getFollowing().remove(targetUser);
            targetUser.getFollowers().remove(currentUser);
            userRepository.save(currentUser);
            userRepository.save(targetUser);
            return false; // unfollowed
        } else {
            currentUser.getFollowing().add(targetUser);
            targetUser.getFollowers().add(currentUser);
            userRepository.save(currentUser);
            userRepository.save(targetUser);
            return true; // followed
        }
    }

    // ---- Search ----
    @Transactional(readOnly = true)
    public List<User> searchUsers(String query) {
        return userRepository.searchUsers(query);
    }

    // ---- Suggestions ----
    @Transactional(readOnly = true)
    public List<User> getSuggestedUsers(String currentUsername) {
        User user = getUserByUsername(currentUsername);
        List<User> suggestions = userRepository.findSuggestedUsers(user.getId());
        return suggestions.stream().limit(5).toList();
    }
}
