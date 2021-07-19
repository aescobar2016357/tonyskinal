package org.axelescobar.system;

import java.io.InputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.axelescobar.controller.*;

public class MainApp extends Application {
    private final String PACKAGE_VIEW = "/org/axelescobar/view/";
    private Stage escPrincipal;
    private Scene esc;

    @Override
    public void start(Stage escPrincipal) throws Exception {
        this.escPrincipal = escPrincipal;
        this.escPrincipal.setTitle("Tony's Kinal App");
        escPrincipal.getIcons().add(new Image("/org/axelescobar/img/Icono.png"));
        mainMenu();
        escPrincipal.show();
    }

    public void mainMenu() {
        try {
            MainAppController mainMenu = (MainAppController) changeScene("MenuPrincipalView.fxml", 400, 400);
            mainMenu.setEscPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaProgramador() {
        try {
            DatosPersonalesController datosPersonales = (DatosPersonalesController) changeScene(
                    "DatosPersonalesView.fxml", 500, 400);
            datosPersonales.setEscPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaEmpresas() {
        try {
            EmpresasController datosEmpresa = (EmpresasController) changeScene("EmpresasView.fxml", 600, 540);
            datosEmpresa.setEscPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaEmpleados() {
        try {
            EmpleadosController datosEmpleado = (EmpleadosController) changeScene("EmpleadosView.fxml", 726, 540);
            datosEmpleado.setEscPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaServicios() {
        try {
            ServiciosController datosServicios = (ServiciosController) changeScene("ServiciosView.fxml", 729, 540);
            datosServicios.setEscPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaPlatos() {
        try {
            PlatosController datosPlatos = (PlatosController) changeScene("PlatosView.fxml", 720, 540);
            datosPlatos.setEscPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaPresupuesto() {
        try {
            PresupuestoController datosPresupuesto = (PresupuestoController) changeScene("PresupuestoView.fxml", 600,540);
            datosPresupuesto.setEscPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaProductos() {
        try {
            ProductosController datosProducto = (ProductosController) changeScene("ProductosView.fxml", 600, 504);
            datosProducto.setEscPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaTipoEmpleado() {
        try {
            TipoEmpleadoController datosTipoEmpleado = (TipoEmpleadoController) changeScene("TipoEmpleadoView.fxml",600, 470);
            datosTipoEmpleado.setEscPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaTipoPlato() {
        try {
            TipoPlatoController datosTipoPlato = (TipoPlatoController) changeScene("TipoPlatoView.fxml", 600, 506);
            datosTipoPlato.setEscPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaSE() {
        try {
            ServiciosHasEmpleadosController datosSE = (ServiciosHasEmpleadosController) changeScene("ServiciosHasEmpleadosView.fxml", 730, 540);
            datosSE.setEscPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaSP() {
        try {
            ServiciosHasPlatosController datosSP = (ServiciosHasPlatosController) changeScene("ServiciosHasPlatosView.fxml", 600, 430);
            datosSP.setEscPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaPP() {
        try {
            ProductosHasPlatosController datosPP = (ProductosHasPlatosController) changeScene("ProductosHasPlatosView.fxml", 600, 430);
            datosPP.setEscPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Initializable changeScene(String fxml, int ancho, int alto) throws Exception {
        Initializable result = null;
        FXMLLoader loadFXML = new FXMLLoader();
        InputStream archivo = MainApp.class.getResourceAsStream(PACKAGE_VIEW + fxml);
        loadFXML.setBuilderFactory(new JavaFXBuilderFactory());
        loadFXML.setLocation(MainApp.class.getResource(PACKAGE_VIEW + fxml));
        esc = new Scene((AnchorPane) loadFXML.load(archivo), ancho, alto);
        escPrincipal.setScene(esc);
        escPrincipal.sizeToScene();
        result = (Initializable) loadFXML.getController();
        return result;
    }
}
