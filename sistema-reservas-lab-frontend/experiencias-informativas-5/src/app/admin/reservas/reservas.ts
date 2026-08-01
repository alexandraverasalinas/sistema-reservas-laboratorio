import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { AuthService } from '../../services/auth';
import { Reserva } from '../../models/reserva.model';
import { Usuario } from '../../models/usuario.model';

@Component({
  selector: 'app-reservas',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './reservas.html',
  styleUrl: './reservas.css'
})
export class Reservas implements OnInit {

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

    if (!this.usuario || this.usuario.rol !== 'ADMINISTRADOR') {
      this.router.navigate(['/login']);
      return;
    }

    this.listarReservas();
  }

  listarReservas(): void {
    this.cargando = true;
    this.mensajeError = '';
    this.mensajeExito = '';

    fetch('http://localhost:8080/api/admin/reservas')
      .then(response => {
        if (!response.ok) {
          throw new Error('No se pudieron cargar las reservas.');
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
        this.mensajeError = error.message || 'No se pudieron cargar las reservas.';
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
        const usuario = `${reserva.usuario?.nombres || ''} ${reserva.usuario?.apellidos || ''}`.toLowerCase();
        const correo = reserva.usuario?.correo?.toLowerCase() || '';
        const rol = reserva.usuario?.rol?.toLowerCase() || '';
        const laboratorio = reserva.laboratorio?.nombre?.toLowerCase() || '';
        const ubicacion = reserva.laboratorio?.ubicacion?.toLowerCase() || '';
        const motivo = reserva.motivo?.toLowerCase() || '';
        const id = String(reserva.idReserva);
        const tipo = reserva.tipoReserva?.toLowerCase() || '';

        return (
          usuario.includes(texto) ||
          correo.includes(texto) ||
          rol.includes(texto) ||
          laboratorio.includes(texto) ||
          ubicacion.includes(texto) ||
          motivo.includes(texto) ||
          id.includes(texto) ||
          tipo.includes(texto)
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
    const confirmar = confirm('¿Está seguro de cancelar esta reserva?');

    if (!confirmar) return;

    fetch(`http://localhost:8080/api/admin/reservas/${idReserva}/cancelar`, {
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
        this.listarReservas();
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