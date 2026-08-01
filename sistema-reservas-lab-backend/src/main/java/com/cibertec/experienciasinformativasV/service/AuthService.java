package com.cibertec.experienciasinformativasV.service;

import com.cibertec.experienciasinformativasV.dto.LoginRequest;
import com.cibertec.experienciasinformativasV.dto.RegistroRequest;
import com.cibertec.experienciasinformativasV.entity.Usuario;
import com.cibertec.experienciasinformativasV.enums.Rol;
import com.cibertec.experienciasinformativasV.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario login(LoginRequest request) {

        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

        if (!usuario.getEstado()) {
            throw new RuntimeException("Usuario inactivo");
        }

        if (!usuario.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        return usuario;
    }

    public Usuario registrarAlumno(RegistroRequest request) {

        if (request.getNombres() == null || request.getNombres().isBlank() ||
            request.getApellidos() == null || request.getApellidos().isBlank() ||
            request.getCorreo() == null || request.getCorreo().isBlank() ||
            request.getPassword() == null || request.getPassword().isBlank()) {

            throw new RuntimeException("Todos los campos son obligatorios");
        }

        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        Usuario nuevo = new Usuario();
        nuevo.setNombres(request.getNombres());
        nuevo.setApellidos(request.getApellidos());
        nuevo.setCorreo(request.getCorreo());
        nuevo.setPassword(request.getPassword());
        nuevo.setRol(Rol.ALUMNO);
        nuevo.setEstado(true);

        return usuarioRepository.save(nuevo);
    }
}