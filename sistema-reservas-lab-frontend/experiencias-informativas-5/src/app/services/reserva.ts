import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Reserva } from '../models/reserva.model';
import { CrearReservaRequest } from '../models/crear-reserva-request.model';

@Injectable({
  providedIn: 'root'
})
export class ReservaService {

  private apiUrl = 'http://localhost:8080/api/reservas';
  private apiAdminUrl = 'http://localhost:8080/api/admin/reservas';

  constructor(private http: HttpClient) {}

  crear(data: CrearReservaRequest): Observable<Reserva> {
    return this.http.post<Reserva>(this.apiUrl, data);
  }

  listarPorUsuario(idUsuario: number): Observable<Reserva[]> {
    return this.http.get<Reserva[]>(`${this.apiUrl}/usuario/${idUsuario}`);
  }

  cancelar(idReserva: number): Observable<Reserva> {
    return this.http.put<Reserva>(`${this.apiUrl}/${idReserva}/cancelar`, {});
  }

  listarTodasAdmin(): Observable<Reserva[]> {
    return this.http.get<Reserva[]>(this.apiAdminUrl);
  }

  listarActivasAdmin(): Observable<Reserva[]> {
    return this.http.get<Reserva[]>(`${this.apiAdminUrl}/activas`);
  }

  cancelarAdmin(idReserva: number): Observable<Reserva> {
    return this.http.put<Reserva>(`${this.apiAdminUrl}/${idReserva}/cancelar`, {});
  }
}
