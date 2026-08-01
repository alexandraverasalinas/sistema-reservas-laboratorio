package com.cibertec.experienciasinformativasV.controller;

import com.cibertec.experienciasinformativasV.dto.AuthResponse;
import com.cibertec.experienciasinformativasV.dto.LoginRequest;
import com.cibertec.experienciasinformativasV.dto.RegistroRequest;
import com.cibertec.experienciasinformativasV.entity.Usuario;
import com.cibertec.experienciasinformativasV.service.AuthService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        Usuario usuario = authService.login(request);
        return new AuthResponse(usuario);
    }

    @PostMapping("/registro")
    public AuthResponse registro(@RequestBody RegistroRequest request) {
        Usuario nuevo = authService.registrarAlumno(request);
        return new AuthResponse(nuevo);
    }
}