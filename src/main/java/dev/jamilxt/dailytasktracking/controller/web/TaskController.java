package dev.jamilxt.dailytasktracking.controller.web;

import dev.jamilxt.dailytasktracking.entity.Task;
import dev.jamilxt.dailytasktracking.servie.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;

    private static final DateTimeFormatter URL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

    @GetMapping("tasks")
    public String viewTasks(
            @RequestParam(value = "selectedDate", required = false) String selectedDate,
            Model model) {
        // Default to today if no date is selected
        LocalDate dateToShow = (selectedDate != null) ? LocalDate.parse(selectedDate) : LocalDate.now();

        // Fetch tasks for the selected date
        List<Task> tasks = taskService.getTasksByDate(dateToShow);

        // Format task dates for display
        List<Map<String, String>> formattedTasks = new ArrayList<>();
        for (Task task : tasks) {
            Map<String, String> taskMap = new HashMap<>();
            taskMap.put("date", task.getDate().format(DISPLAY_DATE_FORMATTER));
            taskMap.put("taskType", task.getTaskType());
            taskMap.put("taskDetails", task.getTaskDetails());
            taskMap.put("link", task.getLink());
            taskMap.put("remarks", task.getRemarks());
            formattedTasks.add(taskMap);
        }
        model.addAttribute("tasks", formattedTasks);

        // Create a new Task with the selected date pre-filled
        Task newTask = new Task();
        newTask.setDate(dateToShow);
        model.addAttribute("task", newTask);

        // List of task types for the dropdown
        List<String> taskTypes = Arrays.asList("Meeting", "Course", "Project", "Assignment", "Assessment", "Book", "Exercise", "Other");
        model.addAttribute("taskTypes", taskTypes);

        // Generate calendar days and partition into weeks
        YearMonth yearMonth = YearMonth.from(dateToShow);
        List<LocalDate> calendarDays = generateCalendarDays(yearMonth);
        List<List<Map<String, Object>>> calendarWeeks = partitionIntoWeeks(calendarDays, dateToShow);
        model.addAttribute("calendarWeeks", calendarWeeks);
        model.addAttribute("selectedDate", dateToShow.format(URL_DATE_FORMATTER));
        model.addAttribute("selectedDateDisplay", dateToShow.format(DISPLAY_DATE_FORMATTER));
        model.addAttribute("currentMonth", yearMonth.getMonth().toString() + " " + yearMonth.getYear());
        model.addAttribute("today", LocalDate.now().format(URL_DATE_FORMATTER));

        return "tasks";
    }

    @PostMapping("/addTask")
    public String addTask(@ModelAttribute Task task, @RequestParam("selectedDate") String selectedDate) {
        System.out.println("Received task: " + task);
        System.out.println("Selected Date from param: " + selectedDate);
        if (task.getDate() == null) {
            task.setDate(LocalDate.parse(selectedDate));
            System.out.println("Date was null, set to: " + task.getDate());
        } else {
            System.out.println("Date from form: " + task.getDate());
        }
        taskService.saveTask(task);
        return "redirect:tasks?selectedDate=" + selectedDate;
    }

    private List<LocalDate> generateCalendarDays(YearMonth yearMonth) {
        LocalDate firstOfMonth = yearMonth.atDay(1);
        LocalDate lastOfMonth = yearMonth.atEndOfMonth();

        // Determine the first day of the week (Sunday = 0, Monday = 1, etc.)
        DayOfWeek firstDayOfWeek = firstOfMonth.getDayOfWeek();
        int offset = firstDayOfWeek.getValue() % 7; // Adjust to start week on Sunday

        // Create a list to hold all days (including padding for the first week)
        List<LocalDate> days = new ArrayList<>();
        LocalDate previousMonth = firstOfMonth.minusDays(1);
        YearMonth previousYearMonth = YearMonth.from(previousMonth);

        // Add padding days from the previous month
        for (int i = offset - 1; i >= 0; i--) {
            days.add(previousYearMonth.atEndOfMonth().minusDays(i));
        }

        // Add days of the current month
        for (LocalDate date = firstOfMonth; !date.isAfter(lastOfMonth); date = date.plusDays(1)) {
            days.add(date);
        }

        // Add padding days for the next month
        int remainingDays = 42 - days.size(); // 6 weeks * 7 days
        for (int i = 1; i <= remainingDays; i++) {
            days.add(lastOfMonth.plusDays(i));
        }

        return days;
    }

    private List<List<Map<String, Object>>> partitionIntoWeeks(List<LocalDate> days, LocalDate selectedDate) {
        List<List<Map<String, Object>>> weeks = new ArrayList<>();
        for (int i = 0; i < days.size(); i += 7) {
            int end = Math.min(i + 7, days.size());
            List<LocalDate> weekDays = days.subList(i, end);
            List<Map<String, Object>> week = new ArrayList<>();
            for (LocalDate day : weekDays) {
                Map<String, Object> dayMap = new HashMap<>();
                dayMap.put("date", day);
                dayMap.put("dayOfMonth", day.getDayOfMonth());
                dayMap.put("formattedDate", day.format(URL_DATE_FORMATTER));
                dayMap.put("isSelected", day.equals(selectedDate));
                dayMap.put("isToday", day.equals(LocalDate.now()));
                week.add(dayMap);
            }
            weeks.add(week);
        }
        return weeks;
    }
}