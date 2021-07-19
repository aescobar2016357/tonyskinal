package org.axelescobar.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.axelescobar.bd.Conexion;
import org.axelescobar.beans.Empleados;
import org.axelescobar.system.MainApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.axelescobar.beans.TipoEmpleado;

public class EmpleadosController implements Initializable {
    private MainApp escPrincipal;
    private ObservableList<Empleados> listaEmpleado;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    @FXML private TextField txtCodigoEmpleado;
    @FXML private TextField txtNumeroEmpleado;
    @FXML private TextField txtNombresEmpleado;
    @FXML private TextField txtApellidosEmpleado;
    @FXML private TextField txtDireccionEmpleado;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtGradoCocinero;
    @FXML private ComboBox  cmbTipoEmpleado;
    @FXML private TableView tblEmpleados;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colNumeroEmpleado;
    @FXML private TableColumn colNombresEmpleado;
    @FXML private TableColumn colApellidosEmpleado;
    @FXML private TableColumn colDireccionEmpleado;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colGradoCocinero;
    @FXML private TableColumn colTipoEmpleado;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnUpdate;
    @FXML private Button btnReporte;
    @FXML private Button btnReload;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditText;
    @FXML private Button btnCancelar;


    public ObservableList<Empleados> getEmpleados(){
        ArrayList<Empleados> lista = new ArrayList<Empleados>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados()}");
            ResultSet result = procedimiento.executeQuery();
            while (result.next()) {
                lista.add(new Empleados(result.getInt("codigoEmpleado"),
                result.getInt("numeroEmpleado"),
                result.getString("apellidosEmpleado"), 
                result.getString("nombresEmpleado"),
                result.getString("direccionEmpleado"),
                result.getString("telefonoContacto"), 
                result.getString("gradoCocinero"), 
                result.getInt("codigoTipoEmpleado")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableList(lista);
    }
    
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
        tblEmpleados.setItems(getEmpleados());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoEmpleado"));
        colNumeroEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("numeroEmpleado"));
        colApellidosEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, String>("apellidosEmpleado"));
        colNombresEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, String>("nombresEmpleado"));
        colDireccionEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, String>("direccionEmpleado"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empleados, String>("telefonoContacto"));
        colGradoCocinero.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("gradoCocinero"));
        colTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoTipoEmpleado"));
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
    
    public void ventanaTipoEmpleado() {
        escPrincipal.ventanaTipoEmpleado();
    }

    public void addEmpleados() {
        try {
            agregarDatos();
            cargarDatos();
            cleanTextField();
            disableTextField();
            btnEditText.setDisable(false);
            btnCancelar.setVisible(false);
            tblEmpleados.setDisable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void agregarDatos() {
        Empleados newEmpleados = new Empleados();
        newEmpleados.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
        newEmpleados.setApellidosEmpleado(txtApellidosEmpleado.getText());
        newEmpleados.setNombresEmpleado(txtNombresEmpleado.getText());
        newEmpleados.setDireccionEmpleado(txtDireccionEmpleado.getText());
        newEmpleados.setTelefonoContacto(txtTelefono.getText());
        newEmpleados.setGradoCocinero(txtGradoCocinero.getText());
        newEmpleados.setCodigoTipoEmpleado(((TipoEmpleado)cmbTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
        try {
            PreparedStatement agregar = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleados(?,?,?,?,?,?,?)}");
            agregar.setInt(1, newEmpleados.getNumeroEmpleado());
            agregar.setString(2, newEmpleados.getApellidosEmpleado());
            agregar.setString(3,newEmpleados.getNombresEmpleado());
            agregar.setString(4,newEmpleados.getDireccionEmpleado());
            agregar.setString(5,newEmpleados.getTelefonoContacto());
            agregar.setString(6,newEmpleados.getGradoCocinero());
            agregar.setInt(7, newEmpleados.getCodigoTipoEmpleado());
            agregar.execute();
            listaEmpleado.add(newEmpleados);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cleanTextField() {
        txtCodigoEmpleado.setText("");
        txtNumeroEmpleado.setText("");
        txtApellidosEmpleado.setText("");
        txtNombresEmpleado.setText("");
        txtDireccionEmpleado.setText("");
        txtTelefono.setText("");
        txtGradoCocinero.setText("");
        cmbTipoEmpleado.getSelectionModel().clearSelection();
    }

    public void disableTextField() {
        txtNumeroEmpleado.setDisable(true);
        txtApellidosEmpleado.setDisable(true);
        txtNombresEmpleado.setDisable(true);
        txtDireccionEmpleado.setDisable(true);
        txtTelefono.setDisable(true);
        txtGradoCocinero.setDisable(true);
        cmbTipoEmpleado.setDisable(true);
    }

    public void ableTextField() {
        txtNumeroEmpleado.setDisable(false);
        txtApellidosEmpleado.setDisable(false);
        txtNombresEmpleado.setDisable(false);
        txtDireccionEmpleado.setDisable(false);
        txtTelefono.setDisable(false);
        txtGradoCocinero.setDisable(false);
        cmbTipoEmpleado.setDisable(false);
    }
    
    public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado){
    TipoEmpleado result = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoEmpleado(?)}");
                procedimiento.setInt(1, codigoTipoEmpleado);
                ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                result = new TipoEmpleado(registro.getInt("codigoTipoEmpleado"),
                registro.getString("descripcion"));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        return result;
    }

    public void selectDatafromTable() {
        txtCodigoEmpleado.setText(String.valueOf(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        txtNumeroEmpleado.setText(String.valueOf(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
        txtApellidosEmpleado.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado());
        txtNombresEmpleado.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado());
        txtDireccionEmpleado.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
        txtTelefono.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto());
        txtGradoCocinero.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero());
        cmbTipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));

    }

    public void btnNuevo(){
        ableTextField();
        cleanTextField();
        btnEditText.setDisable(true);
        tblEmpleados.setDisable(true);
        btnCancelar.setVisible(true);
    }
    
    public void btnEditText() {
        ableTextField();
        btnNuevo.setDisable(true);
        btnCancelar.setVisible(true);
        cmbTipoEmpleado.setDisable(true);
    }
    
    public void cancelar() {
        disableTextField();
        cleanTextField();
        btnEditText.setDisable(false);
        btnNuevo.setDisable(false);
        tblEmpleados.setDisable(false);
        btnCancelar.setVisible(false);
    }

    public void deleteEmpleado(){
        if(tblEmpleados.getSelectionModel().getSelectedItem()!= null){
            int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro?", "Eliminar Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_OPTION){
                try {
                    PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpleados(?)}");
                    sp.setInt(1, ((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                    sp.execute();
                    cargarDatos();
                    cleanTextField();
                    disableTextField();
                    btnEditText.setDisable(false);
                    JOptionPane.showMessageDialog(null, "Datos eliminados con exito");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else{
            JOptionPane.showMessageDialog(null,"Debe seleccionar un registro de la tabla");
        }
    }

    public void updateEmpleado(){
        if (tblEmpleados.getSelectionModel().getSelectedItem()!=null) {
            try {
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarEmpleados(?,?,?,?,?,?,?,?)}");
                Empleados empleadoUpdate = ((Empleados)tblEmpleados.getSelectionModel().getSelectedItem());
                empleadoUpdate.setCodigoEmpleado(Integer.parseInt(txtCodigoEmpleado.getText()));
                empleadoUpdate.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
                empleadoUpdate.setApellidosEmpleado(txtApellidosEmpleado.getText());
                empleadoUpdate.setNombresEmpleado(txtNombresEmpleado.getText());
                empleadoUpdate.setDireccionEmpleado(txtDireccionEmpleado.getText());
                empleadoUpdate.setTelefonoContacto(txtTelefono.getText());
                empleadoUpdate.setGradoCocinero(txtGradoCocinero.getText());
                sp.setInt(1, empleadoUpdate.getCodigoEmpleado());
                sp.setInt(2, empleadoUpdate.getNumeroEmpleado());
                sp.setString(3, empleadoUpdate.getApellidosEmpleado());
                sp.setString(4,empleadoUpdate.getNombresEmpleado());
                sp.setString(5,empleadoUpdate.getDireccionEmpleado());
                sp.setString(6,empleadoUpdate.getTelefonoContacto());
                sp.setString(7,empleadoUpdate.getGradoCocinero());
                sp.setInt(8, empleadoUpdate.getCodigoTipoEmpleado());
                sp.execute();
                JOptionPane.showMessageDialog(null, "Datos Actualizados");
                disableTextField();
                cleanTextField();
                btnEditText.setDisable(false);
                cargarDatos();
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
        cmbTipoEmpleado.setItems(getTipoEmpleado());

    }

}