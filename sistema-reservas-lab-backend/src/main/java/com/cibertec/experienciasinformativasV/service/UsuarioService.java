package com.cibertec.experienciasinformativasV.service;

import com.cibertec.experienciasinformativasV.dto.PerfilRequest;
import com.cibertec.experienciasinformativasV.entity.Usuario;
import com.cibertec.experienciasinformativasV.enums.Rol;
import com.cibertec.experienciasinformativasV.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrarProfesor(Usuario profesor) {

        if (profesor.getNombres() == null || profesor.getNombres().isBlank() ||
            profesor.getApellidos() == null || profesor.getApellidos().isBlank() ||
            profesor.getCorreo() == null || profesor.getCorreo().isBlank() ||
            profesor.getPassword() == null || profesor.getPassword().isBlank()) {

            throw new RuntimeException("Todos los campos son obligatorios");
        }

        if (usuarioRepository.existsByCorreo(profesor.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        profesor.setRol(Rol.PROFESOR);
        profesor.setEstado(true);

        return usuarioRepository.save(profesor);
    }

    public List<Usuario> listarProfesores() {
        return usuarioRepository.findByRolAndEstadoTrue(Rol.PROFESOR);
    }

    public List<Usuario> listarAlumnos() {
        return usuarioRepository.findByRolAndEstadoTrue(Rol.ALUMNO);
    }

    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario actualizarProfesor(Long id, Usuario datos) {

        Usuario profesor = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));

        if (profesor.getRol() != Rol.PROFESOR) {
            throw new RuntimeException("El usuario seleccionado no es profesor");
        }

        validarCorreoDisponible(datos.getCorreo(), profesor.getIdUsuario());

        profesor.setNombres(datos.getNombres());
        profesor.setApellidos(datos.getApellidos());
        profesor.setCorreo(datos.getCorreo());

        if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
            profesor.setPassword(datos.getPassword());
        }

        return usuarioRepository.save(profesor);
    }

    public Usuario actualizarAlumno(Long id, Usuario datos) {

        Usuario alumno = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

        if (alumno.getRol() != Rol.ALUMNO) {
            throw new RuntimeException("El usuario seleccionado no es alumno");
        }

        validarCorreoDisponible(datos.getCorreo(), alumno.getIdUsuario());

        alumno.setNombres(datos.getNombres());
        alumno.setApellidos(datos.getApellidos());
        alumno.setCorreo(datos.getCorreo());

        if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
            alumno.setPassword(datos.getPassword());
        }

        return usuarioRepository.save(alumno);
    }

    public Usuario actualizarPerfil(Long id, PerfilRequest datos) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getRol() == Rol.ADMINISTRADOR) {
            throw new RuntimeException("El administrador no actualiza perfil desde esta ruta");
        }

        validarCorreoDisponible(datos.getCorreo(), usuario.getIdUsuario());

        usuario.setNombres(datos.getNombres());
        usuario.setApellidos(datos.getApellidos());
        usuario.setCorreo(datos.getCorreo());

        if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
            usuario.setPassword(datos.getPassword());
        }

        return usuarioRepository.save(usuario);
    }

    public void desactivarUsuario(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getRol() == Rol.ADMINISTRADOR) {
            throw new RuntimeException("No se puede desactivar un usuario administrador");
        }

        usuario.setEstado(false);
        usuarioRepository.save(usuario);
    }

    private void validarCorreoDisponible(String correo, Long idUsuarioActual) {

        if (correo == null || correo.isBlank()) {
            throw new RuntimeException("El correo es obligatorio");
        }

        Optional<Usuario> usuarioExistente = usuarioRepository.findByCorreo(correo);

        if (usuarioExistente.isPresent() &&
            !usuarioExistente.get().getIdUsuario().equals(idUsuarioActual)) {
            throw new RuntimeException("El correo ya está registrado");
        }
    }
    
    
    
    public Usuario registrarAlumno(Usuario alumno) {

        if (usuarioRepository.existsByCorreo(alumno.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        alumno.setRol(Rol.ALUMNO);
        alumno.setEstado(true);

        return usuarioRepository.save(alumno);
    }
    
    public List<Usuario> buscarAlumnos(String texto) {
        return usuarioRepository
                .findByRolAndEstadoTrueAndNombresContainingIgnoreCase(
                        Rol.ALUMNO,
                        texto
                );
    }
}