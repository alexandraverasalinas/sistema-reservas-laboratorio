import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { HorarioLaboratorio } from '../models/horario.model';

@Injectable({
  providedIn: 'root'
})
export class HorarioService {

  private apiAdminUrl = 'http://localhost:8080/api/admin/horarios';
  private apiPublicUrl = 'http://localhost:8080/api/laboratorios';

  constructor(private http: HttpClient) {}

  listar(): Observable<HorarioLaboratorio[]> {
    return this.http.get<HorarioLaboratorio[]>(this.apiAdminUrl);
  }

  listarPorLaboratorio(idLaboratorio: number): Observable<HorarioLaboratorio[]> {
    return this.http.get<HorarioLaboratorio[]>(
      `${this.apiPublicUrl}/${idLaboratorio}/horarios`
    );
  }

  crear(idLaboratorio: number, horario: HorarioLaboratorio): Observable<HorarioLaboratorio> {
    return this.http.post<HorarioLaboratorio>(
      `${this.apiAdminUrl}/laboratorio/${idLaboratorio}`,
      horario
    );
  }

  actualizar(idHorario: number, horario: HorarioLaboratorio): Observable<HorarioLaboratorio> {
    return this.http.put<HorarioLaboratorio>(
      `${this.apiAdminUrl}/${idHorario}`,
      horario
    );
  }

  eliminar(idHorario: number): Observable<void> {
    return this.http.delete<void>(`${this.apiAdminUrl}/${idHorario}`);
  }
}
