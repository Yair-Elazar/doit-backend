package com.yairelazar.DoIt.service;

import com.yairelazar.DoIt.model.Task;
import com.yairelazar.DoIt.model.User;
import com.yairelazar.DoIt.repository.TaskRepository;
import com.yairelazar.DoIt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Task> getTasksByUser(User user) {
        System.out.println("Fetching tasks for user: " + user.getUsername());
        List<Task> tasks = taskRepository.findByUser(user);
        System.out.println("Found tasks: " + tasks);
        return tasks;
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task addTask(Task task, User user) {
        task.setUser(user);
        return taskRepository.save(task);
    }

    public Task updateTask(Task updatedTask, User user) {
        Optional<Task> existing = taskRepository.findById(updatedTask.getId());
        if (existing.isPresent() && existing.get().getUser().getId().equals(user.getId())) {
            updatedTask.setUser(user); // ודא שהבעלות לא משתנה
            return taskRepository.save(updatedTask);
        }
        throw new RuntimeException("Task not found or not authorized");
    }
    public Task partialUpdate(Long id, Map<String, Object> updates, User user) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            if (!task.getUser().getId().equals(user.getId())) {
                throw new AccessDeniedException("Not authorized");
            }

            if (updates.containsKey("title")) {
                task.setTitle((String) updates.get("title"));
            }
            if (updates.containsKey("description")) {
                task.setDescription((String) updates.get("description"));
            }
            if (updates.containsKey("dueDate")) {
                task.setDueDate(LocalDate.parse((String) updates.get("dueDate")));
            }
            if (updates.containsKey("category")) {
                task.setCategory((String) updates.get("category"));
            }
            if (updates.containsKey("completed")) {
                task.setCompleted((Boolean) updates.get("completed"));
            }

            return taskRepository.save(task);
        } else {
            throw new RuntimeException("Task not found");
        }
    }


    public void deleteTask(Long id, User user) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to delete this task");
        }

        taskRepository.delete(task);
    }

}