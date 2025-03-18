package dev.jamilxt.dailyconnect.repository;

import dev.jamilxt.dailyconnect.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
