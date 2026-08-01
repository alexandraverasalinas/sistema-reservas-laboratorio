package com.cibertec.experienciasinformativasV.controller;

import com.cibertec.experienciasinformativasV.dto.PerfilRequest;
import com.cibertec.experienciasinformativasV.entity.Usuario;
import com.cibertec.experienciasinformativasV.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioPerfilController {

    private final UsuarioService usuarioService;

    public UsuarioPerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{idUsuario}/perfil")
    public Usuario obtenerPerfil(@PathVariable Long idUsuario) {
        return usuarioService.obtenerUsuarioPorId(idUsuario);
    }

    @PutMapping("/{idUsuario}/perfil")
    public Usuario actualizarPerfil(
            @PathVariable Long idUsuario,
            @RequestBody PerfilRequest request
    ) {
        return usuarioService.actualizarPerfil(idUsuario, request);
    }
}