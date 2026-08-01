import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { LoginRequest } from '../models/login-request.model';
import { RegistroRequest } from '../models/registro-request.model';
import { Usuario } from '../models/usuario.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080/api/auth';
  private esNavegador: boolean;

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.esNavegador = isPlatformBrowser(this.platformId);
  }

  login(data: LoginRequest): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.apiUrl}/login`, data);
  }

  registrarAlumno(data: RegistroRequest): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.apiUrl}/registro`, data);
  }

  guardarUsuario(usuario: Usuario): void {
    if (!this.esNavegador) return;

    window.localStorage.setItem('usuario', JSON.stringify(usuario));
  }

  obtenerUsuario(): Usuario | null {
    if (!this.esNavegador) return null;

    const data = window.localStorage.getItem('usuario');
    return data ? JSON.parse(data) : null;
  }

  obtenerIdUsuario(): number | null {
    const usuario = this.obtenerUsuario();
    return usuario ? usuario.idUsuario : null;
  }

  obtenerRol(): string | null {
    const usuario = this.obtenerUsuario();
    return usuario ? usuario.rol : null;
  }

  estaLogueado(): boolean {
    if (!this.esNavegador) return false;

    return !!window.localStorage.getItem('usuario');
  }

  cerrarSesion(): void {
    if (!this.esNavegador) return;

    window.localStorage.removeItem('usuario');
  }
}
