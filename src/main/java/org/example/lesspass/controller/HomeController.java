// java
// src/main/java/org/example/lesspass/controller/HomeController.java
package org.example.lesspass.controller;

import org.example.lesspass.model.Pass;
import org.example.lesspass.repository.PassRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final PassRepository passRepository;

    public HomeController(PassRepository passRepository) {
        this.passRepository = passRepository;
    }

    @GetMapping({"/", "/welcome"})
    public String welcome(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        List<Pass> passes = passRepository.findAll().stream()
                .filter(p -> p.getUser() != null && username.equals(p.getUser().getUsername()))
                .collect(Collectors.toList());

        model.addAttribute("username", username);
        model.addAttribute("passes", passes);
        return "welcome";
    }
}
