import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';

import { DashboardAdminService } from '../../services/dashboard-admin';
import { AuthService } from '../../services/auth';

import { DashboardAdmin } from '../../models/dashboard-admin.model';
import { Usuario } from '../../models/usuario.model';
import { Reserva } from '../../models/reserva.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {

  usuario: Usuario | null = null;

  resumen: DashboardAdmin = {
    totalLaboratoriosActivos: 0,
    totalProfesoresActivos: 0,
    totalAlumnosActivos: 0,
    totalReservas: 0,
    reservasActivas: 0,
    reservasCanceladas: 0,
    reservasFinalizadas: 0,
    reservasDelDia: 0
  };

  ultimasReservas: Reserva[] = [];

  cargando = false;
  mensajeError = '';

  constructor(
    private dashboardService: DashboardAdminService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.usuario = this.authService.obtenerUsuario();

    if (!this.usuario || this.usuario.rol !== 'ADMINISTRADOR') {
      this.router.navigate(['/login']);
      return;
    }

    this.cargarDashboard();
    this.cargarUltimasReservas();
  }

  cargarDashboard(): void {
    this.cargando = true;
    this.mensajeError = '';

    this.dashboardService.obtenerResumen().subscribe({
      next: (data) => {
        this.resumen = data;
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error dashboard:', error);
        this.cargando = false;
        this.mensajeError = 'No se pudo cargar el resumen del dashboard.';
      }
    });
  }

  cargarUltimasReservas(): void {
    fetch('http://localhost:8080/api/admin/reservas')
      .then(response => {
        if (!response.ok) {
          throw new Error('No se pudieron cargar las últimas reservas.');
        }
        return response.json();
      })
      .then(data => {
        const reservas: Reserva[] = data || [];

        this.ultimasReservas = reservas
          .sort((a, b) => b.idReserva - a.idReserva)
          .slice(0, 5);

        this.cdr.detectChanges();
      })
      .catch(error => {
        console.error('Error últimas reservas:', error);
      });
  }

  cerrarSesion(): void {
    this.authService.cerrarSesion();
    this.router.navigate(['/login']);
  }
}
