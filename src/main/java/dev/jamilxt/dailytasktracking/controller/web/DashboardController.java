package dev.jamilxt.dailytasktracking.controller.web;

import dev.jamilxt.dailytasktracking.servie.WordService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final WordService wordService;

    public DashboardController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("dailyWord", wordService.getDailyWord());
        model.addAttribute("userCount", 42);
        return "dashboard";
    }

    @GetMapping("/notifications")
    public String notifications() {
        return "notifications";
    }

    @GetMapping("/chat")
    public String chat() {
        return "chat";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}