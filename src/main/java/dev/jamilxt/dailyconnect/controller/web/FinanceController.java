package dev.jamilxt.dailyconnect.controller.web;

import dev.jamilxt.dailyconnect.model.Transaction;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class FinanceController {

    // In-memory storage for simplicity (replace with DB later)
    private final ConcurrentHashMap<String, List<Transaction>> userTransactions = new ConcurrentHashMap<>();

    @GetMapping("/finance")
    public String financePage(Authentication authentication, Model model) {
        String username = authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())
                ? authentication.getName() : "Guest";
        List<Transaction> transactions = userTransactions.getOrDefault(username, new ArrayList<>());
        double balance = calculateBalance(transactions);

        model.addAttribute("currentUser", username);
        model.addAttribute("transactions", transactions);
        model.addAttribute("balance", balance);
        return "finance";
    }

    @PostMapping("/finance")
    public String addTransaction(
            @RequestParam("description") String description,
            @RequestParam("amount") double amount,
            @RequestParam("type") String type, // "income" or "expense"
            Authentication authentication) {
        String username = authentication.getName();
        Transaction transaction = new Transaction(description, amount, type);
        userTransactions.computeIfAbsent(username, k -> new ArrayList<>()).add(transaction);
        return "redirect:/finance";
    }

    private double calculateBalance(List<Transaction> transactions) {
        return transactions.stream()
                .mapToDouble(t -> "income".equals(t.getType()) ? t.getAmount() : -t.getAmount())
                .sum();
    }
}
