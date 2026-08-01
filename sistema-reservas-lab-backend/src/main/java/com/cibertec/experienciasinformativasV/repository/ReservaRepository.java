package com.cibertec.experienciasinformativasV.repository;

import com.cibertec.experienciasinformativasV.entity.Reserva;
import com.cibertec.experienciasinformativasV.enums.EstadoReserva;
import com.cibertec.experienciasinformativasV.enums.TipoReserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByEstado(EstadoReserva estado);

    List<Reserva> findByUsuarioIdUsuarioAndEstado(Long idUsuario, EstadoReserva estado);

    List<Reserva> findByLaboratorioIdLaboratorioAndFechaAndEstado(
            Long idLaboratorio,
            LocalDate fecha,
            EstadoReserva estado
    );

    long countByEstado(EstadoReserva estado);

    long countByFechaAndEstado(LocalDate fecha, EstadoReserva estado);

    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
        FROM Reserva r
        WHERE r.laboratorio.idLaboratorio = :idLaboratorio
        AND r.fecha = :fecha
        AND r.estado = :estado
        AND (:horaInicio < r.horaFin AND :horaFin > r.horaInicio)
    """)
    boolean existeCruceReserva(
            @Param("idLaboratorio") Long idLaboratorio,
            @Param("fecha") LocalDate fecha,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin,
            @Param("estado") EstadoReserva estado
    );

    List<Reserva> findByEstadoAndMotivoContainingIgnoreCase(
            EstadoReserva estado,
            String motivo
    );


    
    @Query("""
        SELECT r FROM Reserva r
        WHERE r.laboratorio.idLaboratorio = :idLaboratorio
        AND r.fecha = :fecha
        AND r.horaInicio = :horaInicio
        AND r.horaFin = :horaFin
        AND r.estado = :estado
        AND r.tipoReserva = :tipoReserva
        AND r.cuposOcupados < :maxCupos
    """)
    Optional<Reserva> findReservaCompartible(
            @Param("idLaboratorio") Long idLaboratorio,
            @Param("fecha") LocalDate fecha,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin,
            @Param("estado") EstadoReserva estado,
            @Param("tipoReserva") TipoReserva tipoReserva,
            @Param("maxCupos") Integer maxCupos
    );

        @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
        FROM Reserva r
        WHERE r.laboratorio.idLaboratorio = :idLaboratorio
        AND r.fecha = :fecha
        AND r.estado = :estado
        AND (:horaInicio < r.horaFin AND :horaFin > r.horaInicio)
        AND r.tipoReserva IN (:tipos)
    """)
    boolean existeReservaProfesorOAdmin(
            @Param("idLaboratorio") Long idLaboratorio,
            @Param("fecha") LocalDate fecha,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin,
            @Param("estado") EstadoReserva estado,
            @Param("tipos") List<TipoReserva> tipos
    );
}