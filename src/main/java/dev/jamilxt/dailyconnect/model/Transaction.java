package dev.jamilxt.dailyconnect.model;

public class Transaction {
    private String description;
    private double amount;
    private String type; // "income" or "expense"
    private String timestamp; // Optional, using current time for now

    public Transaction(String description, double amount, String type) {
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public String getTimestamp() { return timestamp; }
}
