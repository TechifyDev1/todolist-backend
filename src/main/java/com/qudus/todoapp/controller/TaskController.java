package com.qudus.todoapp.controller;

// import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qudus.todoapp.entity.Task;
import com.qudus.todoapp.entity.User;
import com.qudus.todoapp.repository.TaskRepository;
import com.qudus.todoapp.repository.UserRepository;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskController(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public Task createTask(@RequestBody Task task, @RequestParam Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        task.setUser(user);
        return taskRepository.save(task);
    }

    @GetMapping
    public Optional<Task> getAllTasks(@RequestParam Long id) {
        return taskRepository.findById(id);
    }


}
