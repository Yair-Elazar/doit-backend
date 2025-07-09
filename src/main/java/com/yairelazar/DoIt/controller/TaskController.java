package com.yairelazar.DoIt.controller;

import com.yairelazar.DoIt.dto.ShareTaskRequest;
import com.yairelazar.DoIt.model.Task;
import com.yairelazar.DoIt.model.User;
import com.yairelazar.DoIt.repository.TaskRepository;
import com.yairelazar.DoIt.repository.UserRepository;
import com.yairelazar.DoIt.security.CustomUserDetails;
import com.yairelazar.DoIt.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getUserTasks(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        System.out.println("===> getTasks called by user: " + user.getUsername());
        List<Task> tasks = taskService.getTasksByUser(user);
        System.out.println("===> Found tasks: " + tasks);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(tasks);
    }

    @GetMapping("/{id}")
    public Optional<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping
    public Task addTask(@RequestBody Task task, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        System.out.println("User id: " + user.getId());
        return taskService.addTask(task, user);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id,
                           @RequestBody Task updatedTask,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        updatedTask.setId(id);
        return taskService.updateTask(updatedTask, user);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        taskService.deleteTask(id, user);
    }

    @PatchMapping("/{id}")
    public Task partialUpdateTask(@PathVariable Long id,
                                  @RequestBody Map<String, Object> updates,
                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        return taskService.partialUpdate(id, updates, user);
    }
    @PostMapping("/share")
    public ResponseEntity<String> shareTask(@RequestBody ShareTaskRequest request,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            taskService.shareTask(request.getTaskId(), request.getUsernames(), userDetails.getUser());
            return ResponseEntity.ok("Task shared successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}