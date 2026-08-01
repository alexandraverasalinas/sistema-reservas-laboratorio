package com.cibertec.experienciasinformativasV.controller;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.experienciasinformativasV.entity.HorarioLaboratorio;
import com.cibertec.experienciasinformativasV.service.HorarioLaboratorioService;

@RestController
@RequestMapping("/api/admin/horarios")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminHorarioController {

    private final HorarioLaboratorioService horarioService;

    public AdminHorarioController(HorarioLaboratorioService horarioService) {
        this.horarioService = horarioService;
    }

    @GetMapping
    public List<HorarioLaboratorio> listar() {
        return horarioService.listarActivos();
    }

    @GetMapping("/laboratorio/{idLaboratorio}")
    public List<HorarioLaboratorio> listarPorLaboratorio(@PathVariable Long idLaboratorio) {
        return horarioService.listarPorLaboratorio(idLaboratorio);
    }

    @PostMapping("/laboratorio/{idLaboratorio}")
    public HorarioLaboratorio guardar(
            @PathVariable Long idLaboratorio,
            @RequestBody HorarioLaboratorio horario
    ) {
        return horarioService.guardar(idLaboratorio, horario);
    }

    @PutMapping("/{idHorario}")
    public HorarioLaboratorio actualizar(
            @PathVariable Long idHorario,
            @RequestBody HorarioLaboratorio horario
    ) {
        return horarioService.actualizar(idHorario, horario);
    }

    @DeleteMapping("/{idHorario}")
    public void eliminar(@PathVariable Long idHorario) {
        horarioService.desactivar(idHorario);
    }
}