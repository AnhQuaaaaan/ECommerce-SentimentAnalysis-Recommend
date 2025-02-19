package com.example.doan.Repository;

import com.example.doan.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findUserByUsernameAndPassword(String username, String password);
    User findUserByUsername(String username);
}
