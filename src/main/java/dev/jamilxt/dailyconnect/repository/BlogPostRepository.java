package dev.jamilxt.dailyconnect.repository;

import dev.jamilxt.dailyconnect.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
}
