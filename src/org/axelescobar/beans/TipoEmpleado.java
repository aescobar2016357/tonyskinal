package org.axelescobar.beans;

public class TipoEmpleado {
    private int codigoTipoEmpleado;
    private String descripcion;


    public TipoEmpleado() {
    }

    public TipoEmpleado(int codigoTipoEmpleado, String descripcion) {
        this.codigoTipoEmpleado = codigoTipoEmpleado;
        this.descripcion = descripcion;
    }

    public int getCodigoTipoEmpleado() {
        return this.codigoTipoEmpleado;
    }

    public void setCodigoTipoEmpleado(int codigoTipoEmpleado) {
        this.codigoTipoEmpleado = codigoTipoEmpleado;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    @Override
    public String toString() {
        return getCodigoTipoEmpleado() + "|" + getDescripcion();
    }
    
}