package org.axelescobar.beans;

import java.util.Date;

public class Presupuesto {
    private int codigoPresupuesto;
    private Date fechaSolicitud;
    private float cantidadPresupuesto;
    private int codigoEmpresa;

    public Presupuesto() {

    }

    public Presupuesto(int codigoPresupuesto, Date fechaSolicitud, float cantidadPresupuesto, int codigoEmpresa){
        this.codigoPresupuesto = codigoPresupuesto;
        this.fechaSolicitud = fechaSolicitud;
        this.cantidadPresupuesto = cantidadPresupuesto;
        this.codigoEmpresa = codigoEmpresa;
    }

    public int getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(int codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public float getCantidadPresupuesto() {
        return cantidadPresupuesto;
    }

    public void setCantidadPresupuesto(float cantidadPresupuesto) {
        this.cantidadPresupuesto = cantidadPresupuesto;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public int getCodigoPresupuesto() {
        return codigoPresupuesto;
    }

    public void setCodigoPresupuesto(int codigoPresupuesto) {
        this.codigoPresupuesto = codigoPresupuesto;
    }

}