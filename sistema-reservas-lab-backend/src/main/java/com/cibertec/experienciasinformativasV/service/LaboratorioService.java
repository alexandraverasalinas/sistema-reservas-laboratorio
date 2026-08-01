package com.cibertec.experienciasinformativasV.service;

import com.cibertec.experienciasinformativasV.entity.Laboratorio;
import com.cibertec.experienciasinformativasV.repository.LaboratorioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LaboratorioService {

    private final LaboratorioRepository laboratorioRepository;

    public LaboratorioService(LaboratorioRepository laboratorioRepository) {
        this.laboratorioRepository = laboratorioRepository;
    }

    public List<Laboratorio> listarActivos() {
        return laboratorioRepository.findByEstadoTrue();
    }

    public Laboratorio guardar(Laboratorio laboratorio) {
        laboratorio.setEstado(true);
        return laboratorioRepository.save(laboratorio);
    }

    public Laboratorio actualizar(Long id, Laboratorio datos) {

        Laboratorio laboratorio = laboratorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado"));

        laboratorio.setNombre(datos.getNombre());
        laboratorio.setUbicacion(datos.getUbicacion());
        laboratorio.setCapacidad(datos.getCapacidad());
        laboratorio.setDescripcion(datos.getDescripcion());

        return laboratorioRepository.save(laboratorio);
    }

    public void desactivar(Long id) {

        Laboratorio laboratorio = laboratorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado"));

        laboratorio.setEstado(false);
        laboratorioRepository.save(laboratorio);
    }
}