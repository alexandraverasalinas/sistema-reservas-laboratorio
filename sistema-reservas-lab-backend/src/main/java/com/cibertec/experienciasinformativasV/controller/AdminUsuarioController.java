package com.cibertec.experienciasinformativasV.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.experienciasinformativasV.entity.Usuario;
import com.cibertec.experienciasinformativasV.service.UsuarioService;

@RestController
@RequestMapping("/api/admin/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminUsuarioController {

    private final UsuarioService usuarioService;

    public AdminUsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/profesores")
    public Usuario registrarProfesor(@RequestBody Usuario profesor) {
        return usuarioService.registrarProfesor(profesor);
    }

    @GetMapping("/profesores")
    public List<Usuario> listarProfesores() {
        return usuarioService.listarProfesores();
    }

    @PutMapping("/profesores/{id}")
    public Usuario actualizarProfesor(@PathVariable Long id, @RequestBody Usuario profesor) {
        return usuarioService.actualizarProfesor(id, profesor);
    }

    @GetMapping("/alumnos")
    public List<Usuario> listarAlumnos() {
        return usuarioService.listarAlumnos();
    }

    @PutMapping("/alumnos/{id}")
    public Usuario actualizarAlumno(@PathVariable Long id, @RequestBody Usuario alumno) {
        return usuarioService.actualizarAlumno(id, alumno);
    }

    @DeleteMapping("/{id}")
    public void desactivarUsuario(@PathVariable Long id) {
        usuarioService.desactivarUsuario(id);
    }
    
    @PostMapping("/alumnos")
    public Usuario registrarAlumno(@RequestBody Usuario alumno) {
        return usuarioService.registrarAlumno(alumno);
    }
    
    
    @GetMapping("/alumnos/buscar")
    public List<Usuario> buscarAlumnos(@RequestParam String texto) {
        return usuarioService.buscarAlumnos(texto);
    }
    
    
}