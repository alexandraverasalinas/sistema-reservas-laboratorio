import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Laboratorio } from '../models/laboratorio.model';

@Injectable({
  providedIn: 'root'
})
export class LaboratorioService {

  private apiAdminUrl = 'http://localhost:8080/api/admin/laboratorios';
  private apiPublicUrl = 'http://localhost:8080/api/laboratorios';

  constructor(private http: HttpClient) {}

  listar(): Observable<Laboratorio[]> {
    return this.http.get<Laboratorio[]>(this.apiPublicUrl);
  }

  listarAdmin(): Observable<Laboratorio[]> {
    return this.http.get<Laboratorio[]>(this.apiAdminUrl);
  }

  crear(laboratorio: Laboratorio): Observable<Laboratorio> {
    return this.http.post<Laboratorio>(this.apiAdminUrl, laboratorio);
  }

  actualizar(id: number, laboratorio: Laboratorio): Observable<Laboratorio> {
    return this.http.put<Laboratorio>(`${this.apiAdminUrl}/${id}`, laboratorio);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiAdminUrl}/${id}`);
  }
}
