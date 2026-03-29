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
public class FeedController {

    private final PostService postService;
    private final UserService userService;

    // ---- Feed ----
    @GetMapping("/feed")
    public String feed(Model model, Principal principal) {
        String username = principal.getName();
        User currentUser = userService.getUserByUsername(username);
        List<Post> posts = postService.getFeed(username);
        List<User> suggestions = userService.getSuggestedUsers(username);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("posts", posts);
        model.addAttribute("suggestions", suggestions);
        return "feed";
    }

    // ---- Explore ----
    @GetMapping("/explore")
    public String explore(@RequestParam(required = false) String search,
                          Model model, Principal principal) {
        List<Post> posts;
        if (search != null && !search.trim().isEmpty()) {
            posts = postService.searchPosts(search.trim());
            model.addAttribute("searchQuery", search);
        } else {
            posts = postService.getExplorePosts();
        }

        if (principal != null) {
            User currentUser = userService.getUserByUsername(principal.getName());
            model.addAttribute("currentUser", currentUser);
        }
        model.addAttribute("posts", posts);
        return "explore";
    }

    // ---- Create Post ----
    @PostMapping("/post/create")
    public String createPost(@RequestParam String content,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {
        if (content == null || content.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Post cannot be empty!");
            return "redirect:/feed";
        }
        if (content.length() > 500) {
            redirectAttributes.addFlashAttribute("error", "Post is too long (max 500 characters)!");
            return "redirect:/feed";
        }
        try {
            postService.createPost(content, principal.getName());
            redirectAttributes.addFlashAttribute("success", "Post created!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Could not create post: " + e.getMessage());
        }
        return "redirect:/feed";
    }

    // ---- Delete Post ----
    @PostMapping("/post/{id}/delete")
    public String deletePost(@PathVariable Long id,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {
        try {
            postService.deletePost(id, principal.getName());
            redirectAttributes.addFlashAttribute("success", "Post deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Could not delete post.");
        }
        return "redirect:/feed";
    }

    // ---- Like / Unlike Post ----
    @PostMapping("/post/{id}/like")
    public String likePost(@PathVariable Long id,
                           @RequestParam(defaultValue = "/feed") String returnTo,
                           Principal principal) {
        postService.toggleLike(id, principal.getName());
        return "redirect:" + returnTo;
    }

    // ---- View Single Post + Comments ----
    @GetMapping("/post/{id}")
    public String viewPost(@PathVariable Long id, Model model, Principal principal) {
        Post post = postService.getPostById(id);
        User currentUser = userService.getUserByUsername(principal.getName());
        model.addAttribute("post", post);
        model.addAttribute("currentUser", currentUser);
        return "post-detail";
    }

    // ---- Add Comment ----
    @PostMapping("/post/{id}/comment")
    public String addComment(@PathVariable Long id,
                             @RequestParam String content,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {
        if (content == null || content.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Comment cannot be empty!");
            return "redirect:/post/" + id;
        }
        postService.addComment(id, content, principal.getName());
        return "redirect:/post/" + id;
    }

    // ---- Delete Comment ----
    @PostMapping("/comment/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId,
                                @RequestParam Long postId,
                                Principal principal) {
        postService.deleteComment(commentId, principal.getName());
        return "redirect:/post/" + postId;
    }
}
