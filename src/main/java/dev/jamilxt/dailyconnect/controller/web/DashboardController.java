package dev.jamilxt.dailyconnect.controller.web;

import dev.jamilxt.dailyconnect.servie.WordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final WordService wordService;

    public DashboardController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("dailyWord", wordService.getDailyWord());
        model.addAttribute("userCount", 42);
        return "dashboard";
    }

    @GetMapping("/notifications")
    public String notifications() {
        return "notifications";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}