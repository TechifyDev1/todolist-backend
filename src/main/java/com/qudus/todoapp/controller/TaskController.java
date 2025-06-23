package com.qudus.todoapp.controller;

import java.util.List;
// import java.util.List;
// import java.util.Optional;
// import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ResponseEntity;

import com.qudus.todoapp.entity.Task;
import com.qudus.todoapp.entity.User;
import com.qudus.todoapp.repository.TaskRepository;
import com.qudus.todoapp.repository.UserRepository;

@RestController
@CrossOrigin(origins = { "http://localhost:3000", "https://todolist-frontend-ruby.vercel.app" })
@RequestMapping("/tasks")
public class TaskController {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskController(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/create")
    @CrossOrigin(origins = { "http://localhost:3000", "https://todolist-frontend-ruby.vercel.app" })
    public Task createTask(@RequestBody Task task, @RequestParam Long userId) {
        if (userId == null || userId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user Id");
        }
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task title is required");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found 😢"));
        task.setUser(user);
        return taskRepository.save(task);
    }

    @GetMapping
    @CrossOrigin(origins = { "http://localhost:3000", "https://todolist-frontend-ruby.vercel.app" })
    public Task getTaskById(@RequestParam Long taskId, @RequestParam Long userId) {
        if (taskId == null || taskId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid task Id");
        }
        if (userId == null || userId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user Id");
        }
        Task task = taskRepository.findTaskByIdAndUserId(taskId, userId);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        return task;
    }

    @GetMapping("/all")
    @CrossOrigin(origins = { "http://localhost:3000", "https://todolist-frontend-ruby.vercel.app" })
    public List<Task> getAllTasks(@RequestParam Long userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID is required");
        }
        if (userId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user ID");
        }
        boolean userExists = userRepository.existsById(userId);
        if (!userExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!!!");
        }
        return taskRepository.findTasksByUserId(userId);
    }

    @PutMapping
    @CrossOrigin(origins = { "http://localhost:3000", "https://todolist-frontend-ruby.vercel.app" })
    public Task updateTask(@RequestBody Task task, @RequestParam Long taskId, @RequestParam Long userId) {

        if (taskId == null || taskId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid task Id");
        }
        if (userId == null || userId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user Id");
        }
        Task existingTask = taskRepository.findTaskByIdAndUserId(taskId, userId);
        if (existingTask == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found for the given user");
        }
        if (task.getTitle() != null && !task.getTitle().isEmpty()) {
            existingTask.setTitle(task.getTitle());
        }
        if (task.getDescription() != null && !task.getDescription().isEmpty()) {
            existingTask.setDescription(task.getDescription());
        }
        if (task.isCompleted() != existingTask.isCompleted()) {
            existingTask.setCompleted(task.isCompleted());
        }
        if (task.getUser() != null) {
            User user = userRepository.findById(task.getUser().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            existingTask.setUser(user);
        }
        return taskRepository.save(existingTask);
    }

    @DeleteMapping
    @CrossOrigin(origins = { "http://localhost:3000", "https://todolist-frontend-ruby.vercel.app" })
    public void deleteTask(@RequestParam Long taskId, @RequestParam Long userId) {
        if (taskId == null || taskId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid task Id");
        }
        if (userId == null || userId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user Id");
        }
        Task task = taskRepository.findTaskByIdAndUserId(taskId, userId);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found for the given user");
        }
        taskRepository.delete(task);
    }

    @PutMapping("/complete")
    @CrossOrigin(origins = { "http://localhost:3000", "https://todolist-frontend-ruby.vercel.app" })
    public Task markTaskAsComplete(@RequestParam Long userId, @RequestParam Long taskId, @RequestBody Task task) {
        if (taskId == null || taskId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid task Id");
        }
        if (userId == null || userId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user Id");
        }
        Task existingTask = taskRepository.findTaskByIdAndUserId(taskId, userId);
        if (existingTask == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found for the given user");
        }
        existingTask.setCompleted(task.isCompleted());
        return taskRepository.save(existingTask);
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }


}
