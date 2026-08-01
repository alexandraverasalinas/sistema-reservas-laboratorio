import { Usuario } from './usuario.model';
import { Laboratorio } from './laboratorio.model';

export interface Reserva {
  idReserva: number;
  usuario: Usuario;
  laboratorio: Laboratorio;
  fecha: string;
  horaInicio: string;
  horaFin: string;
  motivo: string;
  estado: 'RESERVADO' | 'CANCELADO' | 'FINALIZADO';
  tipoReserva: 'ALUMNO' | 'PROFESOR' | 'ADMINISTRADOR';
  cuposOcupados: number;
}