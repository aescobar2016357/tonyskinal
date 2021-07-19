package org.axelescobar.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.axelescobar.bd.Conexion;
import org.axelescobar.beans.TipoEmpleado;
import org.axelescobar.system.MainApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class TipoEmpleadoController implements Initializable {
    private MainApp escPrincipal;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    @FXML private TextField txtCodigoTipoEmpleado;
    @FXML private TextField txtDescripcion;
    @FXML private TableView tblTipoEmpleado;
    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private TableColumn colDescripcion;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnUpdate;
    @FXML private Button btnReporte;
    @FXML private Button btnReload;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditText;
    @FXML private Button btnCancelar;
    
    public ObservableList<TipoEmpleado> getTipoEmpleado(){
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoEmpleado()}");
            ResultSet result = procedimiento.executeQuery();
            while (result.next()) {
                lista.add(new TipoEmpleado(result.getInt("codigoTipoEmpleado"),
                result.getString("descripcion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoEmpleado = FXCollections.observableList(lista);
    }

    public void cargarDatos() {
        tblTipoEmpleado.setItems(getTipoEmpleado());
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, Integer>("codigoTipoEmpleado"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, String>("descripcion"));
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
    
    public void empleados() {
        escPrincipal.ventanaEmpleados();
    }

    public void addTipoEmpleado() {
        try {
            agregarDatos();
            cargarDatos();
            cleanTextField();
            disableTextField();
            btnEditText.setDisable(false);
            tblTipoEmpleado.setDisable(false);
            btnCancelar.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
       
    }

    public void agregarDatos() {
        TipoEmpleado newTipoEmpleado = new TipoEmpleado();
        newTipoEmpleado.setDescripcion(txtDescripcion.getText());
        try {
            PreparedStatement agregar = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoEmpleado(?)}");
            agregar.setString(1, newTipoEmpleado.getDescripcion());
            agregar.execute();
            listaTipoEmpleado.add(newTipoEmpleado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cleanTextField() {
        txtCodigoTipoEmpleado.setText("");
        txtDescripcion.setText("");
    }

    public void disableTextField() {
        txtDescripcion.setDisable(true);
    }

    public void ableTextField() {
        txtDescripcion.setDisable(false);
    }

    public void selectDatafromTable() {
        txtCodigoTipoEmpleado.setText(String.valueOf(((TipoEmpleado) tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
        txtDescripcion.setText((((TipoEmpleado) tblTipoEmpleado.getSelectionModel().getSelectedItem()).getDescripcion()));
    }

    public void btnNuevo() {
        ableTextField();
        cleanTextField();
        btnEditText.setDisable(true);
        tblTipoEmpleado.setDisable(true);
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
        tblTipoEmpleado.setDisable(false);
        btnCancelar.setVisible(false);
    }

    public void deleteTipoEmpleado(){
        if(tblTipoEmpleado.getSelectionModel().getSelectedItem()!= null){
            int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro?", "Eliminar Tipo Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_OPTION){
                try {
                    PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoEmpleado(?)}");
                    sp.setInt(1, ((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
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

    public void updateTipoEmpleado(){
        if (tblTipoEmpleado.getSelectionModel().getSelectedItem()!=null) {
            try {
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarTipoEmpleado(?,?)}");
                TipoEmpleado tipoUpdate = ((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem());
                tipoUpdate.setDescripcion(txtDescripcion.getText());
                sp.setInt(1, tipoUpdate.getCodigoTipoEmpleado());
                sp.setString(2, tipoUpdate.getDescripcion());
                sp.execute();
                JOptionPane.showMessageDialog(null, "Datos Actualizados");
                disableTextField();
                cleanTextField();
                btnEditText.setDisable(false);
                btnCancelar.setVisible(false);
                btnNuevo.setDisable(false);
                cargarDatos();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            JOptionPane.showMessageDialog(null, "Debe selecionar un registro para editar");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

}