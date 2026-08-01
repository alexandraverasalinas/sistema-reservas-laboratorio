package com.cibertec.experienciasinformativasV.service;

import com.cibertec.experienciasinformativasV.dto.DisponibilidadResponse;
import com.cibertec.experienciasinformativasV.entity.HorarioLaboratorio;
import com.cibertec.experienciasinformativasV.entity.Laboratorio;
import com.cibertec.experienciasinformativasV.entity.Reserva;
import com.cibertec.experienciasinformativasV.enums.DiaSemana;
import com.cibertec.experienciasinformativasV.enums.EstadoReserva;
import com.cibertec.experienciasinformativasV.enums.TipoReserva;
import com.cibertec.experienciasinformativasV.repository.HorarioLaboratorioRepository;
import com.cibertec.experienciasinformativasV.repository.LaboratorioRepository;
import com.cibertec.experienciasinformativasV.repository.ReservaDetalleRepository;
import com.cibertec.experienciasinformativasV.repository.ReservaRepository;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisponibilidadService {

    private static final int MAX_CUPOS_POR_HORARIO = 15;

    private final HorarioLaboratorioRepository horarioRepository;
    private final ReservaRepository reservaRepository;
    private final LaboratorioRepository laboratorioRepository;
    private final ReservaDetalleRepository reservaDetalleRepository;

    public DisponibilidadService(
            HorarioLaboratorioRepository horarioRepository,
            ReservaRepository reservaRepository,
            LaboratorioRepository laboratorioRepository,
            ReservaDetalleRepository reservaDetalleRepository
    ) {
        this.horarioRepository = horarioRepository;
        this.reservaRepository = reservaRepository;
        this.laboratorioRepository = laboratorioRepository;
        this.reservaDetalleRepository = reservaDetalleRepository;
    }

    public List<DisponibilidadResponse> obtenerDisponibilidad(Long idLaboratorio, LocalDate fecha) {

        Laboratorio laboratorio = laboratorioRepository.findById(idLaboratorio)
                .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado"));

        DiaSemana diaSemana = obtenerDiaSemana(fecha.getDayOfWeek());

        List<HorarioLaboratorio> horarios = horarioRepository
                .findByLaboratorioIdLaboratorioAndDiaSemanaAndEstadoTrue(idLaboratorio, diaSemana);

        List<Reserva> reservas = reservaRepository
                .findByLaboratorioIdLaboratorioAndFechaAndEstado(idLaboratorio, fecha, EstadoReserva.RESERVADO);

        List<DisponibilidadResponse> respuesta = new ArrayList<>();

        for (HorarioLaboratorio horario : horarios) {

            List<Reserva> reservasEnEsteHorario = new ArrayList<>();
            for (Reserva reserva : reservas) {
                if (seCruzan(horario.getHoraInicio(), horario.getHoraFin(),
                        reserva.getHoraInicio(), reserva.getHoraFin())) {
                    reservasEnEsteHorario.add(reserva);
                }
            }

            if (reservasEnEsteHorario.isEmpty()) {
                DisponibilidadResponse item = new DisponibilidadResponse();
                item.setIdLaboratorio(laboratorio.getIdLaboratorio());
                item.setNombreLaboratorio(laboratorio.getNombre());
                item.setFecha(fecha);
                item.setHoraInicio(horario.getHoraInicio());
                item.setHoraFin(horario.getHoraFin());
                item.setEstado("DISPONIBLE");
                item.setIdReserva(null);
                item.setTipoReserva(null);
                item.setCuposOcupados(0);
                item.setCuposTotales(MAX_CUPOS_POR_HORARIO);
                item.setAlumnos(new ArrayList<>());
                respuesta.add(item);
                continue;
            }

            boolean tieneProfesorOAdmin = false;
            Reserva reservaAlumno = null;

            for (Reserva r : reservasEnEsteHorario) {
                if (r.getTipoReserva() == TipoReserva.PROFESOR || r.getTipoReserva() == TipoReserva.ADMINISTRADOR) {
                    tieneProfesorOAdmin = true;
                    break;
                }
                if (r.getTipoReserva() == TipoReserva.ALUMNO) {
                    reservaAlumno = r;
                }
            }

            if (tieneProfesorOAdmin) {
                DisponibilidadResponse item = new DisponibilidadResponse();
                item.setIdLaboratorio(laboratorio.getIdLaboratorio());
                item.setNombreLaboratorio(laboratorio.getNombre());
                item.setFecha(fecha);
                item.setHoraInicio(horario.getHoraInicio());
                item.setHoraFin(horario.getHoraFin());
                item.setEstado("OCUPADO");
                item.setTipoReserva("PROFESOR");
                item.setCuposOcupados(MAX_CUPOS_POR_HORARIO);
                item.setCuposTotales(MAX_CUPOS_POR_HORARIO);
                item.setAlumnos(new ArrayList<>());
                respuesta.add(item);
            } else if (reservaAlumno != null) {
                DisponibilidadResponse item = new DisponibilidadResponse();
                item.setIdLaboratorio(laboratorio.getIdLaboratorio());
                item.setNombreLaboratorio(laboratorio.getNombre());
                item.setFecha(fecha);
                item.setHoraInicio(horario.getHoraInicio());
                item.setHoraFin(horario.getHoraFin());
                item.setIdReserva(reservaAlumno.getIdReserva());
                item.setTipoReserva("ALUMNO");
                item.setCuposOcupados(reservaAlumno.getCuposOcupados());
                item.setCuposTotales(MAX_CUPOS_POR_HORARIO);

                if (reservaAlumno.getCuposOcupados() >= MAX_CUPOS_POR_HORARIO) {
                    item.setEstado("OCUPADO");
                } else {
                    item.setEstado("OCUPADO");
                }

                List<String> nombresAlumnos = reservaDetalleRepository
                        .findByReservaIdReserva(reservaAlumno.getIdReserva())
                        .stream()
                        .map(d -> d.getAlumno().getNombres() + " " + d.getAlumno().getApellidos())
                        .collect(Collectors.toList());
                item.setAlumnos(nombresAlumnos);

                respuesta.add(item);
            }
        }

        return respuesta;
    }

    private boolean seCruzan(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {
        return inicio1.isBefore(fin2) && fin1.isAfter(inicio2);
    }

    private DiaSemana obtenerDiaSemana(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> DiaSemana.LUNES;
            case TUESDAY -> DiaSemana.MARTES;
            case WEDNESDAY -> DiaSemana.MIERCOLES;
            case THURSDAY -> DiaSemana.JUEVES;
            case FRIDAY -> DiaSemana.VIERNES;
            case SATURDAY -> DiaSemana.SABADO;
            case SUNDAY -> DiaSemana.DOMINGO;
        };
    }
}