package com.cibertec.experienciasinformativasV.service;

import com.cibertec.experienciasinformativasV.dto.CrearReservaRequest;
import com.cibertec.experienciasinformativasV.entity.Laboratorio;
import com.cibertec.experienciasinformativasV.entity.Reserva;
import com.cibertec.experienciasinformativasV.entity.ReservaDetalle;
import com.cibertec.experienciasinformativasV.entity.Usuario;
import com.cibertec.experienciasinformativasV.enums.DiaSemana;
import com.cibertec.experienciasinformativasV.enums.EstadoReserva;
import com.cibertec.experienciasinformativasV.enums.Rol;
import com.cibertec.experienciasinformativasV.enums.TipoReserva;
import com.cibertec.experienciasinformativasV.repository.HorarioLaboratorioRepository;
import com.cibertec.experienciasinformativasV.repository.LaboratorioRepository;
import com.cibertec.experienciasinformativasV.repository.ReservaDetalleRepository;
import com.cibertec.experienciasinformativasV.repository.ReservaRepository;
import com.cibertec.experienciasinformativasV.repository.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private static final int MAX_CUPOS_POR_HORARIO = 15;

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final LaboratorioRepository laboratorioRepository;
    private final HorarioLaboratorioRepository horarioRepository;
    private final ReservaDetalleRepository reservaDetalleRepository;

    public ReservaService(
            ReservaRepository reservaRepository,
            UsuarioRepository usuarioRepository,
            LaboratorioRepository laboratorioRepository,
            HorarioLaboratorioRepository horarioRepository,
            ReservaDetalleRepository reservaDetalleRepository
    ) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.laboratorioRepository = laboratorioRepository;
        this.horarioRepository = horarioRepository;
        this.reservaDetalleRepository = reservaDetalleRepository;
    }

    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    public List<Reserva> listarReservadas() {
        return reservaRepository.findByEstado(EstadoReserva.RESERVADO);
    }

    @Transactional
    public Reserva crearReserva(CrearReservaRequest request) {

        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getEstado()) {
            throw new RuntimeException("Usuario inactivo");
        }

        TipoReserva tipoReserva;
        try {
            tipoReserva = TipoReserva.valueOf(request.getTipoReserva());
        } catch (Exception e) {
            throw new RuntimeException("Tipo de reserva inválido. Use: ALUMNO, PROFESOR o ADMINISTRADOR");
        }

        if (usuario.getRol() == Rol.ALUMNO && tipoReserva != TipoReserva.ALUMNO) {
            throw new RuntimeException("Un alumno solo puede hacer reservas de tipo ALUMNO");
        }
        if (usuario.getRol() == Rol.PROFESOR && tipoReserva != TipoReserva.PROFESOR) {
            throw new RuntimeException("Un profesor solo puede hacer reservas de tipo PROFESOR");
        }
        if (usuario.getRol() == Rol.ADMINISTRADOR && tipoReserva != TipoReserva.ADMINISTRADOR) {
            throw new RuntimeException("Un administrador solo puede hacer reservas de tipo ADMINISTRADOR");
        }

        Laboratorio laboratorio = laboratorioRepository.findById(request.getIdLaboratorio())
                .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado"));

        if (!laboratorio.getEstado()) {
            throw new RuntimeException("El laboratorio no está disponible");
        }

        if (!request.getHoraInicio().isBefore(request.getHoraFin())) {
            throw new RuntimeException("La hora de inicio debe ser menor que la hora fin");
        }

        DiaSemana diaSemana = obtenerDiaSemana(request.getFecha().getDayOfWeek());

        boolean existeHorarioConfigurado = horarioRepository.existeHorarioDisponible(
                request.getIdLaboratorio(),
                diaSemana,
                request.getHoraInicio(),
                request.getHoraFin()
        );

        if (!existeHorarioConfigurado) {
            throw new RuntimeException("No existe un horario disponible configurado para este laboratorio en la fecha y hora seleccionada");
        }

        if (tipoReserva == TipoReserva.ALUMNO) {

            boolean existeProfeOAdmin = reservaRepository.existeReservaProfesorOAdmin(
                    request.getIdLaboratorio(),
                    request.getFecha(),
                    request.getHoraInicio(),
                    request.getHoraFin(),
                    EstadoReserva.RESERVADO,
                    Arrays.asList(TipoReserva.PROFESOR, TipoReserva.ADMINISTRADOR)
            );

            if (existeProfeOAdmin) {
                throw new RuntimeException("El laboratorio ya está reservado completamente en este horario");
            }

            Optional<Reserva> reservaCompartible = reservaRepository.findReservaCompartible(
                    request.getIdLaboratorio(),
                    request.getFecha(),
                    request.getHoraInicio(),
                    request.getHoraFin(),
                    EstadoReserva.RESERVADO,
                    TipoReserva.ALUMNO,
                    MAX_CUPOS_POR_HORARIO
            );

            if (reservaCompartible.isPresent()) {
                Reserva reservaExistente = reservaCompartible.get();

                boolean yaEstaEnReserva = reservaDetalleRepository
                        .existsByReservaIdReservaAndAlumnoIdUsuario(
                                reservaExistente.getIdReserva(),
                                usuario.getIdUsuario()
                        );

                if (yaEstaEnReserva) {
                    throw new RuntimeException("Ya tienes una reserva en este horario");
                }

                ReservaDetalle detalle = new ReservaDetalle();
                detalle.setReserva(reservaExistente);
                detalle.setAlumno(usuario);
                detalle.setFechaRegistro(LocalDate.now());
                reservaDetalleRepository.save(detalle);

                reservaExistente.setCuposOcupados(reservaExistente.getCuposOcupados() + 1);
                return reservaRepository.save(reservaExistente);
            } else {
                boolean existeCruce = reservaRepository.existeCruceReserva(
                        request.getIdLaboratorio(),
                        request.getFecha(),
                        request.getHoraInicio(),
                        request.getHoraFin(),
                        EstadoReserva.RESERVADO
                );

                if (existeCruce) {
                    throw new RuntimeException("El horario ya está ocupado");
                }

                Reserva reserva = new Reserva();
                reserva.setUsuario(usuario);
                reserva.setLaboratorio(laboratorio);
                reserva.setFecha(request.getFecha());
                reserva.setHoraInicio(request.getHoraInicio());
                reserva.setHoraFin(request.getHoraFin());
                reserva.setMotivo(request.getMotivo());
                reserva.setEstado(EstadoReserva.RESERVADO);
                reserva.setTipoReserva(TipoReserva.ALUMNO);
                reserva.setCuposOcupados(1);

                reserva = reservaRepository.save(reserva);

                ReservaDetalle detalle = new ReservaDetalle();
                detalle.setReserva(reserva);
                detalle.setAlumno(usuario);
                detalle.setFechaRegistro(LocalDate.now());
                reservaDetalleRepository.save(detalle);

                return reserva;
            }
        }

        if (tipoReserva == TipoReserva.PROFESOR || tipoReserva == TipoReserva.ADMINISTRADOR) {

            boolean existeCruce = reservaRepository.existeCruceReserva(
                    request.getIdLaboratorio(),
                    request.getFecha(),
                    request.getHoraInicio(),
                    request.getHoraFin(),
                    EstadoReserva.RESERVADO
            );

            if (existeCruce) {
                throw new RuntimeException("El horario ya está ocupado");
            }

            Reserva reserva = new Reserva();
            reserva.setUsuario(usuario);
            reserva.setLaboratorio(laboratorio);
            reserva.setFecha(request.getFecha());
            reserva.setHoraInicio(request.getHoraInicio());
            reserva.setHoraFin(request.getHoraFin());
            reserva.setMotivo(request.getMotivo());
            reserva.setEstado(EstadoReserva.RESERVADO);
            reserva.setTipoReserva(tipoReserva);
            reserva.setCuposOcupados(MAX_CUPOS_POR_HORARIO);

            return reservaRepository.save(reserva);
        }

        throw new RuntimeException("Tipo de reserva no soportado");
    }

    public List<Reserva> listarMisReservas(Long idUsuario) {
        List<Reserva> todas = new ArrayList<>();

        List<Reserva> reservasPropias = reservaRepository.findByUsuarioIdUsuarioAndEstado(idUsuario, EstadoReserva.RESERVADO);
        todas.addAll(reservasPropias);

        List<ReservaDetalle> detalles = reservaDetalleRepository.findByAlumnoIdUsuario(idUsuario);

        for (ReservaDetalle detalle : detalles) {
            Reserva reserva = detalle.getReserva();
            if (reserva != null && reserva.getEstado() == EstadoReserva.RESERVADO) {
                boolean yaExiste = false;
                for (Reserva r : todas) {
                    if (r.getIdReserva().equals(reserva.getIdReserva())) {
                        yaExiste = true;
                        break;
                    }
                }
                if (!yaExiste) {
                    todas.add(reserva);
                }
            }
        }

        return todas;
    }

    @Transactional
    public Reserva cancelarReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        reserva.setEstado(EstadoReserva.CANCELADO);
        return reservaRepository.save(reserva);
    }

    private boolean seCruzan(java.time.LocalTime inicio1, java.time.LocalTime fin1,
                             java.time.LocalTime inicio2, java.time.LocalTime fin2) {
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