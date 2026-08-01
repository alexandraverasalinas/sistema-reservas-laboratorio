import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Usuario } from '../models/usuario.model';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private apiAdminUrl = 'http://localhost:8080/api/admin/usuarios';
  private apiUsuarioUrl = 'http://localhost:8080/api/usuarios';

  constructor(private http: HttpClient) {}

  listarProfesores(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.apiAdminUrl}/profesores`);
  }

  crearProfesor(profesor: any): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.apiAdminUrl}/profesores`, profesor);
  }

  actualizarProfesor(id: number, profesor: any): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiAdminUrl}/profesores/${id}`, profesor);
  }

  listarAlumnos(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.apiAdminUrl}/alumnos`);
  }

  actualizarAlumno(id: number, alumno: any): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiAdminUrl}/alumnos/${id}`, alumno);
  }

  desactivarUsuario(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiAdminUrl}/${id}`);
  }

  obtenerPerfil(idUsuario: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUsuarioUrl}/${idUsuario}/perfil`);
  }

  actualizarPerfil(idUsuario: number, data: any): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiUsuarioUrl}/${idUsuario}/perfil`, data);
  }
}
