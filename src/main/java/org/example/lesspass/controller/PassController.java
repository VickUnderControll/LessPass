// java
package org.example.lesspass.controller;

import org.example.lesspass.model.Pass;
import org.example.lesspass.model.User;
import org.example.lesspass.repository.PassRepository;
import org.example.lesspass.repository.UserRepository;
import org.example.lesspass.service.EncryptionService;
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
    private final EncryptionService encryptionService;

    public PassController(PassRepository passRepository,
                          UserRepository userRepository,
                          EncryptionService encryptionService) {
        this.passRepository = passRepository;
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
    }

    @PostMapping("/add")
    @Transactional
    public String addPass(@RequestParam String url,
                          @RequestParam String usuario,
                          @RequestParam String contrasena,
                          Principal principal) {

        String username = principal.getName();
        Optional<User> ou = userRepository.findByUsername(username);
        if (ou.isEmpty()) {
            return "redirect:/?error";
        }

        User user = ou.get();
        Pass p = new Pass();
        p.setUser(user);
        p.setUrl(url);
        p.setUsuario(usuario);
        p.setContrasena(encryptionService.encrypt(contrasena));
        passRepository.save(p);

        return "redirect:/";
    }

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
        String plain;
        try {
            plain = encryptionService.decrypt(p.getContrasena());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al desencriptar");
        }
        return ResponseEntity.ok(plain);
    }
}
