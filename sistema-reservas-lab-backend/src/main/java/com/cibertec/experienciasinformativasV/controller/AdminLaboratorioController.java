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

import com.cibertec.experienciasinformativasV.entity.Laboratorio;
import com.cibertec.experienciasinformativasV.service.LaboratorioService;

@RestController
@RequestMapping("/api/admin/laboratorios")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminLaboratorioController {

    private final LaboratorioService laboratorioService;

    public AdminLaboratorioController(LaboratorioService laboratorioService) {
        this.laboratorioService = laboratorioService;
    }

    @GetMapping
    public List<Laboratorio> listar() {
        return laboratorioService.listarActivos();
    }

    @PostMapping
    public Laboratorio guardar(@RequestBody Laboratorio laboratorio) {
        return laboratorioService.guardar(laboratorio);
    }

    @PutMapping("/{id}")
    public Laboratorio actualizar(@PathVariable Long id, @RequestBody Laboratorio laboratorio) {
        return laboratorioService.actualizar(id, laboratorio);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        laboratorioService.desactivar(id);
    }
}