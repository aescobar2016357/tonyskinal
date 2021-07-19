package org.axelescobar.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;

import org.axelescobar.bd.Conexion;
import org.axelescobar.beans.TipoPlato;
import org.axelescobar.beans.Platos;
import org.axelescobar.system.MainApp;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.axelescobar.report.GenerarReporte;



public class PlatosController implements Initializable {
    private MainApp escPrincipal;
    private ObservableList<Platos> listaPlatos;
    private ObservableList<TipoPlato> listaTipoPlato;
    
    @FXML private TextField txtCodigoPlato;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtNombrePlato;
    @FXML private TextField txtDescripcionPlato;
    @FXML private TextField txtPrecioPlato;
    @FXML private ComboBox cmbCodigoTipoPlato;
    @FXML private TableView tblPlatos;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colNombrePlato;
    @FXML private TableColumn colPrecioPlato;
    @FXML private TableColumn colDescripcionPlato;
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnUpdate;
    @FXML private Button btnReporte;
    @FXML private Button btnReload;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditText;
    @FXML private Button btnCancelar;
    
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
    
    public ObservableList<TipoPlato> getTipoPlato(){
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoPlato()}");
            ResultSet result = procedimiento.executeQuery();
            while (result.next()) {
                lista.add(new TipoPlato(result.getInt("codigoTipoPlato"),
                result.getString("descripcionTipo")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableList(lista);
    }
    
    public void cargarDatos() {
        tblPlatos.setItems(getPlato());
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Platos, Integer>("codigoPlato"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Platos, Integer>("cantidad"));
        colNombrePlato.setCellValueFactory(new PropertyValueFactory<Platos, String>("nombrePlato"));
        colDescripcionPlato.setCellValueFactory(new PropertyValueFactory<Platos, String>("descripcionPlato"));
        colPrecioPlato.setCellValueFactory(new PropertyValueFactory<Platos, Float>("precioPlato"));
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<Platos, Integer>("codigoTipoPlato"));
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

    public void tipoPlato() {
      escPrincipal.ventanaTipoPlato();  
    }
    
    public void addPlato() {
        try {
            agregarDatos();
            cargarDatos();
            cleanTextField();
            disableTextField();
            btnEditText.setDisable(false);
            btnCancelar.setVisible(false);
            tblPlatos.setDisable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void agregarDatos() {
        Platos newPlato = new Platos();
        newPlato.setCantidad(Integer.parseInt(txtCantidad.getText()));
        newPlato.setNombrePlato(txtNombrePlato.getText());
        newPlato.setDescripcionPlato(txtDescripcionPlato.getText());
        newPlato.setPrecioPlato(Float.parseFloat(txtPrecioPlato.getText()));
        newPlato.setCodigoTipoPlato(((TipoPlato)cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
        try {
            PreparedStatement agregar = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPlatos(?,?,?,?,?)}");
            agregar.setInt(1,newPlato.getCantidad());
            agregar.setString(2,newPlato.getNombrePlato());
            agregar.setString(3,newPlato.getDescripcionPlato());
            agregar.setFloat(4,newPlato.getPrecioPlato());
            agregar.setInt(5,newPlato.getCodigoTipoPlato());
            agregar.execute();
            listaPlatos.add(newPlato);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void cleanTextField() {
        txtCodigoPlato.setText("");
        txtCantidad.setText("");
        txtNombrePlato.setText("");
        txtDescripcionPlato.setText("");
        txtPrecioPlato.setText("");
        cmbCodigoTipoPlato.getSelectionModel().clearSelection();
//        cmbCodigoEmpresa.getSelectionModel().select(null);
    }

    public void disableTextField() {
        txtCantidad.setDisable(true);
        txtNombrePlato.setDisable(true);
        txtDescripcionPlato.setDisable(true);
        txtPrecioPlato.setDisable(true);
        cmbCodigoTipoPlato.setDisable(true);
    }

    public void ableTextField() {
        txtCantidad.setDisable(false);
        txtNombrePlato.setDisable(false);
        txtDescripcionPlato.setDisable(false);
        txtPrecioPlato.setDisable(false);
        cmbCodigoTipoPlato.setDisable(false);
    }
    
    public TipoPlato buscarTipoPlato(int codigoTipoPlato){
    TipoPlato result = null;
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoPlato(?)}");
                procedimiento.setInt(1, codigoTipoPlato);
                ResultSet registro = procedimiento.executeQuery();
                while(registro.next()){
                    result = new TipoPlato(registro.getInt("codigoTipoPlato"),
                    registro.getString("descripcionTipo"));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        return result;
    }
    
     public void selectDatafromTable() {
        txtCodigoPlato.setText(String.valueOf(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        txtCantidad.setText(String.valueOf(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getCantidad()));
        txtNombrePlato.setText(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato());
        txtDescripcionPlato.setText(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getDescripcionPlato());
        txtPrecioPlato.setText(String.valueOf(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
        cmbCodigoTipoPlato.getSelectionModel().select(buscarTipoPlato(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
    }
     
     public void btnNuevo(){
        ableTextField();
        cleanTextField();
        btnEditText.setDisable(true);
        tblPlatos.setDisable(true);
        btnCancelar.setVisible(true);
    }
    
    public void btnEditText() {
        ableTextField();
        cmbCodigoTipoPlato.setDisable(true);
        btnNuevo.setDisable(true);
        btnCancelar.setVisible(true);
    }
    
    public void cancelar() {
        disableTextField();
        cleanTextField();
        btnEditText.setDisable(false);
        btnNuevo.setDisable(false);
        tblPlatos.setDisable(false);
        btnCancelar.setVisible(false);
    }
    
    public void deleteServicio(){
        if(tblPlatos.getSelectionModel().getSelectedItem()!= null){
            int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro?", "Eliminar Plato", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_OPTION){
                try {
                    PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPlatos(?)}");
                    sp.setInt(1, ((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
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
        if (tblPlatos.getSelectionModel().getSelectedItem()!=null) {
            try {
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarPlatos(?,?,?,?,?,?)}");
                Platos platoUpdate = ((Platos)tblPlatos.getSelectionModel().getSelectedItem());
                platoUpdate.setCantidad(Integer.parseInt(txtCantidad.getText()));
                platoUpdate.setNombrePlato(txtNombrePlato.getText());
                platoUpdate.setDescripcionPlato(txtDescripcionPlato.getText());
                platoUpdate.setPrecioPlato(Float.parseFloat(txtPrecioPlato.getText()));
                sp.setInt(1, platoUpdate.getCodigoPlato());
                sp.setInt(2,platoUpdate.getCantidad());
                sp.setString(3,platoUpdate.getNombrePlato());
                sp.setString(4,platoUpdate.getDescripcionPlato());
                sp.setFloat(5,platoUpdate.getPrecioPlato());
                sp.setInt(6,platoUpdate.getCodigoTipoPlato());
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
        imprimirReporte();
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoPlato",null);
        GenerarReporte.mostrarReporte("ReportePlatos.jasper", "Reporte de Platos", null);
     }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoTipoPlato.setItems(getTipoPlato());

    }
}