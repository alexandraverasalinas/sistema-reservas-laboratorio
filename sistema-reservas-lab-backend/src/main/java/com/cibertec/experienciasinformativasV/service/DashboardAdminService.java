package com.cibertec.experienciasinformativasV.service;

import com.cibertec.experienciasinformativasV.dto.DashboardAdminResponse;
import com.cibertec.experienciasinformativasV.enums.EstadoReserva;
import com.cibertec.experienciasinformativasV.enums.Rol;
import com.cibertec.experienciasinformativasV.repository.LaboratorioRepository;
import com.cibertec.experienciasinformativasV.repository.ReservaRepository;
import com.cibertec.experienciasinformativasV.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DashboardAdminService {

    private final LaboratorioRepository laboratorioRepository;
    private final UsuarioRepository usuarioRepository;
    private final ReservaRepository reservaRepository;

    public DashboardAdminService(
            LaboratorioRepository laboratorioRepository,
            UsuarioRepository usuarioRepository,
            ReservaRepository reservaRepository
    ) {
        this.laboratorioRepository = laboratorioRepository;
        this.usuarioRepository = usuarioRepository;
        this.reservaRepository = reservaRepository;
    }

    public DashboardAdminResponse obtenerResumen() {

        long totalLaboratoriosActivos = laboratorioRepository.countByEstadoTrue();

        long totalProfesoresActivos = usuarioRepository.countByRolAndEstadoTrue(Rol.PROFESOR);

        long totalAlumnosActivos = usuarioRepository.countByRolAndEstadoTrue(Rol.ALUMNO);

        long totalReservas = reservaRepository.count();

        long reservasActivas = reservaRepository.countByEstado(EstadoReserva.RESERVADO);

        long reservasCanceladas = reservaRepository.countByEstado(EstadoReserva.CANCELADO);

        long reservasFinalizadas = reservaRepository.countByEstado(EstadoReserva.FINALIZADO);

        long reservasDelDia = reservaRepository.countByFechaAndEstado(
                LocalDate.now(),
                EstadoReserva.RESERVADO
        );

        return new DashboardAdminResponse(
                totalLaboratoriosActivos,
                totalProfesoresActivos,
                totalAlumnosActivos,
                totalReservas,
                reservasActivas,
                reservasCanceladas,
                reservasFinalizadas,
                reservasDelDia
        );
    }
}