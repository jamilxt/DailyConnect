package dev.jamilxt.dailyconnect.servie;

import dev.jamilxt.dailyconnect.entity.Task;
import dev.jamilxt.dailyconnect.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByDate(LocalDate date) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getDate().equals(date))
                .toList();
    }

    public void saveTask(Task task) {
        taskRepository.save(task);
    }
}