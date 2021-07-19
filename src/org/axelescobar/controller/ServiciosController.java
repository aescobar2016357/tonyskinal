package org.axelescobar.controller;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;


import org.axelescobar.bd.Conexion;
import org.axelescobar.beans.Servicios;
import org.axelescobar.system.MainApp;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import org.axelescobar.beans.Empresa;
import org.axelescobar.report.GenerarReporte;

public class ServiciosController implements Initializable {
    private MainApp escPrincipal;
    private ObservableList<Servicios> listaServicios;
    private ObservableList<Empresa> listaEmpresa;
    private DatePicker fecha;
    @FXML private TextField txtCodigoServicio;
    @FXML private GridPane grdFechaServicio;
    @FXML private TextField txtTipoServicio;
    @FXML private TextField txtHoraServicio;
    @FXML private TextField txtLugarServicio;
    @FXML private TextField txtTelefonoContacto;
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private TableView tblServicios;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colFechaServicio;
    @FXML private TableColumn colTipoServicio;
    @FXML private TableColumn colHoraServicio;
    @FXML private TableColumn colLugarServicio;
    @FXML private TableColumn colTelefonoContacto;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnUpdate;
    @FXML private Button btnReporte;
    @FXML private Button btnReload;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditText;
    @FXML private Button btnCancelar;

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
        tblServicios.setItems(getServicios());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicios, Integer>("codigoServicio"));
        colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicios, Date>("fechaServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicios, String>("tipoServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicios, Time>("horaServicio"));
        colLugarServicio.setCellValueFactory(new PropertyValueFactory<Servicios, String>("lugarServicio"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Servicios, String>("telefonoContacto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicios, Integer>("codigoEmpresa"));
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
    
    public void vistaEmpresa() {
        escPrincipal.ventanaEmpresas();
    }
    
    public void addServicio() {
        try {
            agregarDatos();
            cargarDatos();
            cleanTextField();
            disableTextField();
            btnEditText.setDisable(false);
            btnCancelar.setVisible(false);
            tblServicios.setDisable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void agregarDatos() {
        Servicios newServicio = new Servicios();
        newServicio.setFechaServicio(fecha.getSelectedDate());
        newServicio.setTipoServicio(txtTipoServicio.getText());
        newServicio.setHoraServicio(txtHoraServicio.getText());
        newServicio.setLugarServicio(txtLugarServicio.getText());
        newServicio.setTelefonoContacto(txtTelefonoContacto.getText());
        newServicio.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try {
            PreparedStatement agregar = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicios(?,?,?,?,?,?)}");
            agregar.setDate(1,new java.sql.Date(newServicio.getFechaServicio().getTime()));
            agregar.setString(2,newServicio.getTipoServicio());
            agregar.setString(3,newServicio.getHoraServicio());
            agregar.setString(4,newServicio.getLugarServicio());
            agregar.setString(5,newServicio.getTelefonoContacto());
            agregar.setInt(6,newServicio.getCodigoEmpresa());            
            agregar.execute();
            listaServicios.add(newServicio);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cleanTextField() {
        txtCodigoServicio.setText("");
        fecha.selectedDateProperty().set(null);
        txtTipoServicio.setText("");
        txtHoraServicio.setText("");
        txtLugarServicio.setText("");
        txtTelefonoContacto.setText("");
        cmbCodigoEmpresa.getSelectionModel().clearSelection();
//        cmbCodigoEmpresa.getSelectionModel().select(null);
    }

    public void disableTextField() {
        grdFechaServicio.setDisable(true);
        txtTipoServicio.setDisable(true);
        txtHoraServicio.setDisable(true);
        txtLugarServicio.setDisable(true);
        txtTelefonoContacto.setDisable(true);
        cmbCodigoEmpresa.setDisable(true);
    }

    public void ableTextField() {
        grdFechaServicio.setDisable(false);
        txtTipoServicio.setDisable(false);
        txtHoraServicio.setDisable(false);
        txtLugarServicio.setDisable(false);
        txtTelefonoContacto.setDisable(false);
        cmbCodigoEmpresa.setDisable(false);
    }
    
    public Empresa buscarEmpresa(int codigoEmpresa){
    Empresa result = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpresas(?)}");
                procedimiento.setInt(1, codigoEmpresa);
                ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                result = new Empresa(registro.getInt("codigoEmpresa"),
                registro.getString("nombreEmpresa"),
                registro.getString("direccion"),
                registro.getString("telefono"));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        return result;
    }

    public void selectDatafromTable() {
        txtCodigoServicio.setText(String.valueOf(((Servicios) tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        fecha.selectedDateProperty().set((((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio()));
        txtTipoServicio.setText(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio());
        txtHoraServicio.setText(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio());
        txtLugarServicio.setText((((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio()));
        txtTelefonoContacto.setText((((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto()));
        cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicios) tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
    }

    public void btnNuevo(){
        ableTextField();
        cleanTextField();
        btnEditText.setDisable(true);
        tblServicios.setDisable(true);
        btnCancelar.setVisible(true);
    }
    
    public void btnEditText() {
        ableTextField();
        cmbCodigoEmpresa.setDisable(true);
        btnNuevo.setDisable(true);
        btnCancelar.setVisible(true);
    }
    
    public void cancelar() {
        disableTextField();
        cleanTextField();
        btnEditText.setDisable(false);
        btnNuevo.setDisable(false);
        tblServicios.setDisable(false);
        btnCancelar.setVisible(false);
    }

    public void deleteServicio(){
        if(tblServicios.getSelectionModel().getSelectedItem()!= null){
            int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro?", "Eliminar Servicio", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_OPTION){
                try {
                    PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicios(?)}");
                    sp.setInt(1, ((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
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

    public void updateServicio(){
        if (tblServicios.getSelectionModel().getSelectedItem()!=null) {
            try {
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicios(?,?,?,?,?,?,?)}");
                Servicios servicioUpdate = ((Servicios)tblServicios.getSelectionModel().getSelectedItem());
                servicioUpdate.setFechaServicio(fecha.getSelectedDate());
                servicioUpdate.setTipoServicio(txtTipoServicio.getText());
                servicioUpdate.setHoraServicio(txtHoraServicio.getText());
                servicioUpdate.setLugarServicio(txtLugarServicio.getText());
                servicioUpdate.setTelefonoContacto(txtTelefonoContacto.getText());
                sp.setInt(1, servicioUpdate.getCodigoServicio());
                sp.setDate(2,new java.sql.Date(servicioUpdate.getFechaServicio().getTime()));
                sp.setString(3,servicioUpdate.getTipoServicio());
                sp.setString(4,servicioUpdate.getHoraServicio());
                sp.setString(5,servicioUpdate.getLugarServicio());
                sp.setString(6,servicioUpdate.getTelefonoContacto());
                sp.setInt(7,servicioUpdate.getCodigoEmpresa());
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
    
    public void generarReporte(){
        if (tblServicios.getSelectionModel().getSelectedItem()!=null) {
            try {
                imprimirReporte();
                JOptionPane.showMessageDialog(null, "Se ha generado un reporte");
                disableTextField();
                cleanTextField();
                cargarDatos();
                btnEditText.setDisable(false);
                btnCancelar.setVisible(false);
                btnNuevo.setDisable(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            JOptionPane.showMessageDialog(null, "Debe selecionar un registro para generar un reporte");
        }
        
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        int codServicio = Integer.valueOf(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
        parametros.put("codServicio",codServicio);
        GenerarReporte.mostrarReporte("ReporteServicios.jasper", "Reporte de Servicios", parametros);
     }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        grdFechaServicio.add(fecha, 0, 0);
        fecha.getStylesheets().add("/org/axelescobar/resource/datepicker.css");
        cmbCodigoEmpresa.setItems(getEmpresa());
    }
    
}