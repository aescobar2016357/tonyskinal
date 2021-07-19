package org.axelescobar.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.axelescobar.bd.Conexion;
import org.axelescobar.beans.Empleados;
import org.axelescobar.beans.Servicios;
import org.axelescobar.beans.ServiciosHasEmpleados;
import org.axelescobar.system.MainApp;

public class ServiciosHasEmpleadosController implements Initializable {
    private MainApp escPrincipal;
    private ObservableList<ServiciosHasEmpleados> listaSE;
    private ObservableList<Servicios> listaServicios;
    private ObservableList<Empleados> listaEmpleados;
    private DatePicker fecha;
    @FXML private TextField txtCodigoSE;
    @FXML private GridPane grdFechaEvento;
    @FXML private TextField txtHoraEvento;
    @FXML private TextField txtLugarEvento;
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoEmpleado;
    @FXML private TableView tblServiciosEmpleados;
    @FXML private TableColumn colCodigoSE;
    @FXML private TableColumn colFechaEvento;
    @FXML private TableColumn colHoraEvento;
    @FXML private TableColumn colLugarEvento;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnUpdate;
    @FXML private Button btnReporte;
    @FXML private Button btnReload;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditText;
    @FXML private Button btnCancelar;
    
    public ObservableList<ServiciosHasEmpleados> getServiciosHasPlatos(){
        ArrayList<ServiciosHasEmpleados> lista = new ArrayList<ServiciosHasEmpleados>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_has_Empleados()}");
            ResultSet result = procedimiento.executeQuery();
            while (result.next()) {
                lista.add(new ServiciosHasEmpleados(result.getInt("codigoSE"),
                result.getInt("codigoServicio"),
                result.getInt("codigoEmpleado"),
                result.getDate("fechaEvento"),
                result.getString(String.valueOf("horaEvento")),
                result.getString("lugarEvento")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaSE = FXCollections.observableList(lista);
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
        return listaEmpleados = FXCollections.observableList(lista);
    }
    
    public void cargarDatos() {
        tblServiciosEmpleados.setItems(getServiciosHasPlatos());
        colCodigoSE.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Integer>("codigoSE"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Integer>("codigoServicio"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Integer>("codigoEmpleado"));
        colFechaEvento.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Date>("fechaEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Time>("horaEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, String>("lugarEvento"));
        
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
    
    public void addSE() {
        try {
            agregarDatos();
            cargarDatos();
            cleanTextField();
            disableTextField();
            btnEditText.setDisable(false);
            btnCancelar.setVisible(false);
            tblServiciosEmpleados.setDisable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void agregarDatos() {
        ServiciosHasEmpleados newSE = new ServiciosHasEmpleados();
        newSE.setFechaEvento(fecha.getSelectedDate());
        newSE.setHoraEvento(txtHoraEvento.getText());
        newSE.setLugarEvento(txtLugarEvento.getText());
        newSE.setCodigoEmpleado(((Empleados)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        newSE.setCodigoServicio(((Servicios)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
//      newServicio.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        
        try {
            PreparedStatement agregar = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicios_has_Empleados(?,?,?,?,?)}");
            agregar.setInt(1,newSE.getCodigoServicio());
            agregar.setInt(2,newSE.getCodigoEmpleado());
            agregar.setDate(3,new java.sql.Date(newSE.getFechaEvento().getTime()));
            agregar.setString(4, String.valueOf(newSE.getHoraEvento()));
            agregar.setString(5, newSE.getLugarEvento());
            agregar.execute();
            listaSE.add(newSE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void cleanTextField() {
        txtCodigoSE.setText("");
        fecha.selectedDateProperty().set(null);
        txtHoraEvento.setText("");
        txtLugarEvento.setText("");
        cmbCodigoEmpleado.getSelectionModel().clearSelection();
        cmbCodigoServicio.getSelectionModel().clearSelection();
//        cmbCodigoEmpresa.getSelectionModel().select(null);
    }

    public void disableTextField() {
        grdFechaEvento.setDisable(true);
        txtHoraEvento.setDisable(true);
        txtLugarEvento.setDisable(true);
        cmbCodigoEmpleado.setDisable(true);
        cmbCodigoServicio.setDisable(true);
    }

    public void ableTextField() {
        grdFechaEvento.setDisable(false);
        txtHoraEvento.setDisable(false);
        txtLugarEvento.setDisable(false);
        cmbCodigoEmpleado.setDisable(false);
        cmbCodigoServicio.setDisable(false);
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
    
    public Empleados buscarEmpleados(int codigoEmpleados){
    Empleados result = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpleados(?)}");
                procedimiento.setInt(1, codigoEmpleados);
                ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                result = new Empleados(registro.getInt("codigoEmpleado"),
                registro.getInt("numeroEmpleado"),
                registro.getString("apellidosEmpleado"),
                registro.getString("nombresEmpleado"),
                registro.getString("direccionEmpleado"),
                registro.getString("telefonoContacto"),
                registro.getString("gradoCocinero"),
                registro.getInt("codigoTipoEmpleado"));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        return result;
    }
    
    public void selectDatafromTable() {
        txtCodigoSE.setText(String.valueOf(((ServiciosHasEmpleados) tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getCodigoSE()));
        fecha.selectedDateProperty().set((((ServiciosHasEmpleados)tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getFechaEvento()));
        txtHoraEvento.setText(((ServiciosHasEmpleados)tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getHoraEvento());
        txtLugarEvento.setText((((ServiciosHasEmpleados)tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getLugarEvento()));
        cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleados(((ServiciosHasEmpleados) tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        cmbCodigoServicio.getSelectionModel().select(buscarServicios(((ServiciosHasEmpleados) tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicio()));
    }
    
    public void btnNuevo(){
        ableTextField();
        cleanTextField();
        btnCancelar.setVisible(true);
        btnEditText.setDisable(true);
        tblServiciosEmpleados.setDisable(true);
    }
    
    public void btnEditText() {
        ableTextField();
        cmbCodigoEmpleado.setDisable(true);
        cmbCodigoServicio.setDisable(true);
        btnNuevo.setDisable(true);
        btnCancelar.setVisible(true);
    }
    
    public void cancelar() {
        disableTextField();
        cleanTextField();
        btnEditText.setDisable(false);
        btnNuevo.setDisable(false);
        btnCancelar.setVisible(false);
        tblServiciosEmpleados.setDisable(false);
    }
    
    public void deleteSE(){
        if(tblServiciosEmpleados.getSelectionModel().getSelectedItem()!= null){
            int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro?", "Eliminar Registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_OPTION){
                try {
                    PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicios_Has_Empleados(?)}");
                    sp.setInt(1, ((ServiciosHasEmpleados)tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getCodigoSE());
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
    
    public void updateSE(){
        if (tblServiciosEmpleados.getSelectionModel().getSelectedItem()!=null) {
            try {
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicios_has_Empleados(?,?,?,?,?,?)}");
                ServiciosHasEmpleados seUpdate = ((ServiciosHasEmpleados)tblServiciosEmpleados.getSelectionModel().getSelectedItem());
                seUpdate.setFechaEvento(fecha.getSelectedDate());
                seUpdate.setHoraEvento(txtHoraEvento.getText());
                seUpdate.setLugarEvento(txtLugarEvento.getText());
                sp.setInt(1, seUpdate.getCodigoSE());
                sp.setInt(2, seUpdate.getCodigoServicio());
                sp.setInt(3,seUpdate.getCodigoEmpleado());
                sp.setDate(4,new java.sql.Date(seUpdate.getFechaEvento().getTime()));
                sp.setString(5,seUpdate.getHoraEvento());
                sp.setString(6,seUpdate.getLugarEvento());
                
                sp.execute();
                System.out.println(sp.execute());
                JOptionPane.showMessageDialog(null, "Datos Actualizados");
                disableTextField();
                cleanTextField();
                btnCancelar.setVisible(false);
                btnEditText.setDisable(false);
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
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        grdFechaEvento.add(fecha, 0, 0);
        fecha.getStylesheets().add("/org/axelescobar/resource/datepicker.css");
        cmbCodigoEmpleado.setItems(getEmpleados());
        cmbCodigoServicio.setItems(getServicios());
    }
}
