package com.socialmedia.service;

import com.socialmedia.model.Comment;
import com.socialmedia.model.Post;
import com.socialmedia.model.User;
import com.socialmedia.repository.CommentRepository;
import com.socialmedia.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    // ---- Create Post ----
    public Post createPost(String content, String username) {
        User author = userService.getUserByUsername(username);
        Post post = new Post();
        post.setContent(content.trim());
        post.setAuthor(author);
        return postRepository.save(post);
    }

    // ---- Delete Post ----
    public void deletePost(Long postId, String username) {
        Post post = getPostById(postId);
        if (!post.getAuthor().getUsername().equals(username)) {
            throw new SecurityException("Not authorized to delete this post");
        }
        postRepository.delete(post);
    }

    // ---- Get Post ----
    @Transactional(readOnly = true)
    public Post getPostById(Long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));
    }

    // ---- Feed (posts from followed users + own) ----
    @Transactional(readOnly = true)
    public List<Post> getFeed(String username) {
        User user = userService.getUserByUsername(username);
        return postRepository.findFeedPostsByUserId(user.getId());
    }

    // ---- Explore (all posts) ----
    @Transactional(readOnly = true)
    public List<Post> getExplorePosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    // ---- User's posts ----
    @Transactional(readOnly = true)
    public List<Post> getUserPosts(String username) {
        User user = userService.getUserByUsername(username);
        return postRepository.findByAuthorOrderByCreatedAtDesc(user);
    }

    // ---- Like / Unlike ----
    public boolean toggleLike(Long postId, String username) {
        Post post = getPostById(postId);
        User user = userService.getUserByUsername(username);

        if (user.getLikes().contains(post)) {
            user.getLikes().remove(post);
            post.getLikedBy().remove(user);
            postRepository.save(post);
            return false; // unliked
        } else {
            user.getLikes().add(post);
            post.getLikedBy().add(user);
            postRepository.save(post);
            return true; // liked
        }
    }

    // ---- Add Comment ----
    public Comment addComment(Long postId, String content, String username) {
        Post post = getPostById(postId);
        User author = userService.getUserByUsername(username);

        Comment comment = new Comment();
        comment.setContent(content.trim());
        comment.setPost(post);
        comment.setAuthor(author);
        return commentRepository.save(comment);
    }

    // ---- Delete Comment ----
    public void deleteComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        if (!comment.getAuthor().getUsername().equals(username)) {
            throw new SecurityException("Not authorized to delete this comment");
        }
        commentRepository.delete(comment);
    }

    // ---- Search Posts ----
    @Transactional(readOnly = true)
    public List<Post> searchPosts(String query) {
        return postRepository.searchPosts(query);
    }
}
