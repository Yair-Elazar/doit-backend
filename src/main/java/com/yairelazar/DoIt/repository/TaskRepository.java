package com.yairelazar.DoIt.repository;

import com.yairelazar.DoIt.model.Task;
import com.yairelazar.DoIt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long>{

}
