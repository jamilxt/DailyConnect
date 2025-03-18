package dev.jamilxt.dailyconnect.controller.web;

import dev.jamilxt.dailyconnect.entity.BlogPost;
import dev.jamilxt.dailyconnect.repository.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
public class BlogController {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @GetMapping("/blog")
    public String blog(Authentication authentication, Model model) {
        List<BlogPost> blogPosts = blogPostRepository.findAll(); // Fetch all posts; can refine for user-specific later
        List<String> categories = Arrays.asList("Personal Finance", "Lifestyle", "Tips", "Reviews");
        model.addAttribute("blogPosts", blogPosts);
        model.addAttribute("categories", categories);
        return "blog";
    }

    @PostMapping("/blog")
    public String addBlogPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("category") String category,
            Authentication authentication) {
        BlogPost blogPost = new BlogPost(title, content, category);
        blogPostRepository.save(blogPost);
        return "redirect:/blog";
    }
}
