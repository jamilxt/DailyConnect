package dev.jamilxt.dailyconnect.repository;

import dev.jamilxt.dailyconnect.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Optional<Portfolio> findByUsername(String username);
}
