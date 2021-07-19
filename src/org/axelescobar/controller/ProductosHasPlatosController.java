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
import org.axelescobar.beans.Productos;
import org.axelescobar.beans.ProductosHasPlatos;
import org.axelescobar.system.MainApp;

public class ProductosHasPlatosController implements Initializable{
    private MainApp escPrincipal;
    private ObservableList<ProductosHasPlatos> listaPP;
    private ObservableList<Platos> listaPlatos;
    private ObservableList<Productos> listaProductos;
    @FXML private ComboBox cmbCodigoProducto;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private TableView tblProductoPlato;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colCodigoProducto;
    @FXML private Button btnReload;
    
    public ObservableList<ProductosHasPlatos> getProductosHasPlatos(){
        ArrayList<ProductosHasPlatos> lista = new ArrayList<ProductosHasPlatos>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos_Has_Platos()}");
            ResultSet result = procedimiento.executeQuery();
            while (result.next()) {
                lista.add(new ProductosHasPlatos(result.getInt("codigoProducto"),
                result.getInt("codigoPlato")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPP = FXCollections.observableList(lista);
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
    
    public ObservableList<Productos> getProducto(){
        ArrayList<Productos> lista = new ArrayList<Productos>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos()}");
            ResultSet result = procedimiento.executeQuery();
            while (result.next()) {
                lista.add(new Productos(result.getInt("codigoProducto"),
                result.getString("nombreProducto"), 
                result.getInt("cantidad")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProductos = FXCollections.observableList(lista);
    }
    
    public Productos buscarProducto(int codigoProducto){
    Productos result = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProductos(?)}");
                procedimiento.setInt(1, codigoProducto);
                ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                result = new Productos(registro.getInt("codigoProducto"),
                registro.getString("nombreProducto"),
                registro.getInt("cantidad"));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        return result;
    }
    
    public void cargarDatos() {
        tblProductoPlato.setItems(getProductosHasPlatos());
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ProductosHasPlatos, Integer>("codigoPlato"));
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<ProductosHasPlatos, String>("codigoProducto"));
    }

    public void selectDatafromTable() {
        cmbCodigoProducto.getSelectionModel().select(buscarProducto(((ProductosHasPlatos) tblProductoPlato.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        cmbCodigoPlato.getSelectionModel().select(buscarPlatos(((ProductosHasPlatos) tblProductoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        

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
    }
}
