import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { DashboardAdmin } from '../models/dashboard-admin.model';

@Injectable({
  providedIn: 'root'
})
export class DashboardAdminService {

  private apiUrl = 'http://localhost:8080/api/admin/dashboard';

  constructor(private http: HttpClient) {}

  obtenerResumen(): Observable<DashboardAdmin> {
    return this.http.get<DashboardAdmin>(this.apiUrl);
  }
}
