package org.axelescobar.beans;

public class Platos {
    private int codigoPlato;
    private int cantidad;
    private String nombrePlato;
    private String descripcionPlato;
    private float precioPlato;
    private int codigoTipoPlato;

    public Platos() {
        
    }

    public Platos(int codigoPlato,int cantidad, String nombrePlato, String descripcionPlato,float precioPlato,int codigoTipoPlato){
        this.codigoPlato = codigoPlato;
        this.cantidad = cantidad;
        this.nombrePlato = nombrePlato;
        this.descripcionPlato = descripcionPlato;
        this.precioPlato = precioPlato;
        this.codigoTipoPlato = codigoTipoPlato;
    }

    public int getCodigoPlato() {
        return this.codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }

    public int getCantidad() {
        return this.cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombrePlato() {
        return this.nombrePlato;
    }

    public void setNombrePlato(String nombrePlato) {
        this.nombrePlato = nombrePlato;
    }

    public String getDescripcionPlato() {
        return this.descripcionPlato;
    }

    public void setDescripcionPlato(String descripcionPlato) {
        this.descripcionPlato = descripcionPlato;
    }

    public float getPrecioPlato() {
        return this.precioPlato;
    }

    public void setPrecioPlato(float precioPlato) {
        this.precioPlato = precioPlato;
    }

    public int getCodigoTipoPlato() {
        return this.codigoTipoPlato;
    }

    public void setCodigoTipoPlato(int codigoTipoPlato) {
        this.codigoTipoPlato = codigoTipoPlato;
    }
    
    @Override
    public String toString() {
        return getCodigoPlato() + "|" + getDescripcionPlato();
    }

}