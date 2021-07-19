package org.axelescobar.controller;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import org.axelescobar.system.MainApp;
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
import org.axelescobar.beans.Empresa;
import org.axelescobar.beans.Presupuesto;
import org.axelescobar.report.GenerarReporte;

public class PresupuestoController implements Initializable {
    private MainApp escPrincipal;
     private ObservableList<Presupuesto> listaPresupuesto;
    private ObservableList<Empresa> listaEmpresa;
    private DatePicker fecha;
    @FXML private TextField txtCodigoPresupuesto;
    @FXML private GridPane grdFechaSolicitud;
    @FXML private TextField txtCantidadPresupuesto;
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private TableView tblPresupuesto;
    @FXML private TableColumn colCodigoPresupuesto;
    @FXML private TableColumn colFechaSolicitud;
    @FXML private TableColumn colCantidadPresupuesto;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnUpdate;
    @FXML private Button btnReporte;
    @FXML private Button btnReload;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditText;
    @FXML private Button btnCancelar;

    public ObservableList<Presupuesto> getPresupuesto(){
        ArrayList<Presupuesto> lista = new ArrayList<Presupuesto>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPresupuesto()}");
            ResultSet result = procedimiento.executeQuery();
            while (result.next()) {
                lista.add(new Presupuesto(result.getInt("codigoPresupuesto"),
                result.getDate("fechaSolicitud"), 
                result.getFloat("CantidadPresupuesto"), 
                result.getInt("codigoEmpresa")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPresupuesto = FXCollections.observableList(lista);
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
        tblPresupuesto.setItems(getPresupuesto());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoPresupuesto"));
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<Presupuesto, Date>("fechaSolicitud"));
        colCantidadPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Float>("cantidadPresupuesto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoEmpresa"));
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
    
    public void addPresupuesto() {
        try {
            agregarDatos();
            cargarDatos();
            cleanTextField();
            disableTextField();
            btnEditText.setDisable(false);
            btnCancelar.setVisible(false);
            tblPresupuesto.setDisable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void agregarDatos() {
        Presupuesto newPresupuesto = new Presupuesto();
        newPresupuesto.setFechaSolicitud(fecha.getSelectedDate());
        newPresupuesto.setCantidadPresupuesto(Float.parseFloat(txtCantidadPresupuesto.getText()));
        newPresupuesto.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try {
            PreparedStatement agregar = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPresupuesto(?,?,?)}");
            agregar.setDate(1, new java.sql.Date(newPresupuesto.getFechaSolicitud().getTime()));
            agregar.setFloat(2,newPresupuesto.getCantidadPresupuesto());
            agregar.setInt(3,newPresupuesto.getCodigoEmpresa());
            agregar.execute();
            listaPresupuesto.add(newPresupuesto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void cleanTextField() {
        txtCodigoPresupuesto.setText("");
        fecha.selectedDateProperty().set(null);
        txtCantidadPresupuesto.setText("");
        cmbCodigoEmpresa.getSelectionModel().clearSelection();

    }

    public void disableTextField() {
        grdFechaSolicitud.setDisable(true);
        txtCantidadPresupuesto.setDisable(true);
        cmbCodigoEmpresa.setDisable(true);
    }

    public void ableTextField() {
        grdFechaSolicitud.setDisable(false);
        txtCantidadPresupuesto.setDisable(false);
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
        txtCodigoPresupuesto.setText(String.valueOf(((Presupuesto) tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
        fecha.selectedDateProperty().set(((Presupuesto) tblPresupuesto.getSelectionModel().getSelectedItem()).getFechaSolicitud());
        txtCantidadPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
        cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuesto) tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
    }
    
    public void btnNuevo(){
        ableTextField();
        cleanTextField();
        btnEditText.setDisable(true);
        tblPresupuesto.setDisable(true);
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
        tblPresupuesto.setDisable(false);
        btnCancelar.setVisible(false);
    }
    
    public void deletePresupuesto(){
        if(tblPresupuesto.getSelectionModel().getSelectedItem()!= null){
            int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro?", "Eliminar Presupuesto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_OPTION){
                try {
                    PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPresupuesto(?)}");
                    sp.setInt(1, ((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
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

    public void updatePresupuesto(){
        if (tblPresupuesto.getSelectionModel().getSelectedItem()!=null) {
            try {
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarPresupuesto(?,?,?,?)}");
                Presupuesto presupuestoUpdate = ((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem());
                presupuestoUpdate.setFechaSolicitud(fecha.getSelectedDate());
                presupuestoUpdate.setCantidadPresupuesto(Float.parseFloat(txtCantidadPresupuesto.getText()));
                sp.setInt(1, presupuestoUpdate.getCodigoPresupuesto());
                sp.setDate(2, new java.sql.Date(presupuestoUpdate.getFechaSolicitud().getTime()));
                sp.setFloat(3, presupuestoUpdate.getCantidadPresupuesto());
                sp.setInt(4, presupuestoUpdate.getCodigoEmpresa());
                sp.execute();
                JOptionPane.showMessageDialog(null, "Datos Actualizados");
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
            JOptionPane.showMessageDialog(null, "Debe selecionar un registro para actualizar");
        }
    }
    
    public void generarReporte(){
        if (tblPresupuesto.getSelectionModel().getSelectedItem()!=null) {
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
        int codEmpresa = Integer.valueOf(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        parametros.put("codEmpresa",codEmpresa);
        GenerarReporte.mostrarReporte("ReportPresupuesto.jasper", "Reporte de Presupuesto", parametros);
     }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        grdFechaSolicitud.add(fecha, 0, 0);
        fecha.getStylesheets().add("/org/axelescobar/resource/datepicker.css");
        cmbCodigoEmpresa.setItems(getEmpresa());

    }
}