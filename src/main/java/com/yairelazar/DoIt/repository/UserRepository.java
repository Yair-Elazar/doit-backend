package com.yairelazar.DoIt.repository;
import com.yairelazar.DoIt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long>{

    User findByUsername(String username);
}
