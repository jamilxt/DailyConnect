package dev.jamilxt.dailytasktracking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/")
    public String viewTasks(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        model.addAttribute("task", new Task());

        // List of task types for the dropdown
        List<String> taskTypes = Arrays.asList("Meeting", "Course", "Project", "Assignment", "Assessment", "Book", "Exercise", "Other");
        model.addAttribute("taskTypes", taskTypes);

        return "index";
    }

    @PostMapping("/addTask")
    public String addTask(@ModelAttribute Task task) {
        taskService.saveTask(task);
        return "redirect:/";
    }
}