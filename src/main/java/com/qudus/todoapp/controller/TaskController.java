package com.qudus.todoapp.controller;

import java.util.List;
// import java.util.List;
// import java.util.Optional;
// import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found 😢"));
        task.setUser(user);
        return taskRepository.save(task);
    }

    @GetMapping
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
    
    /**
     * Retrieves all tasks associated with a specific user.
     *
     * @param userId the ID of the user whose tasks are to be retrieved; must not be
     *               null or less than or equal to zero
     * @return a list of {@link Task} objects belonging to the specified user
     * @throws ResponseStatusException if the userId is null, invalid, or the user
     *                                 does not exist
     */
    @GetMapping("/all")
    public List<Task> getAllTasks(@RequestParam Long userId) {
        System.out.println(userId);
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

    /**
     * Updates an existing task for a specific user.
     *
     * <p>
     * This endpoint allows updating the title, description, completion status, and
     * user of a task.
     * The task to be updated is identified by the provided {@code taskId} and
     * {@code userId} parameters.
     * Only non-null and non-empty fields in the request body will be updated.
     *
     * @param task   The {@link Task} object containing updated fields.
     * @param taskId The ID of the task to update. Must be a positive number.
     * @param userId The ID of the user who owns the task. Must be a positive
     *               number.
     * @return The updated {@link Task} object.
     * @throws ResponseStatusException if the taskId or userId is invalid, if the
     *                                 task is not found for the given user,
     *                                 or if the specified user does not exist.
     */
    @PutMapping
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


}
