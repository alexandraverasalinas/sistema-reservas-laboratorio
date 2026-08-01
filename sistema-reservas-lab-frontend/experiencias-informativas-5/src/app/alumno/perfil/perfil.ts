import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { AuthService } from '../../services/auth';
import { UsuarioService } from '../../services/usuario';
import { Usuario } from '../../models/usuario.model';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './perfil.html',
  styleUrl: './perfil.css'
})
export class Perfil implements OnInit {

  usuario: Usuario | null = null;

  perfilForm: any = {
    nombres: '',
    apellidos: '',
    correo: '',
    password: ''
  };

  mensajeError = '';
  mensajeExito = '';
  cargando = false;

  constructor(
    private authService: AuthService,
    private usuarioService: UsuarioService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.usuario = this.authService.obtenerUsuario();

    if (!this.usuario || this.usuario.rol !== 'ALUMNO') {
      this.router.navigate(['/login']);
      return;
    }

    this.cargarDatosLocales();
  }

  cargarDatosLocales(): void {
    if (!this.usuario) return;

    this.cargando = false;

    this.perfilForm = {
      nombres: this.usuario.nombres,
      apellidos: this.usuario.apellidos,
      correo: this.usuario.correo,
      password: ''
    };
  }

  actualizarPerfil(): void {
    this.mensajeError = '';
    this.mensajeExito = '';

    if (!this.usuario) {
      this.mensajeError = 'No se encontró el usuario.';
      return;
    }

    if (!this.perfilForm.nombres || !this.perfilForm.apellidos || !this.perfilForm.correo) {
      this.mensajeError = 'Complete los campos obligatorios.';
      return;
    }

    this.cargando = true;

    this.usuarioService.actualizarPerfil(this.usuario.idUsuario, this.perfilForm).subscribe({
      next: (data) => {
        this.usuario = data;
        this.authService.guardarUsuario(data);

        this.perfilForm = {
          nombres: data.nombres,
          apellidos: data.apellidos,
          correo: data.correo,
          password: ''
        };

        this.mensajeExito = 'Perfil actualizado correctamente.';
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error al actualizar perfil:', error);

        this.cargando = false;

        if (typeof error.error === 'string') {
          this.mensajeError = error.error;
        } else {
          this.mensajeError = error.error?.mensaje || 'No se pudo actualizar el perfil.';
        }
      }
    });
  }

  cerrarSesion(): void {
    this.authService.cerrarSesion();
    this.router.navigate(['/login']);
  }
}
