import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { HorarioService } from '../../services/horario';
import { LaboratorioService } from '../../services/laboratorio';
import { AuthService } from '../../services/auth';

import { HorarioLaboratorio } from '../../models/horario.model';
import { Laboratorio } from '../../models/laboratorio.model';
import { Usuario } from '../../models/usuario.model';

@Component({
  selector: 'app-horarios',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './horarios.html',
  styleUrl: './horarios.css'
})
export class Horarios implements OnInit {

  usuario: Usuario | null = null;

  laboratorios: Laboratorio[] = [];
  horarios: HorarioLaboratorio[] = [];
  horariosPaginados: HorarioLaboratorio[] = [];

  paginaActual: number = 1;
  registrosPorPagina: number = 10;
  totalPaginas: number = 0;

  diasSemana = [
    'LUNES',
    'MARTES',
    'MIERCOLES',
    'JUEVES',
    'VIERNES',
    'SABADO',
    'DOMINGO'
  ];

  idLaboratorioSeleccionado: number | null = null;

  horarioForm: HorarioLaboratorio = {
    diaSemana: 'LUNES',
    horaInicio: '',
    horaFin: ''
  };

  modoEdicion: boolean = false;
  idEditando: number | null = null;

  mensajeError: string = '';
  mensajeExito: string = '';
  cargando: boolean = false;

  constructor(
    private horarioService: HorarioService,
    private laboratorioService: LaboratorioService,
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

    this.listarLaboratorios();
    this.listarHorarios();
  }

  listarLaboratorios(): void {
    this.laboratorioService.listarAdmin().subscribe({
      next: (data) => {
        this.laboratorios = data;
      },
      error: (error) => {
        this.mensajeError = error.error?.mensaje || 'No se pudieron cargar los laboratorios.';
      }
    });
  }

  listarHorarios(): void {
    this.cargando = true;

    this.horarioService.listar().subscribe({
      next: (data) => {
        this.horarios = data;
        this.totalPaginas = Math.ceil(this.horarios.length / this.registrosPorPagina);
        this.paginaActual = 1;
        this.actualizarPagina();
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        this.cargando = false;
        this.mensajeError = error.error?.mensaje || 'No se pudieron cargar los horarios.';
        this.cdr.detectChanges();
      }
    });
  }

  actualizarPagina(): void {
    const inicio = (this.paginaActual - 1) * this.registrosPorPagina;
    const fin = inicio + this.registrosPorPagina;
    this.horariosPaginados = this.horarios.slice(inicio, fin);
  }

  cambiarPagina(pagina: number): void {
    if (pagina >= 1 && pagina <= this.totalPaginas) {
      this.paginaActual = pagina;
      this.actualizarPagina();
    }
  }

  guardarHorario(): void {
    this.mensajeError = '';
    this.mensajeExito = '';

    if (!this.horarioForm.diaSemana || !this.horarioForm.horaInicio || !this.horarioForm.horaFin) {
      this.mensajeError = 'Complete todos los campos del horario.';
      return;
    }

    if (!this.modoEdicion && !this.idLaboratorioSeleccionado) {
      this.mensajeError = 'Seleccione un laboratorio.';
      return;
    }

    if (this.modoEdicion && this.idEditando !== null) {
      this.horarioService.actualizar(this.idEditando, this.horarioForm).subscribe({
        next: () => {
          this.mensajeExito = 'Horario actualizado correctamente.';
          this.limpiarFormulario();
          this.listarHorarios();
        },
        error: (error) => {
          this.mensajeError = error.error?.mensaje || 'No se pudo actualizar el horario.';
        }
      });
    } else {
      this.horarioService.crear(this.idLaboratorioSeleccionado!, this.horarioForm).subscribe({
        next: () => {
          this.mensajeExito = 'Horario registrado correctamente.';
          this.limpiarFormulario();
          this.listarHorarios();
        },
        error: (error) => {
          this.mensajeError = error.error?.mensaje || 'No se pudo registrar el horario.';
        }
      });
    }
  }

  editarHorario(horario: HorarioLaboratorio): void {
    this.modoEdicion = true;
    this.idEditando = horario.idHorario || null;
    this.idLaboratorioSeleccionado = horario.laboratorio?.idLaboratorio || null;

    this.horarioForm = {
      diaSemana: horario.diaSemana,
      horaInicio: horario.horaInicio,
      horaFin: horario.horaFin
    };

    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  desactivarHorario(idHorario?: number): void {
    if (!idHorario) return;

    const confirmar = confirm('¿Está seguro de desactivar este horario?');

    if (!confirmar) return;

    this.horarioService.eliminar(idHorario).subscribe({
      next: () => {
        this.mensajeExito = 'Horario desactivado correctamente.';
        this.listarHorarios();
      },
      error: (error) => {
        this.mensajeError = error.error?.mensaje || 'No se pudo desactivar el horario.';
      }
    });
  }

  limpiarFormulario(): void {
    this.idLaboratorioSeleccionado = null;

    this.horarioForm = {
      diaSemana: 'LUNES',
      horaInicio: '',
      horaFin: ''
    };

    this.modoEdicion = false;
    this.idEditando = null;
  }

  cerrarSesion(): void {
    this.authService.cerrarSesion();
    this.router.navigate(['/login']);
  }
}