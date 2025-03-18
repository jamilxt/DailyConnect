package dev.jamilxt.dailyconnect.repository;

import dev.jamilxt.dailyconnect.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUsername(String username);
}
