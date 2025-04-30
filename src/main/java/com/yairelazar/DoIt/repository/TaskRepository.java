package com.yairelazar.DoIt.repository;

import com.yairelazar.DoIt.model.Task;
import com.yairelazar.DoIt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>{

    List<Task> findByUser(User user);
}