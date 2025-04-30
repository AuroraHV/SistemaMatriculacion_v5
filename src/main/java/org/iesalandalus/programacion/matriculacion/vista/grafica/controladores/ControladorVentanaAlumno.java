package org.iesalandalus.programacion.matriculacion.vista.grafica.controladores;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.iesalandalus.programacion.matriculacion.controlador.Controlador;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.Alumno;
import org.iesalandalus.programacion.matriculacion.vista.grafica.VistaGrafica;
import org.iesalandalus.programacion.matriculacion.vista.grafica.utilidades.Dialogos;

import javax.naming.OperationNotSupportedException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ControladorVentanaAlumno {

    @FXML
    private TextField tfCorreo;
    @FXML
    private TextField tfNombre;
    @FXML
    private Button btnAnadir;
    @FXML
    private TextField tfTelefono;
    @FXML
    private DatePicker dpNacimiento;
    @FXML
    private TextField tfDni;
    @FXML
    private Button btnCancelar;

    private List<Alumno> coleccionAlumnos;
    private ObservableList<Alumno> alumnosObservable = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        inicializarCampos();
        btnAnadir.setDefaultButton(true);
        btnCancelar.setCancelButton(true);
    }

    private void inicializarCampos() {
        dpNacimiento.setValue(LocalDate.now());

        dpNacimiento.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });

        Platform.runLater(() -> tfNombre.requestFocus());
    }

    @FXML
    void anadirAlumno(ActionEvent event) {
        try {
            String nombre = tfNombre.getText();
            String dni = tfDni.getText();
            String correo = tfCorreo.getText();
            String telefono = tfTelefono.getText();
            LocalDate fechaNacimiento = dpNacimiento.getValue();

            if (dni.isEmpty() || nombre.isEmpty() || correo.isEmpty() || telefono.isEmpty() || fechaNacimiento == null) {
                Dialogos.mostrarDialogoError("Datos incompletos", "Todos los campos son obligatorios.");

            } else {
                Alumno alumno = new Alumno(nombre, dni, correo, telefono, fechaNacimiento);
                VistaGrafica.getInstancia().getControlador().insertar(alumno);
                Dialogos.mostrarDialogoInformacion("Completado", "Alumno insertado correctamente.");
                ((Stage)btnAnadir.getScene().getWindow()).close();
            }

        } catch (Exception e) {
            Dialogos.mostrarDialogoError("Error al insertar el alumno.", e.getMessage());
        }

    }

    public void cargarAlumnos(List<Alumno> coleccionAlumnos, ObservableList<Alumno> alumnosObservable) {
        try {
            this.coleccionAlumnos = VistaGrafica.getInstancia().getControlador().getAlumnos();
            this.alumnosObservable.setAll(coleccionAlumnos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cancelarAlumno(ActionEvent event) {
        if (Dialogos.mostrarDialogoConfirmacion("Confirmación", "¿Deseas cerrar sin guardar?")) {
            ((Stage)btnCancelar.getScene().getWindow()).close();
        }else
            event.consume();
    }

}
