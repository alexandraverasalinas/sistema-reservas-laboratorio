package com.cibertec.experienciasinformativasV.controller;

import com.cibertec.experienciasinformativasV.entity.HorarioLaboratorio;
import com.cibertec.experienciasinformativasV.entity.Laboratorio;
import com.cibertec.experienciasinformativasV.service.HorarioLaboratorioService;
import com.cibertec.experienciasinformativasV.service.LaboratorioService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/laboratorios")
@CrossOrigin(origins = "http://localhost:4200")
public class CatalogoController {

    private final LaboratorioService laboratorioService;
    private final HorarioLaboratorioService horarioService;

    public CatalogoController(
            LaboratorioService laboratorioService,
            HorarioLaboratorioService horarioService
    ) {
        this.laboratorioService = laboratorioService;
        this.horarioService = horarioService;
    }

    @GetMapping
    public List<Laboratorio> listarLaboratoriosActivos() {
        return laboratorioService.listarActivos();
    }

    @GetMapping("/{idLaboratorio}/horarios")
    public List<HorarioLaboratorio> listarHorariosPorLaboratorio(@PathVariable Long idLaboratorio) {
        return horarioService.listarPorLaboratorio(idLaboratorio);
    }
}