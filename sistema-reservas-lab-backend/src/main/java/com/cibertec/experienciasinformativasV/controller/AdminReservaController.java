package com.cibertec.experienciasinformativasV.controller;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.experienciasinformativasV.entity.Reserva;
import com.cibertec.experienciasinformativasV.service.ReservaService;

@RestController
@RequestMapping("/api/admin/reservas")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminReservaController {

    private final ReservaService reservaService;

    public AdminReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public List<Reserva> listarTodas() {
        return reservaService.listarTodas();
    }

    @GetMapping("/activas")
    public List<Reserva> listarReservadas() {
        return reservaService.listarReservadas();
    }

    @PutMapping("/{id}/cancelar")
    public Reserva cancelar(@PathVariable Long id) {
        return reservaService.cancelarReserva(id);
    }
}