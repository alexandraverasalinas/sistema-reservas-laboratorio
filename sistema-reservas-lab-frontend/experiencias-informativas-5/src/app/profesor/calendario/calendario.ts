import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { AuthService } from '../../services/auth';
import { Laboratorio } from '../../models/laboratorio.model';
import { Disponibilidad } from '../../models/disponibilidad.model';
import { Usuario } from '../../models/usuario.model';

@Component({
  selector: 'app-calendario',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './calendario.html',
  styleUrl: './calendario.css'
})
export class Calendario implements OnInit {

  usuario: Usuario | null = null;

  laboratorios: Laboratorio[] = [];
  disponibilidad: Disponibilidad[] = [];

  idLaboratorioSeleccionado: number | null = null;
  fechaSeleccionada: string = '';
  motivo: string = '';

  mensajeError = '';
  mensajeExito = '';
  cargando = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.usuario = this.authService.obtenerUsuario();

    if (!this.usuario || this.usuario.rol !== 'PROFESOR') {
      this.router.navigate(['/login']);
      return;
    }

    this.fechaSeleccionada = this.obtenerFechaActual();
    this.listarLaboratorios();
  }

  listarLaboratorios(): void {
    fetch('http://localhost:8080/api/laboratorios')
      .then(response => response.json())
      .then(data => {
        this.laboratorios = data;
        this.cdr.detectChanges();
      })
      .catch(error => {
        console.error('Error al cargar laboratorios:', error);
        this.mensajeError = 'No se pudieron cargar los laboratorios.';
        this.cdr.detectChanges();
      });
  }

  consultarDisponibilidad(): void {
    this.mensajeError = '';
    this.mensajeExito = '';
    this.disponibilidad = [];

    if (!this.idLaboratorioSeleccionado || !this.fechaSeleccionada) {
      this.mensajeError = 'Seleccione un laboratorio y una fecha.';
      return;
    }

    this.cargando = true;
    this.cdr.detectChanges();

    const url = `http://localhost:8080/api/disponibilidad?laboratorio=${this.idLaboratorioSeleccionado}&fecha=${this.fechaSeleccionada}`;

    fetch(url)
      .then(response => {
        if (!response.ok) {
          throw new Error('No se pudo consultar disponibilidad.');
        }
        return response.json();
      })
      .then(data => {
        this.disponibilidad = data || [];
        this.cargando = false;

        if (this.disponibilidad.length === 0) {
          this.mensajeError = 'No hay bloques de horario configurados para esta fecha.';
        }

        this.cdr.detectChanges();
      })
      .catch(error => {
        console.error('Error al consultar disponibilidad:', error);
        this.cargando = false;
        this.mensajeError = error.message || 'No se pudo consultar la disponibilidad.';
        this.cdr.detectChanges();
      });
  }

  reservar(bloque: Disponibilidad): void {
    this.mensajeError = '';
    this.mensajeExito = '';

    if (!this.usuario) {
      this.mensajeError = 'Debe iniciar sesión para reservar.';
      return;
    }

    if (!this.motivo || !this.motivo.trim()) {
      this.mensajeError = 'Ingrese el motivo de la reserva.';
      return;
    }

    const confirmar = confirm(
      `¿Deseas reservar de ${bloque.horaInicio} a ${bloque.horaFin}?`
    );

    if (!confirmar) return;

    const reserva = {
      idUsuario: this.usuario.idUsuario,
      idLaboratorio: bloque.idLaboratorio,
      fecha: bloque.fecha,
      horaInicio: bloque.horaInicio,
      horaFin: bloque.horaFin,
      motivo: this.motivo,
      tipoReserva: 'PROFESOR'
    };

    fetch('http://localhost:8080/api/reservas', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(reserva)
    })
      .then(response => {
        if (!response.ok) {
          return response.text().then(text => {
            throw new Error(text || 'No se pudo registrar la reserva.');
          });
        }
        return response.json();
      })
      .then(() => {
        this.mensajeExito = 'Reserva registrada correctamente.';
        this.motivo = '';
        this.consultarDisponibilidad();
        this.cdr.detectChanges();
      })
      .catch(error => {
        console.error('Error al reservar:', error);
        this.mensajeError = error.message || 'No se pudo registrar la reserva.';
        this.cdr.detectChanges();
      });
  }

  cerrarSesion(): void {
    this.authService.cerrarSesion();
    this.router.navigate(['/login']);
  }

  private obtenerFechaActual(): string {
    const hoy = new Date();
    const yyyy = hoy.getFullYear();
    const mm = String(hoy.getMonth() + 1).padStart(2, '0');
    const dd = String(hoy.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`;
  }
}