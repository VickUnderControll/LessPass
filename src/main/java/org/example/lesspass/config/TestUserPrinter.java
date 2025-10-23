package org.example.lesspass.config;

import org.example.lesspass.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestUserPrinter {

    @Bean
    public CommandLineRunner printUsers(UserRepository userRepository) {
        return args -> {
            System.out.println("===== USUARIOS EN BASE DE DATOS =====");
            userRepository.findAll().forEach(u ->
                    System.out.println("Username: " + u.getUsername() +
                            " | Role: " + u.getRole() +
                            " | Password: " + u.getPassword()));
        };
    }
}