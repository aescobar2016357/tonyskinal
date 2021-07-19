package org.axelescobar.controller;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.axelescobar.system.MainApp;

public class DatosPersonalesController implements Initializable {
    private MainApp escPrincipal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public MainApp getEscPrincipal() {
        return escPrincipal;
    }

    public void setEscPrincipal(MainApp escPrincipal) {
        this.escPrincipal = escPrincipal;
    }
 
    public void mainApp() {
        escPrincipal.mainMenu();
    }
}