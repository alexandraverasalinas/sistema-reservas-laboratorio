import { Routes } from '@angular/router';
import { adminGuard, alumnoGuard, profesorGuard } from './guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./auth/login/login').then(m => m.Login)
  },
  {
    path: 'registro',
    loadComponent: () =>
      import('./auth/registro/registro').then(m => m.Registro)
  },

  // ADMINISTRADOR
  {
    path: 'admin/dashboard',
    canActivate: [adminGuard],
    loadComponent: () =>
      import('./admin/dashboard/dashboard').then(m => m.Dashboard)
  },
  {
    path: 'admin/laboratorios',
    canActivate: [adminGuard],
    loadComponent: () =>
      import('./admin/laboratorios/laboratorios').then(m => m.Laboratorios)
  },
  {
    path: 'admin/horarios',
    canActivate: [adminGuard],
    loadComponent: () =>
      import('./admin/horarios/horarios').then(m => m.Horarios)
  },
  {
    path: 'admin/profesores',
    canActivate: [adminGuard],
    loadComponent: () =>
      import('./admin/profesores/profesores').then(m => m.Profesores)
  },
  {
    path: 'admin/alumnos',
    canActivate: [adminGuard],
    loadComponent: () =>
      import('./admin/alumnos/alumnos').then(m => m.Alumnos)
  },
  {
    path: 'admin/reservas',
    canActivate: [adminGuard],
    loadComponent: () =>
      import('./admin/reservas/reservas').then(m => m.Reservas)
  },

  // ALUMNO
  {
    path: 'alumno/calendario',
    canActivate: [alumnoGuard],
    loadComponent: () =>
      import('./alumno/calendario/calendario').then(m => m.Calendario)
  },
  {
    path: 'alumno/mis-reservas',
    canActivate: [alumnoGuard],
    loadComponent: () =>
      import('./alumno/mis-reservas/mis-reservas').then(m => m.MisReservas)
  },
  {
    path: 'alumno/perfil',
    canActivate: [alumnoGuard],
    loadComponent: () =>
      import('./alumno/perfil/perfil').then(m => m.Perfil)
  },

  // PROFESOR
  {
    path: 'profesor/calendario',
    canActivate: [profesorGuard],
    loadComponent: () =>
      import('./profesor/calendario/calendario').then(m => m.Calendario)
  },
  {
    path: 'profesor/mis-reservas',
    canActivate: [profesorGuard],
    loadComponent: () =>
      import('./profesor/mis-reservas/mis-reservas').then(m => m.MisReservas)
  },
  {
    path: 'profesor/perfil',
    canActivate: [profesorGuard],
    loadComponent: () =>
      import('./profesor/perfil/perfil').then(m => m.Perfil)
  },

  {
    path: '**',
    redirectTo: 'login'
  }
];
