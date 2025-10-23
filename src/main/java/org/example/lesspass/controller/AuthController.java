// language: java
// File: src/main/java/org/example/lesspass/controller/AuthController.java
package org.example.lesspass.controller;

import org.example.lesspass.model.User;
import org.example.lesspass.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/signup")
    public String mostrarFormularioRegistro() {
        return "register";
    }

    @PostMapping("/signup")
    public String registrarUsuario(@RequestParam String username, @RequestParam String password, Model model) {
        if (userRepository.findAll().stream().anyMatch(u -> u.getUsername().equals(username))) {
            model.addAttribute("error", "El usuario ya existe");
            return "register";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setRole("ROLE_USER");
        userRepository.save(user);

        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }
}
