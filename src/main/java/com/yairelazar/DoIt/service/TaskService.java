package com.yairelazar.DoIt.service;

import com.yairelazar.DoIt.model.Task;
import com.yairelazar.DoIt.model.User;
import com.yairelazar.DoIt.repository.TaskRepository;
import com.yairelazar.DoIt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

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
        List<Task> createdTasks = taskRepository.findByCreator(user);
        List<Task> sharedTasks = taskRepository.findBySharedWithContains(user);

        Set<Task> allTasks = new HashSet<>();
        allTasks.addAll(createdTasks);
        allTasks.addAll(sharedTasks);

        return new ArrayList<>(allTasks);
    }


    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task addTask(Task task, User userFromJwt) {
        // שליפה מחדש של ה-User כדי לקבל אובייקט Managed עם ID תקף
        User userFromDb = userRepository.findByUsername(userFromJwt.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setCreator(userFromDb);
        return taskRepository.save(task);
    }



    public Task updateTask(Task updatedTask, User user) {
        Optional<Task> existing = taskRepository.findById(updatedTask.getId());
        if (existing.isPresent() && existing.get().getCreator().getId().equals(user.getId())) {
            updatedTask.setCreator(user); // ודא שהבעלות לא משתנה
            return taskRepository.save(updatedTask);
        }
        throw new RuntimeException("Task not found or not authorized");
    }
    public Task partialUpdate(Long id, Map<String, Object> updates, User user) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            if (!task.getCreator().getId().equals(user.getId())) {
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

        if (!task.getCreator().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to delete this task");
        }

        taskRepository.delete(task);
    }
    public void shareTask(Long taskId, List<String> usernames, User currentUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getCreator().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not authorized to share this task");
        }

        List<User> usersToShareWith = userRepository.findByUsernameIn(usernames);
        if (usersToShareWith.isEmpty()) {
            throw new RuntimeException("No valid users found");
        }

        task.getSharedWith().addAll(usersToShareWith);
        taskRepository.save(task);
    }



}