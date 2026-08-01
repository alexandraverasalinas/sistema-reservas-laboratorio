package com.cibertec.experienciasinformativasV.dto;

import java.time.LocalDateTime;

public class ErrorResponse {

    private String mensaje;
    private int estado;
    private LocalDateTime fechaHora;

    public ErrorResponse(String mensaje, int estado) {
        this.mensaje = mensaje;
        this.estado = estado;
        this.fechaHora = LocalDateTime.now();
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}