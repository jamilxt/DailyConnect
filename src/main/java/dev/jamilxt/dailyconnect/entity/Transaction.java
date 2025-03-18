package dev.jamilxt.dailyconnect.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String description;
    private double amount;
    private String type; // "income" or "expense"
    private String category;
    private LocalDateTime timestamp;

    public Transaction() {
        this.timestamp = LocalDateTime.now();
    }

    public Transaction(String username, String description, double amount, String type, String category) {
        this.username = username;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.timestamp = LocalDateTime.now();
    }
}