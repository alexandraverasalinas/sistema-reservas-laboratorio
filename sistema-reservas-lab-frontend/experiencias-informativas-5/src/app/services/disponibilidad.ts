import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Disponibilidad } from '../models/disponibilidad.model';

@Injectable({
  providedIn: 'root'
})
export class DisponibilidadService {

  private apiUrl = 'http://localhost:8080/api/disponibilidad';

  constructor(private http: HttpClient) {}

  consultar(idLaboratorio: number, fecha: string): Observable<Disponibilidad[]> {
    return this.http.get<Disponibilidad[]>(
      `${this.apiUrl}?laboratorio=${idLaboratorio}&fecha=${fecha}`
    );
  }
}
