export interface CrearReservaRequest {
  idUsuario: number;
  idLaboratorio: number;
  fecha: string;
  horaInicio: string;
  horaFin: string;
  motivo: string;
  tipoReserva: string;
}