package com.qudus.todoapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qudus.todoapp.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTasksByUserId(Long userId);
    Task findTaskByIdAndUserId(Long taskId, Long userId);
}
