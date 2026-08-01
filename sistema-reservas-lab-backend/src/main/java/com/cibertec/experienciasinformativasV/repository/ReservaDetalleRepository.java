package com.cibertec.experienciasinformativasV.repository;

import com.cibertec.experienciasinformativasV.entity.ReservaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReservaDetalleRepository extends JpaRepository<ReservaDetalle, Long> {

    List<ReservaDetalle> findByReservaIdReserva(Long idReserva);

    @Query("SELECT r FROM ReservaDetalle r WHERE r.reserva.idReserva = :idReserva AND r.alumno.idUsuario = :idAlumno")
    Optional<ReservaDetalle> findByReservaIdReservaAndAlumnoIdUsuario(Long idReserva, Long idAlumno);

    boolean existsByReservaIdReservaAndAlumnoIdUsuario(Long idReserva, Long idAlumno);

    List<ReservaDetalle> findByAlumnoIdUsuario(Long idAlumno);
}