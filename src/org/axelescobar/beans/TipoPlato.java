package org.axelescobar.beans;

public class TipoPlato {
    private int codigoTipoPlato;
    private String descripcionTipo;

    public TipoPlato() {
    }

    public TipoPlato(int codigoTipoPlato, String descripcionTipo) {
        this.codigoTipoPlato = codigoTipoPlato;
        this.descripcionTipo = descripcionTipo;
    }

    public int getCodigoTipoPlato() {
        return this.codigoTipoPlato;
    }

    public void setCodigoTipoPlato(int codigoTipoPlato) {
        this.codigoTipoPlato = codigoTipoPlato;
    }

    public String getDescripcionTipo() {
        return this.descripcionTipo;
    }

    public void setDescripcion(String descripcionTipo) {
        this.descripcionTipo = descripcionTipo;
    }
    
    @Override
    public String toString() {
        return getCodigoTipoPlato() + "|" + getDescripcionTipo();
    }

}