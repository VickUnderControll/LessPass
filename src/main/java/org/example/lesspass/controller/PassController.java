package org.example.lesspass.controller;

import org.example.lesspass.model.Pass;
import org.example.lesspass.model.User;
import org.example.lesspass.repository.PassRepository;
import org.example.lesspass.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/pass")
public class PassController {

    private final PassRepository passRepository;
    private final UserRepository userRepository;

    public PassController(PassRepository passRepository, UserRepository userRepository) {
        this.passRepository = passRepository;
        this.userRepository = userRepository;
    }

    // Crear entrada (form POST desde el modal)
    @PostMapping("/add")
    @Transactional
    public String addPass(@RequestParam String url,
                          @RequestParam String usuario,
                          @RequestParam String contrasena,
                          Principal principal) {

        String username = principal.getName();
        Optional<User> ou = userRepository.findByUsername(username);
        if (ou.isEmpty()) {
            return "redirect:/?error"; // usuario no encontrado
        }

        User user = ou.get();
        Pass p = new Pass();
        p.setUser(user);
        p.setUrl(url);
        p.setUsuario(usuario);
        p.setContrasena(contrasena);
        passRepository.save(p);

        return "redirect:/"; // ajusta si tu home es /welcome
    }

    // Borrar entrada (form POST desde el modal de confirmación)
    @PostMapping("/delete")
    @Transactional
    public String deletePass(@RequestParam Long id, Principal principal) {
        Optional<Pass> op = passRepository.findById(id);
        if (op.isEmpty()) {
            return "redirect:/?error";
        }
        Pass p = op.get();
        if (!p.getUser().getUsername().equals(principal.getName())) {
            return "redirect:/?error=forbidden";
        }
        passRepository.delete(p);
        return "redirect:/";
    }

    // Revelar contraseña (AJAX GET). Devuelve 200 con la contraseña si eres el dueño, 403 si no.
    @GetMapping("/{id}/reveal")
    @ResponseBody
    public ResponseEntity<String> reveal(@PathVariable Long id, Principal principal) {
        Optional<Pass> op = passRepository.findById(id);
        if (op.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe");
        }
        Pass p = op.get();
        if (!p.getUser().getUsername().equals(principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        }
        return ResponseEntity.ok(p.getContrasena());
    }
}
