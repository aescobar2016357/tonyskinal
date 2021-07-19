package org.axelescobar.controller;
import java.net.URL;
import java.util.ResourceBundle;
import org.axelescobar.system.MainApp;
import javafx.fxml.Initializable;

public class MainAppController implements Initializable {
    private MainApp escPrincipal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public MainApp getEscPrincipal(){
        return escPrincipal;
    }

    public void setEscPrincipal(MainApp escPrincipal){
        this.escPrincipal = escPrincipal;
    }
    
    public void ventanaProgramador() {
        escPrincipal.ventanaProgramador();
    }

    public void ventanaEmpresas() {
        escPrincipal.ventanaEmpresas();
    }

    public void ventanaEmpleados() {
        escPrincipal.ventanaEmpleados();
    }

    public void ventanaServicios() {
        escPrincipal.ventanaServicios();
    }

    public void ventanaPlatos() {
        escPrincipal.ventanaPlatos();
    }

    public void ventanaPresupuesto() {
        escPrincipal.ventanaPresupuesto();
    }
    
    public void ventanaProductos() {
        escPrincipal.ventanaProductos();
    }

    public void ventanaTipoEmpleado() {
        escPrincipal.ventanaTipoEmpleado();
    }

    public void ventanaTipoPlato() {
        escPrincipal.ventanaTipoPlato();
    }
    
    public void ventanaSE() {
        escPrincipal.ventanaSE();
    }
    
    public void ventanaSP() {
        escPrincipal.ventanaSP();
    }
    
    public void ventanaPP() {
        escPrincipal.ventanaPP();
    }
    
}