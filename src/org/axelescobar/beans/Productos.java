package org.axelescobar.beans;

public class Productos {
    private int codigoProducto;
    private String nombreProducto;
    private int cantidad;

    public Productos() {

    }
    public Productos(int codigoProducto, String nombreProducto, int cantidad){
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    @Override
    public String toString() {
        return getCodigoProducto() + "|" + getNombreProducto();
    }
    
}