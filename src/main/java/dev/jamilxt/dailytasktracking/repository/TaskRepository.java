package dev.jamilxt.dailytasktracking.repository;

import dev.jamilxt.dailytasktracking.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
