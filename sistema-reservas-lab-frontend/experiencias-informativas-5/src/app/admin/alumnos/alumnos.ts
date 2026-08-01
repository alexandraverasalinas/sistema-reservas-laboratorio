import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { AuthService } from '../../services/auth';
import { Usuario } from '../../models/usuario.model';

@Component({
  selector: 'app-alumnos',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './alumnos.html',
  styleUrl: './alumnos.css'
})
export class Alumnos implements OnInit {

  usuario: Usuario | null = null;

  alumnos: Usuario[] = [];
  alumnosFiltrados: Usuario[] = [];

  filtroTexto: string = '';
  filtroEstado: string = 'TODOS';

  cargando = false;

  mensajeError = '';
  mensajeExito = '';

  modoEdicion = false;
  idAlumnoEditando: number | null = null;

  alumnoForm: any = {
    nombres: '',
    apellidos: '',
    correo: '',
    password: ''
  };

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

    this.listarAlumnos();
  }

  listarAlumnos(): void {

    this.cargando = true;

    this.mensajeError = '';
    this.mensajeExito = '';

    this.alumnos = [];
    this.alumnosFiltrados = [];

    this.cdr.detectChanges();

    fetch('http://localhost:8080/api/admin/usuarios/alumnos')

      .then(response => {

        if (!response.ok) {
          throw new Error(
            'No se pudieron cargar los alumnos.'
          );
        }

        return response.json();
      })

      .then(data => {

        this.alumnos = data || [];

        this.aplicarFiltros();

        this.cargando = false;

        this.cdr.detectChanges();
      })

      .catch(error => {

        console.error(
          'Error al listar alumnos:',
          error
        );

        this.alumnos = [];
        this.alumnosFiltrados = [];

        this.cargando = false;

        this.mensajeError =
          error.message ||
          'No se pudieron cargar los alumnos.';

        this.cdr.detectChanges();
      });
  }

  aplicarFiltros(): void {

    let resultado = [...this.alumnos];

    if (this.filtroEstado !== 'TODOS') {

      const estadoBoolean =
        this.filtroEstado === 'ACTIVO';

      resultado = resultado.filter(
        alumno => alumno.estado === estadoBoolean
      );
    }

    const texto =
      this.filtroTexto.trim().toLowerCase();

    if (texto) {

      resultado = resultado.filter(alumno => {

        const nombres =
          alumno.nombres?.toLowerCase() || '';

        const apellidos =
          alumno.apellidos?.toLowerCase() || '';

        const correo =
          alumno.correo?.toLowerCase() || '';

        const rol =
          alumno.rol?.toLowerCase() || '';

        const id =
          String(alumno.idUsuario);

        return (
          nombres.includes(texto) ||
          apellidos.includes(texto) ||
          correo.includes(texto) ||
          rol.includes(texto) ||
          id.includes(texto)
        );
      });
    }

    this.alumnosFiltrados = resultado;
  }

  limpiarFiltros(): void {

    this.filtroTexto = '';
    this.filtroEstado = 'TODOS';

    this.aplicarFiltros();
  }

  guardarAlumno(): void {

    this.mensajeError = '';
    this.mensajeExito = '';

    if (
      !this.alumnoForm.nombres ||
      !this.alumnoForm.apellidos ||
      !this.alumnoForm.correo
    ) {

      this.mensajeError =
        'Complete nombres, apellidos y correo.';

      return;
    }

    if (this.modoEdicion) {
      this.actualizarAlumno();
    } else {
      this.crearAlumno();
    }
  }

  crearAlumno(): void {

    const alumno = {

      nombres: this.alumnoForm.nombres,
      apellidos: this.alumnoForm.apellidos,
      correo: this.alumnoForm.correo,

      password:
        this.alumnoForm.password || '123456',

      rol: 'ALUMNO',
      estado: true
    };

    fetch(
      'http://localhost:8080/api/admin/usuarios/alumnos',
      {
        method: 'POST',

        headers: {
          'Content-Type': 'application/json'
        },

        body: JSON.stringify(alumno)
      }
    )

      .then(response => {

        if (!response.ok) {

          return response.text().then(text => {

            throw new Error(
              text ||
              'No se pudo registrar el alumno.'
            );
          });
        }

        return response.json();
      })

      .then(() => {

        this.mensajeExito =
          'Alumno registrado correctamente.';

        this.limpiarFormulario();
        this.listarAlumnos();
      })

      .catch(error => {

        console.error(
          'Error al registrar alumno:',
          error
        );

        this.mensajeError =
          error.message ||
          'No se pudo registrar el alumno.';

        this.cdr.detectChanges();
      });
  }

  editarAlumno(alumno: Usuario): void {

    this.modoEdicion = true;

    this.idAlumnoEditando =
      alumno.idUsuario;

    this.alumnoForm = {

      nombres: alumno.nombres,
      apellidos: alumno.apellidos,
      correo: alumno.correo,
      password: ''
    };

    this.mensajeError = '';
    this.mensajeExito = '';
  }

  actualizarAlumno(): void {

    if (!this.idAlumnoEditando) {

      this.mensajeError =
        'Seleccione un alumno para actualizar.';

      return;
    }

    const alumno = {

      nombres: this.alumnoForm.nombres,
      apellidos: this.alumnoForm.apellidos,
      correo: this.alumnoForm.correo,
      password: this.alumnoForm.password,

      rol: 'ALUMNO',
      estado: true
    };

    fetch(
      `http://localhost:8080/api/admin/usuarios/alumnos/${this.idAlumnoEditando}`,
      {
        method: 'PUT',

        headers: {
          'Content-Type': 'application/json'
        },

        body: JSON.stringify(alumno)
      }
    )

      .then(response => {

        if (!response.ok) {

          return response.text().then(text => {

            throw new Error(
              text ||
              'No se pudo actualizar el alumno.'
            );
          });
        }

        return response.json();
      })

      .then(() => {

        this.mensajeExito =
          'Alumno actualizado correctamente.';

        this.limpiarFormulario();
        this.listarAlumnos();
      })

      .catch(error => {

        console.error(
          'Error al actualizar alumno:',
          error
        );

        this.mensajeError =
          error.message ||
          'No se pudo actualizar el alumno.';

        this.cdr.detectChanges();
      });
  }

  desactivarAlumno(idUsuario: number): void {

    const confirmar =
      confirm('¿Está seguro de desactivar este alumno?');

    if (!confirmar) return;

    fetch(
      `http://localhost:8080/api/admin/usuarios/${idUsuario}`,
      {
        method: 'DELETE'
      }
    )

      .then(response => {

        if (!response.ok) {

          return response.text().then(text => {

            throw new Error(
              text ||
              'No se pudo desactivar el alumno.'
            );
          });
        }

        return null;
      })

      .then(() => {

        this.mensajeExito =
          'Alumno desactivado correctamente.';

        this.listarAlumnos();
      })

      .catch(error => {

        console.error(
          'Error al desactivar alumno:',
          error
        );

        this.mensajeError =
          error.message ||
          'No se pudo desactivar el alumno.';

        this.cdr.detectChanges();
      });
  }

  limpiarFormulario(): void {

    this.modoEdicion = false;
    this.idAlumnoEditando = null;

    this.alumnoForm = {
      nombres: '',
      apellidos: '',
      correo: '',
      password: ''
    };

    this.mensajeError = '';
  }

  cerrarSesion(): void {

    this.authService.cerrarSesion();

    this.router.navigate(['/login']);
  }
}