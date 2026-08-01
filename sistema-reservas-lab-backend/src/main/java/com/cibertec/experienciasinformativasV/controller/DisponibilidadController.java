package com.cibertec.experienciasinformativasV.controller;

import com.cibertec.experienciasinformativasV.dto.DisponibilidadResponse;
import com.cibertec.experienciasinformativasV.service.DisponibilidadService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/disponibilidad")
@CrossOrigin(origins = "http://localhost:4200")
public class DisponibilidadController {

    private final DisponibilidadService disponibilidadService;

    public DisponibilidadController(DisponibilidadService disponibilidadService) {
        this.disponibilidadService = disponibilidadService;
    }

    @GetMapping
    public List<DisponibilidadResponse> obtenerDisponibilidad(
            @RequestParam Long laboratorio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
        return disponibilidadService.obtenerDisponibilidad(laboratorio, fecha);
    }
}