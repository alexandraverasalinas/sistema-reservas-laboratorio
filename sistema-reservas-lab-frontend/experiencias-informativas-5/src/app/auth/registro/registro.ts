import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './registro.html',
  styleUrl: './registro.css'
})
export class Registro {

  nombres: string = '';
  apellidos: string = '';
  correo: string = '';
  password: string = '';

  mensajeError: string = '';
  mensajeExito: string = '';
  cargando: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  registrar(): void {
    this.mensajeError = '';
    this.mensajeExito = '';

    if (!this.nombres || !this.apellidos || !this.correo || !this.password) {
      this.mensajeError = 'Todos los campos son obligatorios.';
      return;
    }

    this.cargando = true;

    this.authService.registrarAlumno({
      nombres: this.nombres,
      apellidos: this.apellidos,
      correo: this.correo,
      password: this.password
    }).subscribe({
      next: () => {
        this.cargando = false;
        this.mensajeExito = 'Registro exitoso. Ahora puedes iniciar sesión.';

        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1200);
      },
      error: (error) => {
        this.cargando = false;
        this.mensajeError = error.error?.mensaje || 'No se pudo registrar el alumno.';
      }
    });
  }
}
