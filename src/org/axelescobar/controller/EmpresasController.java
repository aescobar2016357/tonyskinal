package org.axelescobar.controller;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

import org.axelescobar.beans.Empresa;
import org.axelescobar.bd.Conexion;
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
import org.axelescobar.report.GenerarReporte;

public class EmpresasController implements Initializable {
    private MainApp escPrincipal;
    private ObservableList<Empresa> listaEmpresa;

    @FXML private TextField txtCodigoEmpresa;
    @FXML private TextField txtNombreEmpresa;
    @FXML private TextField txtDireccionEmpresa;
    @FXML private TextField txtTelefono; 
    @FXML private TableView tblEmpresas;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private TableColumn colNombreEmpresa;
    @FXML private TableColumn colDireccionEmpresa;
    @FXML private TableColumn colTelefono;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnUpdate;
    @FXML private Button btnReporte;
    @FXML private Button btnReload;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditText;
    @FXML private Button btnCancelar;

    public ObservableList<Empresa> getEmpresa(){
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpresas()}");
            ResultSet result = procedimiento.executeQuery();
            while (result.next()) {
                lista.add(new Empresa(result.getInt("codigoEmpresa"),
                result.getString("nombreEmpresa"), 
                result.getString("direccion"),
                result.getString("telefono")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpresa = FXCollections.observableList(lista);
    }
    
    public void cargarDatos() {
        tblEmpresas.setItems(getEmpresa());
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, Integer>("codigoEmpresa"));
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("nombreEmpresa"));
        colDireccionEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empresa, String>("telefono"));
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

    public void vistaServicios() {
        escPrincipal.ventanaServicios();
    }

    public void vistaPresupuesto() {
        escPrincipal.ventanaPresupuesto();
    }

    public void addEmpresa() {
        try {
            agregarDatos();
            cargarDatos();
            cleanTextField();
            disableTextField();
            btnEditText.setDisable(false);
            tblEmpresas.setDisable(false);
            btnCancelar.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
       
    }
    public void agregarDatos() {
        Empresa newEmpresa = new Empresa();
        newEmpresa.setNombreEmpresa(txtNombreEmpresa.getText());
        newEmpresa.setDireccion(txtDireccionEmpresa.getText());
        newEmpresa.setTelefono(txtTelefono.getText());
        try {
            PreparedStatement agregar = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpresas(?,?,?)}");
            agregar.setString(1,newEmpresa.getNombreEmpresa());
            agregar.setString(2,newEmpresa.getDireccion());
            agregar.setString(3,newEmpresa.getTelefono());
            agregar.execute();
            listaEmpresa.add(newEmpresa);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cleanTextField() {
        txtCodigoEmpresa.setText("");
        txtNombreEmpresa.setText("");
        txtDireccionEmpresa.setText("");
        txtTelefono.setText("");
    }

    public void disableTextField() {
        txtNombreEmpresa.setDisable(true);
        txtDireccionEmpresa.setDisable(true);
        txtTelefono.setDisable(true);
    }

    public void ableTextField() {
        txtNombreEmpresa.setDisable(false);
        txtDireccionEmpresa.setDisable(false);
        txtTelefono.setDisable(false);
    }

    public void selectDatafromTable() {
        txtCodigoEmpresa.setText(String.valueOf(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
        txtNombreEmpresa.setText(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa());
        txtDireccionEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion());
        txtTelefono.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono());
    }

    public void btnNuevo(){
        ableTextField();
        cleanTextField();
        btnEditText.setDisable(true);
        tblEmpresas.setDisable(true);
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
        tblEmpresas.setDisable(false);
        btnCancelar.setVisible(false);
    }

    public void deleteEmpresa(){
        if(tblEmpresas.getSelectionModel().getSelectedItem()!= null){
            int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro?", "Eliminar Empresa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_OPTION){
                try {
                    PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpresas(?)}");
                    sp.setInt(1, ((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
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
        if (tblEmpresas.getSelectionModel().getSelectedItem()!=null) {
            try {
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarEmpresas(?,?,?,?)}");
                Empresa empresaUpdate = ((Empresa)tblEmpresas.getSelectionModel().getSelectedItem());
                empresaUpdate.setNombreEmpresa(txtNombreEmpresa.getText());
                empresaUpdate.setDireccion(txtDireccionEmpresa.getText());
                empresaUpdate.setTelefono(txtTelefono.getText());
                sp.setInt(1, empresaUpdate.getCodigoEmpresa());
                sp.setString(2, empresaUpdate.getNombreEmpresa());
                sp.setString(3, empresaUpdate.getDireccion());
                sp.setString(4, empresaUpdate.getTelefono());
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
    
    public void generarReporte(){
        imprimirReporte();
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoEmpresa",null);
        GenerarReporte.mostrarReporte("ReporteEmpresas.jasper", "Reporte de Empresas", null);
     }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

}