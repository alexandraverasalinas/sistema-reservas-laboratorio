package com.cibertec.experienciasinformativasV.service;

import com.cibertec.experienciasinformativasV.entity.HorarioLaboratorio;
import com.cibertec.experienciasinformativasV.entity.Laboratorio;
import com.cibertec.experienciasinformativasV.repository.HorarioLaboratorioRepository;
import com.cibertec.experienciasinformativasV.repository.LaboratorioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HorarioLaboratorioService {

    private final HorarioLaboratorioRepository horarioRepository;
    private final LaboratorioRepository laboratorioRepository;

    public HorarioLaboratorioService(
            HorarioLaboratorioRepository horarioRepository,
            LaboratorioRepository laboratorioRepository
    ) {
        this.horarioRepository = horarioRepository;
        this.laboratorioRepository = laboratorioRepository;
    }

    public List<HorarioLaboratorio> listarActivos() {
        return horarioRepository.findByEstadoTrue();
    }

    public List<HorarioLaboratorio> listarPorLaboratorio(Long idLaboratorio) {
        return horarioRepository.findByLaboratorioIdLaboratorioAndEstadoTrue(idLaboratorio);
    }

    public HorarioLaboratorio guardar(Long idLaboratorio, HorarioLaboratorio horario) {

        Laboratorio laboratorio = laboratorioRepository.findById(idLaboratorio)
                .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado"));

        if (!horario.getHoraInicio().isBefore(horario.getHoraFin())) {
            throw new RuntimeException("La hora de inicio debe ser menor que la hora fin");
        }

        boolean existeCruce = horarioRepository.existeCruceHorario(
                idLaboratorio,
                horario.getDiaSemana(),
                horario.getHoraInicio(),
                horario.getHoraFin()
        );

        if (existeCruce) {
            throw new RuntimeException("Ya existe un horario registrado para este laboratorio en ese rango de horas");
        }

        horario.setLaboratorio(laboratorio);
        horario.setEstado(true);

        return horarioRepository.save(horario);
    }

    public HorarioLaboratorio actualizar(Long idHorario, HorarioLaboratorio datos) {

        HorarioLaboratorio horario = horarioRepository.findById(idHorario)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        if (!datos.getHoraInicio().isBefore(datos.getHoraFin())) {
            throw new RuntimeException("La hora de inicio debe ser menor que la hora fin");
        }

        Long idLaboratorio = horario.getLaboratorio().getIdLaboratorio();

        boolean existeCruce = horarioRepository.existeCruceHorarioAlActualizar(
                idHorario,
                idLaboratorio,
                datos.getDiaSemana(),
                datos.getHoraInicio(),
                datos.getHoraFin()
        );

        if (existeCruce) {
            throw new RuntimeException("Ya existe otro horario cruzado para este laboratorio");
        }

        horario.setDiaSemana(datos.getDiaSemana());
        horario.setHoraInicio(datos.getHoraInicio());
        horario.setHoraFin(datos.getHoraFin());

        return horarioRepository.save(horario);
    }

    public void desactivar(Long id) {

        HorarioLaboratorio horario = horarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        horario.setEstado(false);
        horarioRepository.save(horario);
    }
}