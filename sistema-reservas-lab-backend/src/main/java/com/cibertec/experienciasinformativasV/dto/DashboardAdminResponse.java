package com.cibertec.experienciasinformativasV.dto;

public class DashboardAdminResponse {

    private long totalLaboratoriosActivos;
    private long totalProfesoresActivos;
    private long totalAlumnosActivos;
    private long totalReservas;
    private long reservasActivas;
    private long reservasCanceladas;
    private long reservasFinalizadas;
    private long reservasDelDia;

    public DashboardAdminResponse(
            long totalLaboratoriosActivos,
            long totalProfesoresActivos,
            long totalAlumnosActivos,
            long totalReservas,
            long reservasActivas,
            long reservasCanceladas,
            long reservasFinalizadas,
            long reservasDelDia
    ) {
        this.totalLaboratoriosActivos = totalLaboratoriosActivos;
        this.totalProfesoresActivos = totalProfesoresActivos;
        this.totalAlumnosActivos = totalAlumnosActivos;
        this.totalReservas = totalReservas;
        this.reservasActivas = reservasActivas;
        this.reservasCanceladas = reservasCanceladas;
        this.reservasFinalizadas = reservasFinalizadas;
        this.reservasDelDia = reservasDelDia;
    }

    public long getTotalLaboratoriosActivos() {
        return totalLaboratoriosActivos;
    }

    public void setTotalLaboratoriosActivos(long totalLaboratoriosActivos) {
        this.totalLaboratoriosActivos = totalLaboratoriosActivos;
    }

    public long getTotalProfesoresActivos() {
        return totalProfesoresActivos;
    }

    public void setTotalProfesoresActivos(long totalProfesoresActivos) {
        this.totalProfesoresActivos = totalProfesoresActivos;
    }

    public long getTotalAlumnosActivos() {
        return totalAlumnosActivos;
    }

    public void setTotalAlumnosActivos(long totalAlumnosActivos) {
        this.totalAlumnosActivos = totalAlumnosActivos;
    }

    public long getTotalReservas() {
        return totalReservas;
    }

    public void setTotalReservas(long totalReservas) {
        this.totalReservas = totalReservas;
    }

    public long getReservasActivas() {
        return reservasActivas;
    }

    public void setReservasActivas(long reservasActivas) {
        this.reservasActivas = reservasActivas;
    }

    public long getReservasCanceladas() {
        return reservasCanceladas;
    }

    public void setReservasCanceladas(long reservasCanceladas) {
        this.reservasCanceladas = reservasCanceladas;
    }

    public long getReservasFinalizadas() {
        return reservasFinalizadas;
    }

    public void setReservasFinalizadas(long reservasFinalizadas) {
        this.reservasFinalizadas = reservasFinalizadas;
    }

    public long getReservasDelDia() {
        return reservasDelDia;
    }

    public void setReservasDelDia(long reservasDelDia) {
        this.reservasDelDia = reservasDelDia;
    }
}