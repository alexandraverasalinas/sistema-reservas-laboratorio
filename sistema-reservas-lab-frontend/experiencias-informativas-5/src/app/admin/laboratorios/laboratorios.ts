import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { LaboratorioService } from '../../services/laboratorio';
import { AuthService } from '../../services/auth';
import { Laboratorio } from '../../models/laboratorio.model';
import { Usuario } from '../../models/usuario.model';

@Component({
  selector: 'app-laboratorios',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './laboratorios.html',
  styleUrl: './laboratorios.css'
})
export class Laboratorios implements OnInit {

  usuario: Usuario | null = null;

  laboratorios: Laboratorio[] = [];

  laboratorioForm: Laboratorio = {
    nombre: '',
    ubicacion: '',
    capacidad: 0,
    descripcion: ''
  };

  modoEdicion: boolean = false;
  idEditando: number | null = null;

  mensajeError: string = '';
  mensajeExito: string = '';
  cargando: boolean = false;

  constructor(
    private laboratorioService: LaboratorioService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.usuario = this.authService.obtenerUsuario();

    if (!this.usuario || this.usuario.rol !== 'ADMINISTRADOR') {
      this.router.navigate(['/login']);
      return;
    }

    this.listarLaboratorios();
  }

  listarLaboratorios(): void {
    this.cargando = true;

    this.laboratorioService.listarAdmin().subscribe({
      next: (data) => {
        this.laboratorios = data;
        this.cargando = false;
      },
      error: (error) => {
        this.cargando = false;
        this.mensajeError = error.error?.mensaje || 'No se pudieron cargar los laboratorios.';
      }
    });
  }

  guardarLaboratorio(): void {
    this.mensajeError = '';
    this.mensajeExito = '';

    if (!this.laboratorioForm.nombre || !this.laboratorioForm.ubicacion || !this.laboratorioForm.capacidad) {
      this.mensajeError = 'Complete los campos obligatorios.';
      return;
    }

    if (this.modoEdicion && this.idEditando !== null) {
      this.laboratorioService.actualizar(this.idEditando, this.laboratorioForm).subscribe({
        next: () => {
          this.mensajeExito = 'Laboratorio actualizado correctamente.';
          this.limpiarFormulario();
          this.listarLaboratorios();
        },
        error: (error) => {
          this.mensajeError = error.error?.mensaje || 'No se pudo actualizar el laboratorio.';
        }
      });
    } else {
      this.laboratorioService.crear(this.laboratorioForm).subscribe({
        next: () => {
          this.mensajeExito = 'Laboratorio registrado correctamente.';
          this.limpiarFormulario();
          this.listarLaboratorios();
        },
        error: (error) => {
          this.mensajeError = error.error?.mensaje || 'No se pudo registrar el laboratorio.';
        }
      });
    }
  }

  editarLaboratorio(laboratorio: Laboratorio): void {
    this.modoEdicion = true;
    this.idEditando = laboratorio.idLaboratorio || null;

    this.laboratorioForm = {
      nombre: laboratorio.nombre,
      ubicacion: laboratorio.ubicacion,
      capacidad: laboratorio.capacidad,
      descripcion: laboratorio.descripcion
    };

    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  desactivarLaboratorio(idLaboratorio?: number): void {
    if (!idLaboratorio) return;

    const confirmar = confirm('¿Está seguro de desactivar este laboratorio?');

    if (!confirmar) return;

    this.laboratorioService.eliminar(idLaboratorio).subscribe({
      next: () => {
        this.mensajeExito = 'Laboratorio desactivado correctamente.';
        this.listarLaboratorios();
      },
      error: (error) => {
        this.mensajeError = error.error?.mensaje || 'No se pudo desactivar el laboratorio.';
      }
    });
  }

  limpiarFormulario(): void {
    this.laboratorioForm = {
      nombre: '',
      ubicacion: '',
      capacidad: 0,
      descripcion: ''
    };

    this.modoEdicion = false;
    this.idEditando = null;
  }

  cerrarSesion(): void {
    this.authService.cerrarSesion();
    this.router.navigate(['/login']);
  }
}
