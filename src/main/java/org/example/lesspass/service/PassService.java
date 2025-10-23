// language: java
package org.example.lesspass.service;

import org.example.lesspass.model.Pass;
import org.example.lesspass.repository.PassRepository;
import org.example.lesspass.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PassService {

    private static final Logger logger = LoggerFactory.getLogger(PassService.class);

    private final PassRepository passRepository;
    private final UserRepository userRepository;

    public PassService(PassRepository passRepository, UserRepository userRepository) {
        this.passRepository = passRepository;
        this.userRepository = userRepository;
    }

    // Método existente mejorado: primero intenta con findByUserUsername para evitar buscar el User manualmente
    public List<Pass> getPassesForUsername(String username) {
        if (username == null) {
            logger.debug("getPassesForUsername: username es null");
            return Collections.emptyList();
        }

        logger.debug("Buscando passes para username='{}'", username);

        List<Pass> passes = passRepository.findByUserUsername(username);
        if (passes != null && !passes.isEmpty()) {
            logger.debug("Encontradas {} passes usando findByUserUsername para '{}'", passes.size(), username);
            return passes;
        }

        // fallback: si no devuelve nada, intentamos buscar user y buscar por id (por compatibilidad)
        return userRepository.findByUsername(username)
                .map(user -> {
                    logger.debug("Usuario encontrado id={} nombre={}", user.getId(), user.getUsername());
                    List<Pass> byId = passRepository.findByUserId(user.getId());
                    logger.debug("Encontradas {} passes usando findByUserId para id={}", byId.size(), user.getId());
                    return byId;
                })
                .orElseGet(() -> {
                    logger.debug("Usuario no encontrado para username='{}'. Retornando lista vacía.", username);
                    return Collections.emptyList();
                });
    }
}
