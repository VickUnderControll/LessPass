// language: java
// File: src/main/java/org/example/lesspass/repository/PassRepository.java
package org.example.lesspass.repository;

import org.example.lesspass.model.Pass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PassRepository extends JpaRepository<Pass, Long> {

    List<Pass> findByUserId(Long userId);

    // Consulta explícita usando la relación: evita problemas con el método derivado
    @Query("SELECT p FROM Pass p WHERE p.user.username = :username")
    List<Pass> findByUserUsername(@Param("username") String username);

    // Alternativa por id de usuario (naming con guion bajo también válido)
    List<Pass> findByUser_Id(Long userId);
}
