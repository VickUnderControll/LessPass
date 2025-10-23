package org.example.lesspass.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.example.lesspass.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
