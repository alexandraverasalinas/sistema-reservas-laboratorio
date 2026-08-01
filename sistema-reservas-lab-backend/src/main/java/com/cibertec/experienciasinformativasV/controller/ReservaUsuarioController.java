package com.cibertec.experienciasinformativasV.controller;

import com.cibertec.experienciasinformativasV.dto.CrearReservaRequest;
import com.cibertec.experienciasinformativasV.entity.Reserva;
import com.cibertec.experienciasinformativasV.entity.ReservaDetalle;
import com.cibertec.experienciasinformativasV.repository.ReservaDetalleRepository;
import com.cibertec.experienciasinformativasV.repository.ReservaRepository;
import com.cibertec.experienciasinformativasV.enums.EstadoReserva;
import com.cibertec.experienciasinformativasV.service.ReservaService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservaUsuarioController {

    private final ReservaService reservaService;
    private final ReservaDetalleRepository reservaDetalleRepository;
    private final ReservaRepository reservaRepository;

    public ReservaUsuarioController(ReservaService reservaService, 
                                     ReservaDetalleRepository reservaDetalleRepository,
                                     ReservaRepository reservaRepository) {
        this.reservaService = reservaService;
        this.reservaDetalleRepository = reservaDetalleRepository;
        this.reservaRepository = reservaRepository;
    }

    @PostMapping
    public Reserva crearReserva(@RequestBody CrearReservaRequest request) {
        return reservaService.crearReserva(request);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<Reserva> listarMisReservas(@PathVariable Long idUsuario) {
        List<Reserva> todas = new ArrayList<>();
        
        List<Reserva> reservasPropias = reservaRepository.findByUsuarioIdUsuarioAndEstado(idUsuario, EstadoReserva.RESERVADO);
        todas.addAll(reservasPropias);
        
        List<ReservaDetalle> detalles = reservaDetalleRepository.findByAlumnoIdUsuario(idUsuario);
        
        for (ReservaDetalle detalle : detalles) {
            Reserva reserva = detalle.getReserva();
            if (reserva != null && "RESERVADO".equals(reserva.getEstado().name())) {
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

    @PutMapping("/{idReserva}/cancelar")
    public Reserva cancelarReserva(@PathVariable Long idReserva) {
        return reservaService.cancelarReserva(idReserva);
    }

    @GetMapping("/debug/usuario/{idUsuario}")
    public String debugReservas(@PathVariable Long idUsuario) {
        StringBuilder sb = new StringBuilder();
        
        List<Reserva> propias = reservaRepository.findByUsuarioIdUsuarioAndEstado(idUsuario, EstadoReserva.RESERVADO);
        sb.append("Reservas propias: ").append(propias.size()).append("\n");
        for (Reserva r : propias) {
            sb.append("ID: ").append(r.getIdReserva())
              .append(" | Horario: ").append(r.getHoraInicio()).append(" - ").append(r.getHoraFin())
              .append(" | Estado: ").append(r.getEstado()).append("\n");
        }
        
        List<ReservaDetalle> detalles = reservaDetalleRepository.findByAlumnoIdUsuario(idUsuario);
        sb.append("\nDetalles: ").append(detalles.size()).append("\n");
        for (ReservaDetalle d : detalles) {
            sb.append("Detalle ID: ").append(d.getIdDetalle())
              .append(" | Reserva ID: ").append(d.getReserva().getIdReserva()).append("\n");
        }
        
        return sb.toString();
    }
}