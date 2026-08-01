package com.cibertec.experienciasinformativasV.controller;

import com.cibertec.experienciasinformativasV.dto.DashboardAdminResponse;
import com.cibertec.experienciasinformativasV.service.DashboardAdminService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminDashboardController {

    private final DashboardAdminService dashboardAdminService;

    public AdminDashboardController(DashboardAdminService dashboardAdminService) {
        this.dashboardAdminService = dashboardAdminService;
    }

    @GetMapping
    public DashboardAdminResponse obtenerResumen() {
        return dashboardAdminService.obtenerResumen();
    }
}