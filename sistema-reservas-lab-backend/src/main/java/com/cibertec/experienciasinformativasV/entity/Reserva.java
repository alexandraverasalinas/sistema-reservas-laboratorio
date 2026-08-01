package com.cibertec.experienciasinformativasV.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.cibertec.experienciasinformativasV.enums.EstadoReserva;
import com.cibertec.experienciasinformativasV.enums.TipoReserva;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Data
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long idReserva;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_laboratorio")
    private Laboratorio laboratorio;

    private LocalDate fecha;

    private LocalTime horaInicio;

    private LocalTime horaFin;

    private String motivo;

    @Enumerated(EnumType.STRING)
    private EstadoReserva estado = EstadoReserva.RESERVADO;

    // NUEVOS CAMPOS
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_reserva")
    private TipoReserva tipoReserva;

    @Column(name = "cupos_ocupados")
    private Integer cuposOcupados = 1;

    // Relación con los alumnos que comparten esta reserva
    @JsonIgnore
    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservaDetalle> detalles = new ArrayList<>();

    // Getters y Setters
    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Laboratorio getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(Laboratorio laboratorio) {
        this.laboratorio = laboratorio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public TipoReserva getTipoReserva() {
        return tipoReserva;
    }

    public void setTipoReserva(TipoReserva tipoReserva) {
        this.tipoReserva = tipoReserva;
    }

    public Integer getCuposOcupados() {
        return cuposOcupados;
    }

    public void setCuposOcupados(Integer cuposOcupados) {
        this.cuposOcupados = cuposOcupados;
    }

    public List<ReservaDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<ReservaDetalle> detalles) {
        this.detalles = detalles;
    }
}