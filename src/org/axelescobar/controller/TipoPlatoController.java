package org.axelescobar.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.axelescobar.bd.Conexion;
import org.axelescobar.beans.TipoPlato;
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

public class TipoPlatoController implements Initializable {
    private MainApp escPrincipal;
    private ObservableList<TipoPlato> listaTipoPlato;

    @FXML private TextField txtCodigoTipoPlato;
    @FXML private TextField txtDescripcionTipo;
    @FXML private TableView tblTipoPlato;
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private TableColumn colDescripcionTipo;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnUpdate;
    @FXML private Button btnReporte;
    @FXML private Button btnReload;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditText;
    @FXML private Button btnCancelar;


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
        tblTipoPlato.setItems(getTipoPlato());
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, Integer>("codigoTipoPlato"));
        colDescripcionTipo.setCellValueFactory(new PropertyValueFactory<TipoPlato, String>("descripcionTipo"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();

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

    public void plato() {
        escPrincipal.ventanaPlatos();
    }

    public void addTipoPlato() {
        try {
            agregarDatos();
            cargarDatos();
            cleanTextField();
            disableTextField();
            btnEditText.setDisable(false);
            tblTipoPlato.setDisable(false);
            btnCancelar.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void agregarDatos() {
        TipoPlato newTipoPlato = new TipoPlato();
        newTipoPlato.setDescripcion(txtDescripcionTipo.getText());
        try {
            PreparedStatement agregar = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoPlato(?)}");
            agregar.setString(1,newTipoPlato.getDescripcionTipo());
            agregar.execute();
            listaTipoPlato.add(newTipoPlato);
        } catch (Exception e) {
        e.printStackTrace();
        }
    }

    public void cleanTextField() {
        txtCodigoTipoPlato.setText("");
        txtDescripcionTipo.setText("");
    }

    public void disableTextField() {
        txtDescripcionTipo.setDisable(true);
    }

    public void ableTextField() {
        txtDescripcionTipo.setDisable(false);
    }

    public void selectDatafromTable() {
        txtCodigoTipoPlato.setText(String.valueOf(((TipoPlato) tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
        txtDescripcionTipo.setText(((TipoPlato) tblTipoPlato.getSelectionModel().getSelectedItem()).getDescripcionTipo());
    }

    public void btnNuevo(){
        ableTextField();
        cleanTextField();
        btnEditText.setDisable(true);
        tblTipoPlato.setDisable(true);
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
        tblTipoPlato.setDisable(false);
        btnCancelar.setVisible(false);
    }

    public void deleteTipoPlato(){
        if(tblTipoPlato.getSelectionModel().getSelectedItem()!= null){
            int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro?", "Eliminar TipoPlato", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_OPTION){
                try {
                    PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoPlato(?)}");
                    sp.setInt(1, ((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
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

    public void updateTipoPlato(){
        if (tblTipoPlato.getSelectionModel().getSelectedItem()!=null) {
            try {
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarTipoPlato(?,?)}");
                TipoPlato TipoPlatoUpdate = ((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem());
                TipoPlatoUpdate.setDescripcion(txtDescripcionTipo.getText());
                sp.setInt(1, TipoPlatoUpdate.getCodigoTipoPlato());
                sp.setString(2, TipoPlatoUpdate.getDescripcionTipo());
                sp.execute();
                JOptionPane.showMessageDialog(null, "Datos Actualizados");
                disableTextField();
                cleanTextField();
                btnNuevo.setDisable(false);
                btnEditText.setDisable(false);
                btnCancelar.setVisible(false);
                cargarDatos();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            JOptionPane.showMessageDialog(null, "Debe selecionar un registro para editar");
        }
    }
    
}