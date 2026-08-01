export interface Disponibilidad {
  idLaboratorio: number;
  nombreLaboratorio: string;
  fecha: string;
  horaInicio: string;
  horaFin: string;
  estado: 'DISPONIBLE' | 'OCUPADO';
  idReserva: number | null;
  tipoReserva: string | null;
  cuposOcupados: number;
  cuposTotales: number;
  alumnos: string[];
}