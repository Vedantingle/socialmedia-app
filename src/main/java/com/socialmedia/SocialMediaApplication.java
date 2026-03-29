package com.socialmedia;

import com.socialmedia.model.User;
import com.socialmedia.model.Post;
import com.socialmedia.repository.UserRepository;
import com.socialmedia.repository.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
public class SocialMediaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialMediaApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("  SocialMedia App Started!");
        System.out.println("  URL: http://localhost:8080");
        System.out.println("  H2 Console: http://localhost:8080/h2-console");
        System.out.println("========================================\n");
    }

    // Seed some demo data on startup
    @Bean
    CommandLineRunner seedData(UserRepository userRepo, PostRepository postRepo, PasswordEncoder encoder) {
        return args -> {
            if (userRepo.count() == 0) {
                // Create demo users
                User alice = new User();
                alice.setUsername("alice");
                alice.setEmail("alice@example.com");
                alice.setPassword(encoder.encode("password123"));
                alice.setFullName("Alice Johnson");
                alice.setBio("Hello! I love coding and coffee ☕");
                alice.setProfilePicture("https://api.dicebear.com/7.x/avataaars/svg?seed=alice");
                alice.setJoinedAt(LocalDateTime.now().minusDays(30));
                userRepo.save(alice);

                User bob = new User();
                bob.setUsername("bob");
                bob.setEmail("bob@example.com");
                bob.setPassword(encoder.encode("password123"));
                bob.setFullName("Bob Smith");
                bob.setBio("Developer | Gamer | Music lover 🎮");
                bob.setProfilePicture("https://api.dicebear.com/7.x/avataaars/svg?seed=bob");
                bob.setJoinedAt(LocalDateTime.now().minusDays(20));
                userRepo.save(bob);

                User charlie = new User();
                charlie.setUsername("charlie");
                charlie.setEmail("charlie@example.com");
                charlie.setPassword(encoder.encode("password123"));
                charlie.setFullName("Charlie Davis");
                charlie.setBio("Photographer 📷 | Traveler ✈️");
                charlie.setProfilePicture("https://api.dicebear.com/7.x/avataaars/svg?seed=charlie");
                charlie.setJoinedAt(LocalDateTime.now().minusDays(10));
                userRepo.save(charlie);

                // Alice follows Bob and Charlie
                alice.getFollowing().add(bob);
                alice.getFollowing().add(charlie);
                bob.getFollowers().add(alice);
                charlie.getFollowers().add(alice);
                userRepo.save(alice);
                userRepo.save(bob);
                userRepo.save(charlie);

                // Create demo posts
                Post p1 = new Post();
                p1.setContent("Just started learning Spring Boot! 🚀 This framework is amazing for building web apps quickly. #Java #SpringBoot");
                p1.setAuthor(alice);
                p1.setCreatedAt(LocalDateTime.now().minusHours(5));
                postRepo.save(p1);

                Post p2 = new Post();
                p2.setContent("Finally finished my college project! Built a full-stack social media app with Java. Super proud of this one 💪 #Programming #CollegeLife");
                p2.setAuthor(bob);
                p2.setCreatedAt(LocalDateTime.now().minusHours(3));
                postRepo.save(p2);

                Post p3 = new Post();
                p3.setContent("Morning coffee + coding = perfect combo ☕💻 Working on some exciting new features today!");
                p3.setAuthor(charlie);
                p3.setCreatedAt(LocalDateTime.now().minusHours(1));
                postRepo.save(p3);

                Post p4 = new Post();
                p4.setContent("Pro tip: Always comment your code! Future you will thank present you 😄 #CodingTips #DeveloperLife");
                p4.setAuthor(alice);
                p4.setCreatedAt(LocalDateTime.now().minusMinutes(30));
                postRepo.save(p4);

                System.out.println("✅ Demo data seeded successfully!");
                System.out.println("   Login with: alice/password123, bob/password123, charlie/password123");
            }
        };
    }
}
