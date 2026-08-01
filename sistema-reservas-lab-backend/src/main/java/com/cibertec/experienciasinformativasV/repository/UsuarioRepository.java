package com.cibertec.experienciasinformativasV.repository;

import com.cibertec.experienciasinformativasV.entity.Usuario;
import com.cibertec.experienciasinformativasV.enums.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    List<Usuario> findByRolAndEstadoTrue(Rol rol);

    long countByRolAndEstadoTrue(Rol rol);
    
    List<Usuario> findByRolAndEstadoTrueAndNombresContainingIgnoreCase(
            Rol rol,
            String nombres
    );
    
    List<Usuario> findByRolAndEstadoTrueAndCorreoContainingIgnoreCase(
            Rol rol,
            String correo
    );
}