import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { AuthService } from '../../services/auth';
import { Reserva } from '../../models/reserva.model';
import { Usuario } from '../../models/usuario.model';

@Component({
  selector: 'app-mis-reservas',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './mis-reservas.html',
  styleUrl: './mis-reservas.css'
})
export class MisReservas implements OnInit {

  usuario: Usuario | null = null;

  reservas: Reserva[] = [];
  reservasFiltradas: Reserva[] = [];

  filtroEstado: string = 'TODOS';
  filtroTexto: string = '';
  filtroFecha: string = '';

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

    this.listarMisReservas();
  }

  listarMisReservas(): void {
    if (!this.usuario) {
      this.mensajeError = 'No se encontró el usuario.';
      return;
    }

    this.cargando = true;
    this.mensajeError = '';
    this.mensajeExito = '';
    this.reservas = [];
    this.reservasFiltradas = [];
    this.cdr.detectChanges();

    const url = `http://localhost:8080/api/reservas/usuario/${this.usuario.idUsuario}`;

    fetch(url)
      .then(response => {
        if (!response.ok) {
          throw new Error('No se pudieron cargar tus reservas.');
        }
        return response.json();
      })
      .then(data => {
        this.reservas = data || [];
        this.aplicarFiltros();
        this.cargando = false;
        this.cdr.detectChanges();
      })
      .catch(error => {
        console.error('Error al cargar reservas:', error);
        this.reservas = [];
        this.reservasFiltradas = [];
        this.cargando = false;
        this.mensajeError = error.message || 'No se pudieron cargar tus reservas.';
        this.cdr.detectChanges();
      });
  }

  aplicarFiltros(): void {
    let resultado = [...this.reservas];

    if (this.filtroEstado !== 'TODOS') {
      resultado = resultado.filter(reserva => reserva.estado === this.filtroEstado);
    }

    if (this.filtroFecha) {
      resultado = resultado.filter(reserva => reserva.fecha === this.filtroFecha);
    }

    const texto = this.filtroTexto.trim().toLowerCase();

    if (texto) {
      resultado = resultado.filter(reserva => {
        const id = String(reserva.idReserva);
        const laboratorio = reserva.laboratorio?.nombre?.toLowerCase() || '';
        const ubicacion = reserva.laboratorio?.ubicacion?.toLowerCase() || '';
        const motivo = reserva.motivo?.toLowerCase() || '';

        return (
          id.includes(texto) ||
          laboratorio.includes(texto) ||
          ubicacion.includes(texto) ||
          motivo.includes(texto)
        );
      });
    }

    this.reservasFiltradas = resultado;
  }

  limpiarFiltros(): void {
    this.filtroEstado = 'TODOS';
    this.filtroTexto = '';
    this.filtroFecha = '';
    this.aplicarFiltros();
  }

  cancelarReserva(idReserva: number): void {
    const confirmar = confirm('¿Estás seguro de cancelar esta reserva?');

    if (!confirmar) return;

    fetch(`http://localhost:8080/api/reservas/${idReserva}/cancelar`, {
      method: 'PUT'
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('No se pudo cancelar la reserva.');
        }
        return response.json();
      })
      .then(() => {
        this.mensajeExito = 'Reserva cancelada correctamente.';
        this.listarMisReservas();
      })
      .catch(error => {
        console.error('Error al cancelar reserva:', error);
        this.mensajeError = error.message || 'No se pudo cancelar la reserva.';
        this.cdr.detectChanges();
      });
  }

  contarPorEstado(estado: string): number {
    return this.reservas.filter(reserva => reserva.estado === estado).length;
  }

  cerrarSesion(): void {
    this.authService.cerrarSesion();
    this.router.navigate(['/login']);
  }
}
