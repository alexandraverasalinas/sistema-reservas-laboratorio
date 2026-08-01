package com.cibertec.experienciasinformativasV.repository;

import com.cibertec.experienciasinformativasV.entity.Laboratorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LaboratorioRepository extends JpaRepository<Laboratorio, Long> {

    List<Laboratorio> findByEstadoTrue();

    long countByEstadoTrue();
    
    List<Laboratorio> findByEstadoTrueAndNombreContainingIgnoreCase(String nombre);
}