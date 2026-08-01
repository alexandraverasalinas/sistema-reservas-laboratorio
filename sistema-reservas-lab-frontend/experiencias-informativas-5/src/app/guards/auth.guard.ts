import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth';

export const adminGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const usuario = authService.obtenerUsuario();

  if (usuario && usuario.rol === 'ADMINISTRADOR') {
    return true;
  }

  router.navigate(['/login']);
  return false;
};

export const alumnoGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const usuario = authService.obtenerUsuario();

  if (usuario && usuario.rol === 'ALUMNO') {
    return true;
  }

  router.navigate(['/login']);
  return false;
};

export const profesorGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const usuario = authService.obtenerUsuario();

  if (usuario && usuario.rol === 'PROFESOR') {
    return true;
  }

  router.navigate(['/login']);
  return false;
};
