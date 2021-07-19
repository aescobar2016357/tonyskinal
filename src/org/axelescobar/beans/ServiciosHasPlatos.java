package org.axelescobar.beans;

public class ServiciosHasPlatos {
    private int codigoSP;
    private int codigoServicio;
    private int codigoPlato;

    public ServiciosHasPlatos(){
    }
    
    public ServiciosHasPlatos(int codigoSP, int codigoServicio, int codigoPlato) {
        this.codigoSP = codigoSP;
        this.codigoServicio = codigoServicio;
        this.codigoPlato = codigoPlato;
    }

    public int getCodigoSP() {
        return codigoSP;
    }

    public void setCodigoSP(int codigoSP) {
        this.codigoSP = codigoSP;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }
    
    
}
