import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

  correo: string = '';
  password: string = '';

  tipoAcceso: string = 'ALUMNO';

  mensajeError: string = '';
  cargando: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  seleccionarTipo(tipo: string): void {
    this.tipoAcceso = tipo;
    this.mensajeError = '';
  }

  iniciarSesion(): void {
    this.mensajeError = '';

    if (!this.correo || !this.password) {
      this.mensajeError = 'Ingrese su correo y contraseña.';
      return;
    }

    this.cargando = true;

    this.authService.login({
      correo: this.correo,
      password: this.password
    }).subscribe({
      next: (usuario) => {
        this.cargando = false;

        this.authService.guardarUsuario(usuario);

        if (usuario.rol === 'ADMINISTRADOR') {
          this.router.navigate(['/admin/dashboard']);
        } else if (usuario.rol === 'PROFESOR') {
          this.router.navigate(['/profesor/calendario']);
        } else if (usuario.rol === 'ALUMNO') {
          this.router.navigate(['/alumno/calendario']);
        } else {
          this.mensajeError = 'Rol no reconocido.';
        }
      },
      error: (error) => {
        this.cargando = false;

        if (typeof error.error === 'string') {
          this.mensajeError = error.error;
        } else {
          this.mensajeError = error.error?.mensaje || 'Credenciales incorrectas.';
        }
      }
    });
  }

  obtenerTextoAcceso(): string {
    if (this.tipoAcceso === 'ALUMNO') {
      return 'Acceso para estudiantes registrados';
    }

    return 'Acceso para docentes autorizados';
  }

  obtenerTituloAcceso(): string {
    if (this.tipoAcceso === 'ALUMNO') {
      return 'Portal del Alumno';
    }

    return 'Portal del Profesor';
  }
}
