package com.socialmedia.controller;

import com.socialmedia.model.Post;
import com.socialmedia.model.User;
import com.socialmedia.service.PostService;
import com.socialmedia.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final PostService postService;

    // View profile by username
    @GetMapping("/{username}")
    public String viewProfile(@PathVariable String username,
                              Model model, Principal principal) {
        User profileUser = userService.getUserByUsername(username);
        User currentUser = userService.getUserByUsername(principal.getName());
        List<Post> posts = postService.getUserPosts(username);

        model.addAttribute("profileUser", profileUser);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("posts", posts);
        model.addAttribute("isOwnProfile", username.equals(principal.getName()));
        model.addAttribute("isFollowing", currentUser.isFollowing(profileUser));
        return "profile";
    }

    // Edit profile page
    @GetMapping("/edit")
    public String editProfilePage(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        model.addAttribute("user", user);
        return "edit-profile";
    }

    // Save profile edits
    @PostMapping("/edit")
    public String editProfile(@RequestParam String fullName,
                              @RequestParam String bio,
                              @RequestParam String location,
                              @RequestParam String website,
                              Principal principal,
                              RedirectAttributes redirectAttributes) {
        userService.updateProfile(principal.getName(), fullName, bio, location, website);
        redirectAttributes.addFlashAttribute("success", "Profile updated!");
        return "redirect:/profile/" + principal.getName();
    }

    // Follow / Unfollow
    @PostMapping("/{userId}/follow")
    public String toggleFollow(@PathVariable Long userId,
                               @RequestParam(defaultValue = "") String returnTo,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        try {
            boolean followed = userService.toggleFollow(principal.getName(), userId);
            redirectAttributes.addFlashAttribute("success",
                followed ? "You are now following this user." : "Unfollowed.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        if (!returnTo.isEmpty()) return "redirect:" + returnTo;
        return "redirect:/profile/" + userService.getUserById(userId).getUsername();
    }

    // Search users
    @GetMapping("/search")
    public String searchUsers(@RequestParam String q, Model model, Principal principal) {
        List<User> users = userService.searchUsers(q);
        User currentUser = userService.getUserByUsername(principal.getName());
        model.addAttribute("users", users);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("searchQuery", q);
        return "search-users";
    }
}
