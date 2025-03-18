package dev.jamilxt.dailyconnect.servie;

import dev.jamilxt.dailyconnect.entity.Portfolio;
import dev.jamilxt.dailyconnect.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    public Optional<Portfolio> getPortfolioByUsername(String username) {
        return portfolioRepository.findByUsername(username);
    }

    public Portfolio savePortfolio(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }
}