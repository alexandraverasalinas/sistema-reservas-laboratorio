package com.cibertec.experienciasinformativasV.dto;

import com.cibertec.experienciasinformativasV.entity.Usuario;

public class AuthResponse {

    private Long idUsuario;
    private String nombres;
    private String apellidos;
    private String correo;
    private String rol;
    private Boolean estado;

    public AuthResponse(Usuario usuario) {
        this.idUsuario = usuario.getIdUsuario();
        this.nombres = usuario.getNombres();
        this.apellidos = usuario.getApellidos();
        this.correo = usuario.getCorreo();
        this.rol = usuario.getRol().name();
        this.estado = usuario.getEstado();
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}