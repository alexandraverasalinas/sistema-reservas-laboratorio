import { Laboratorio } from './laboratorio.model';

export interface HorarioLaboratorio {
  idHorario?: number;
  laboratorio?: Laboratorio;
  diaSemana: 'LUNES' | 'MARTES' | 'MIERCOLES' | 'JUEVES' | 'VIERNES' | 'SABADO' | 'DOMINGO';
  horaInicio: string;
  horaFin: string;
  estado?: boolean;
}
