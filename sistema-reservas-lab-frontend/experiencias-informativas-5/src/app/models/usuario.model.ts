export interface Usuario {
  idUsuario: number;
  nombres: string;
  apellidos: string;
  correo: string;
  rol: 'ADMINISTRADOR' | 'PROFESOR' | 'ALUMNO';
  estado: boolean;
}
