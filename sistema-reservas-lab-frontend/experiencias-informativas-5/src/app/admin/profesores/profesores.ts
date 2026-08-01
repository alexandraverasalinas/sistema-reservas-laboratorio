import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { UsuarioService } from '../../services/usuario';
import { AuthService } from '../../services/auth';
import { Usuario } from '../../models/usuario.model';

@Component({
  selector: 'app-profesores',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './profesores.html',
  styleUrl: './profesores.css'
})
export class Profesores implements OnInit {

  usuario: Usuario | null = null;

  profesores: Usuario[] = [];
  profesoresFiltrados: Usuario[] = [];

  filtroTexto: string = '';
  filtroEstado: string = 'TODOS';

  profesorForm: any = {
    nombres: '',
    apellidos: '',
    correo: '',
    password: ''
  };

  modoEdicion: boolean = false;
  idEditando: number | null = null;

  mensajeError: string = '';
  mensajeExito: string = '';
  cargando: boolean = false;

  constructor(
    private usuarioService: UsuarioService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.usuario = this.authService.obtenerUsuario();

    if (!this.usuario || this.usuario.rol !== 'ADMINISTRADOR') {
      this.router.navigate(['/login']);
      return;
    }

    this.listarProfesores();
  }

  listarProfesores(): void {
    this.cargando = true;
    this.mensajeError = '';

    this.usuarioService.listarProfesores().subscribe({
      next: (data) => {
        this.profesores = data || [];
        this.aplicarFiltros();
        this.cargando = false;
      },

      error: (error) => {

        this.profesores = [];
        this.profesoresFiltrados = [];

        this.cargando = false;

        this.mensajeError =
          error.error?.mensaje ||
          'No se pudieron cargar los profesores.';
      }
    });
  }

  aplicarFiltros(): void {

    let resultado = [...this.profesores];

    if (this.filtroEstado !== 'TODOS') {

      const estadoBoolean = this.filtroEstado === 'ACTIVO';

      resultado = resultado.filter(
        profesor => profesor.estado === estadoBoolean
      );
    }

    const texto = this.filtroTexto.trim().toLowerCase();

    if (texto) {

      resultado = resultado.filter(profesor => {

        const nombres = profesor.nombres?.toLowerCase() || '';
        const apellidos = profesor.apellidos?.toLowerCase() || '';
        const correo = profesor.correo?.toLowerCase() || '';
        const rol = profesor.rol?.toLowerCase() || '';
        const id = String(profesor.idUsuario);

        return (
          nombres.includes(texto) ||
          apellidos.includes(texto) ||
          correo.includes(texto) ||
          rol.includes(texto) ||
          id.includes(texto)
        );
      });
    }

    this.profesoresFiltrados = resultado;
  }

  limpiarFiltros(): void {
    this.filtroTexto = '';
    this.filtroEstado = 'TODOS';
    this.aplicarFiltros();
  }

  guardarProfesor(): void {

    this.mensajeError = '';
    this.mensajeExito = '';

    if (
      !this.profesorForm.nombres ||
      !this.profesorForm.apellidos ||
      !this.profesorForm.correo ||
      (!this.modoEdicion && !this.profesorForm.password)
    ) {
      this.mensajeError = 'Complete todos los campos obligatorios.';
      return;
    }

    if (this.modoEdicion && this.idEditando !== null) {

      this.usuarioService.actualizarProfesor(
        this.idEditando,
        this.profesorForm
      ).subscribe({

        next: () => {

          this.mensajeExito =
            'Profesor actualizado correctamente.';

          this.limpiarFormulario();
          this.listarProfesores();
        },

        error: (error) => {

          this.mensajeError =
            error.error?.mensaje ||
            'No se pudo actualizar el profesor.';
        }
      });

    } else {

      this.usuarioService.crearProfesor(
        this.profesorForm
      ).subscribe({

        next: () => {

          this.mensajeExito =
            'Profesor registrado correctamente.';

          this.limpiarFormulario();
          this.listarProfesores();
        },

        error: (error) => {

          this.mensajeError =
            error.error?.mensaje ||
            'No se pudo registrar el profesor.';
        }
      });
    }
  }

  editarProfesor(profesor: Usuario): void {

    this.modoEdicion = true;
    this.idEditando = profesor.idUsuario;

    this.profesorForm = {
      nombres: profesor.nombres,
      apellidos: profesor.apellidos,
      correo: profesor.correo,
      password: ''
    };

    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });
  }

  desactivarProfesor(idUsuario: number): void {

    const confirmar =
      confirm('¿Está seguro de desactivar este profesor?');

    if (!confirmar) return;

    this.usuarioService.desactivarUsuario(idUsuario).subscribe({

      next: () => {

        this.mensajeExito =
          'Profesor desactivado correctamente.';

        this.listarProfesores();
      },

      error: (error) => {

        this.mensajeError =
          error.error?.mensaje ||
          'No se pudo desactivar el profesor.';
      }
    });
  }

  limpiarFormulario(): void {

    this.profesorForm = {
      nombres: '',
      apellidos: '',
      correo: '',
      password: ''
    };

    this.modoEdicion = false;
    this.idEditando = null;
  }

  cerrarSesion(): void {

    this.authService.cerrarSesion();
    this.router.navigate(['/login']);
  }
}