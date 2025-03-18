package dev.jamilxt.dailyconnect.controller.web;

import dev.jamilxt.dailyconnect.entity.Transaction;
import dev.jamilxt.dailyconnect.repository.TransactionRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class FinanceController {

    private final TransactionRepository transactionRepository;
    private final ConcurrentHashMap<String, Double> userBudgets = new ConcurrentHashMap<>();

    public FinanceController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/finance")
    public String financePage(Authentication authentication, Model model) {
        String username = authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())
                ? authentication.getName() : "Guest";
        List<Transaction> transactions = transactionRepository.findByUsername(username);
        double balance = calculateBalance(transactions);
        double monthlyExpenses = calculateMonthlyExpenses(transactions);
        double budget = userBudgets.getOrDefault(username, 0.0);
        List<String> categories = Arrays.asList("Salary", "Food", "Bills", "Entertainment", "Other");

        model.addAttribute("currentUser", username);
        model.addAttribute("transactions", transactions);
        model.addAttribute("balance", balance);
        model.addAttribute("monthlyExpenses", monthlyExpenses);
        model.addAttribute("budget", budget);
        model.addAttribute("categories", categories);
        return "finance";
    }

    @PostMapping("/finance")
    public String addTransaction(
            @RequestParam("description") String description,
            @RequestParam("amount") double amount,
            @RequestParam("type") String type,
            @RequestParam("category") String category,
            Authentication authentication) {
        String username = authentication.getName();
        Transaction transaction = new Transaction(username, description, amount, type.toLowerCase(), category); // Force lowercase
        transactionRepository.save(transaction);
        return "redirect:/finance";
    }

    @PostMapping("/finance/budget")
    public String setBudget(@RequestParam("budget") double budget, Authentication authentication) {
        String username = authentication.getName();
        userBudgets.put(username, budget);
        return "redirect:/finance";
    }

    @GetMapping("/finance/export")
    public void exportTransactions(Authentication authentication, HttpServletResponse response) throws IOException {
        String username = authentication.getName();
        List<Transaction> transactions = transactionRepository.findByUsername(username);

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"transactions.csv\"");

        StringBuilder csv = new StringBuilder("Description,Amount,Type,Category,Timestamp\n");
        for (Transaction t : transactions) {
            csv.append(String.format("\"%s\",%.2f,%s,%s,%s\n",
                    t.getDescription(), t.getAmount(), t.getType(), t.getCategory(), t.getTimestamp()));
        }

        response.getWriter().write(csv.toString());
    }

    private double calculateBalance(List<Transaction> transactions) {
        return transactions.stream()
                .mapToDouble(t -> "income".equals(t.getType()) ? t.getAmount() : -t.getAmount())
                .sum();
    }

    private double calculateMonthlyExpenses(List<Transaction> transactions) {
        LocalDateTime now = LocalDateTime.now();
        return transactions.stream()
                .filter(t -> "expense".equals(t.getType()) && t.getTimestamp().getMonth() == now.getMonth())
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
}