package com.cibertec.experienciasinformativasV.repository;

import com.cibertec.experienciasinformativasV.entity.HorarioLaboratorio;
import com.cibertec.experienciasinformativasV.enums.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface HorarioLaboratorioRepository extends JpaRepository<HorarioLaboratorio, Long> {

    List<HorarioLaboratorio> findByEstadoTrue();

    List<HorarioLaboratorio> findByLaboratorioIdLaboratorioAndEstadoTrue(Long idLaboratorio);

    List<HorarioLaboratorio> findByLaboratorioIdLaboratorioAndDiaSemanaAndEstadoTrue(
            Long idLaboratorio,
            DiaSemana diaSemana
    );

    @Query("""
        SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END
        FROM HorarioLaboratorio h
        WHERE h.laboratorio.idLaboratorio = :idLaboratorio
        AND h.diaSemana = :diaSemana
        AND h.estado = true
        AND (:horaInicio < h.horaFin AND :horaFin > h.horaInicio)
    """)
    boolean existeCruceHorario(
            @Param("idLaboratorio") Long idLaboratorio,
            @Param("diaSemana") DiaSemana diaSemana,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin
    );

    @Query("""
        SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END
        FROM HorarioLaboratorio h
        WHERE h.laboratorio.idLaboratorio = :idLaboratorio
        AND h.diaSemana = :diaSemana
        AND h.estado = true
        AND h.idHorario <> :idHorario
        AND (:horaInicio < h.horaFin AND :horaFin > h.horaInicio)
    """)
    boolean existeCruceHorarioAlActualizar(
            @Param("idHorario") Long idHorario,
            @Param("idLaboratorio") Long idLaboratorio,
            @Param("diaSemana") DiaSemana diaSemana,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin
    );

    @Query("""
        SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END
        FROM HorarioLaboratorio h
        WHERE h.laboratorio.idLaboratorio = :idLaboratorio
        AND h.diaSemana = :diaSemana
        AND h.estado = true
        AND h.horaInicio <= :horaInicio
        AND h.horaFin >= :horaFin
    """)
    boolean existeHorarioDisponible(
            @Param("idLaboratorio") Long idLaboratorio,
            @Param("diaSemana") DiaSemana diaSemana,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin
    );
    
    List<HorarioLaboratorio>
    findByDiaSemanaAndEstadoTrue(DiaSemana diaSemana);
}