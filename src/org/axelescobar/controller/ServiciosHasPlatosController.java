package org.axelescobar.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.axelescobar.bd.Conexion;
import org.axelescobar.beans.Platos;
import org.axelescobar.beans.Servicios;
import org.axelescobar.beans.ServiciosHasPlatos;
import org.axelescobar.system.MainApp;

public class ServiciosHasPlatosController implements Initializable{
    private MainApp escPrincipal;
    private ObservableList<ServiciosHasPlatos> listaSP;
    private ObservableList<Platos> listaPlatos;
    private ObservableList<Servicios> listaServicios;
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private TableView tblServicioPlato;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoPlato;
    @FXML private Button btnReload;
    
    public ObservableList<ServiciosHasPlatos> getServiciosHasPlatos(){
        ArrayList<ServiciosHasPlatos> lista = new ArrayList<ServiciosHasPlatos>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_Has_Platos()}");
            ResultSet result = procedimiento.executeQuery();
            while (result.next()) {
                lista.add(new ServiciosHasPlatos(result.getInt("codigoSP"),
                result.getInt("codigoServicio"),
                result.getInt("codigoPlato")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaSP = FXCollections.observableList(lista);
    }
    
    public ObservableList<Platos> getPlato(){
        ArrayList<Platos> lista = new ArrayList<Platos>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPlatos()}");
            ResultSet result = procedimiento.executeQuery();
            while (result.next()) {
                lista.add(new Platos(result.getInt("codigoPlato"),
                result.getInt("cantidad"), 
                result.getString("nombrePlato"),
                result.getString("descripcionPlato"),
                result.getFloat("precioPlato"),
                result.getInt("codigoTipoPlato")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPlatos = FXCollections.observableList(lista);
    }
    
    public Platos buscarPlatos(int codigoPlato){
    Platos result = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPlatos(?)}");
                procedimiento.setInt(1, codigoPlato);
                ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                result = new Platos(registro.getInt("codigoPlato"),
                registro.getInt("cantidad"),
                registro.getString("nombrePlato"),
                registro.getString("descripcionPlato"),
                registro.getFloat("precioPlato"),
                registro.getInt("codigoTipoPlato"));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        return result;
    }
    
    public ObservableList<Servicios> getServicios(){
        ArrayList<Servicios> lista = new ArrayList<Servicios>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios()}");
            ResultSet result = procedimiento.executeQuery();
            while (result.next()) {
                lista.add(new Servicios(result.getInt("codigoServicio"),
                result.getDate("fechaServicio"), 
                result.getString("tipoServicio"),
                result.getString(String.valueOf("horaServicio")),
                result.getString("lugarServicio"),
                result.getString("telefonoContacto"), 
                result.getInt("codigoEmpresa")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicios = FXCollections.observableList(lista);
    }
    
    public Servicios buscarServicios(int codigoServicio){
    Servicios result = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicios(?)}");
                procedimiento.setInt(1, codigoServicio);
                ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                result = new Servicios(registro.getInt("codigoServicio"),
                registro.getDate("fechaServicio"),
                registro.getString("tipoServicio"),
                registro.getString("horaServicio"),
                registro.getString("lugarServicio"),
                registro.getString("telefonoContacto"),
                registro.getInt("codigoEmpresa"));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        return result;
    }
    
    public void cargarDatos() {
        tblServicioPlato.setItems(getServiciosHasPlatos());
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ServiciosHasPlatos, Integer>("codigoPlato"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServiciosHasPlatos, String>("codigoServicio"));
    }

    public void selectDatafromTable() {
        cmbCodigoPlato.getSelectionModel().select(buscarPlatos(((ServiciosHasPlatos) tblServicioPlato.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        cmbCodigoServicio.getSelectionModel().select(buscarServicios(((ServiciosHasPlatos) tblServicioPlato.getSelectionModel().getSelectedItem()).getCodigoServicio()));

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
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoPlato.setItems(getPlato());
        cmbCodigoServicio.setItems(getServicios());
        
    }
}
