package org.axelescobar.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import org.axelescobar.system.MainApp;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.axelescobar.bd.Conexion;
import org.axelescobar.beans.Productos;

public class ProductosController implements Initializable {
    private MainApp escPrincipal;
    private ObservableList<Productos> listaProductos;

    @FXML private TextField txtCodigoProducto;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtCantidad;
    @FXML private TableView tblProductos;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colNombreProducto;
    @FXML private TableColumn colCantidad;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnUpdate;
    @FXML private Button btnReporte;
    @FXML private Button btnReload;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditText;
    @FXML private Button btnCancelar;

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
    
     public void cargarDatos() {
        tblProductos.setItems(getProducto());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("codigoProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<Productos, String>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("Cantidad"));
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
    
    public void addEmpresa() {
        try {
            agregarDatos();
            cargarDatos();
            cleanTextField();
            disableTextField();
            btnEditText.setDisable(false);
            tblProductos.setDisable(false);
            btnCancelar.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
       
    }
    
    public void agregarDatos() {
        Productos newProducto = new Productos();
        newProducto.setNombreProducto(txtNombreProducto.getText());
        newProducto.setCantidad(Integer.parseInt(txtCantidad.getText()));
        try {
            PreparedStatement agregar = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProductos(?,?)}");
            agregar.setString(1,newProducto.getNombreProducto());
            agregar.setInt(2,newProducto.getCantidad());
            agregar.execute();
            listaProductos.add(newProducto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void cleanTextField() {
        txtCodigoProducto.setText("");
        txtNombreProducto.setText("");
        txtCantidad.setText("");
    }

    public void disableTextField() {
        txtNombreProducto.setDisable(true);
        txtCantidad.setDisable(true);
    }

    public void ableTextField() {
        txtNombreProducto.setDisable(false);
        txtCantidad.setDisable(false);
    }
    
    public void selectDatafromTable() {
        txtCodigoProducto.setText(String.valueOf(((Productos) tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        txtNombreProducto.setText(((Productos) tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto());
        txtCantidad.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCantidad()));
    }
    
     public void btnNuevo(){
        ableTextField();
        cleanTextField();
        btnEditText.setDisable(true);
        tblProductos.setDisable(true);
        btnCancelar.setVisible(true);
    }

    public void btnEditText() {
        ableTextField();
        btnNuevo.setDisable(true);
        btnCancelar.setVisible(true);
    }

    public void cancelar() {
        disableTextField();
        cleanTextField();
        btnEditText.setDisable(false);
        btnNuevo.setDisable(false);
        tblProductos.setDisable(false);
        btnCancelar.setVisible(false);
    }
    
    public void deleteEmpresa(){
        if(tblProductos.getSelectionModel().getSelectedItem()!= null){
            int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro?", "Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_OPTION){
                try {
                    PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProducto(?)}");
                    sp.setInt(1, ((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                    sp.execute();
                    cargarDatos();
                    cleanTextField();
                    disableTextField();
                    btnEditText.setDisable(false);
                    btnCancelar.setVisible(false);
                    btnNuevo.setDisable(false);
                    JOptionPane.showMessageDialog(null, "Datos eliminados con exito");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else{
            JOptionPane.showMessageDialog(null,"Debe seleccionar un registro de la tabla");
        }
    }

    public void updateEmpresa(){
        if (tblProductos.getSelectionModel().getSelectedItem()!=null) {
            try {
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarProductos(?,?,?)}");
                Productos productoUpdate = ((Productos)tblProductos.getSelectionModel().getSelectedItem());
                productoUpdate.setCodigoProducto(Integer.parseInt(txtCodigoProducto.getText()));
                productoUpdate.setNombreProducto(txtNombreProducto.getText());
                productoUpdate.setCantidad(Integer.parseInt(txtCantidad.getText()));
                sp.setInt(1, productoUpdate.getCodigoProducto());
                sp.setString(2, productoUpdate.getNombreProducto());
                sp.setInt(3, productoUpdate.getCantidad());
                sp.execute();
                JOptionPane.showMessageDialog(null, "Datos Actualizados");
                disableTextField();
                cleanTextField();
                btnEditText.setDisable(false);
                btnCancelar.setVisible(false);
                btnNuevo.setDisable(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            JOptionPane.showMessageDialog(null, "Debe selecionar un registro para editar");
        }
    }
    
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        cargarDatos();
    }
}