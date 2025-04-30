package org.iesalandalus.programacion.matriculacion.vista.grafica.controladores;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.*;
import org.iesalandalus.programacion.matriculacion.vista.grafica.VistaGrafica;
import org.iesalandalus.programacion.matriculacion.vista.grafica.utilidades.Dialogos;

import java.util.List;

public class ControladorVentanaAsignatura {

    @FXML private TextField tfCodigo;
    @FXML private TextField tfNombre;
    @FXML private ListView<CicloFormativo> lvCiclos;
    @FXML private ComboBox<String> cbCurso;
    @FXML private ComboBox<String> cbEspecialidad;
    @FXML private TextField tfHoras;
    @FXML private TextField tfDesdoble;
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;
    @FXML private TextField tfBuscarCiclo;

    private ObservableList<CicloFormativo> ciclosObservable = FXCollections.observableArrayList();
    private FilteredList<CicloFormativo> ciclosFiltrados;

    private final ObservableList<CicloFormativo> ciclosDisponibles = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        try {
            // Obtener lista de ciclos
            List<CicloFormativo> listaCiclos = VistaGrafica.getInstancia().getControlador().getCiclosFormativos();
            ciclosDisponibles.setAll(listaCiclos);

            // Filtrado por código en el buscador
            ciclosFiltrados = new FilteredList<>(ciclosDisponibles, p -> true);
            lvCiclos.setItems(ciclosFiltrados);

            // Listener del TextField de búsqueda
            tfBuscarCiclo.textProperty().addListener((obs, oldVal, newVal) -> {
                ciclosFiltrados.setPredicate(ciclo -> {
                    if (newVal == null || newVal.isBlank()) return true;
                    String texto = newVal.toLowerCase();
                    return ciclo.getNombre().toLowerCase().contains(texto)
                            || String.valueOf(ciclo.getCodigo()).contains(texto);
                });
            });

            // Selección única
            lvCiclos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        } catch (Exception e) {
            Dialogos.mostrarDialogoError("Error", "No se pudieron cargar los ciclos.");
        }

        // Cargar curso y especialidad
        cbCurso.setItems(FXCollections.observableArrayList("Primero", "Segundo"));
        cbEspecialidad.setItems(FXCollections.observableArrayList("Informática", "Sistemas", "FOL"));
    }


    @FXML
    private void anadirAsignatura(ActionEvent event) {
        try {
            // Validación de campos obligatorios
            if (tfCodigo.getText().isBlank() || tfNombre.getText().isBlank() || tfHoras.getText().isBlank()
                    || tfDesdoble.getText().isBlank() || cbCurso.getValue() == null
                    || cbEspecialidad.getValue() == null || lvCiclos.getSelectionModel().getSelectedItem() == null) {
                Dialogos.mostrarDialogoError("Campos obligatorios", "Rellena todos los campos correctamente.");
                return;
            }

            String codigo = tfCodigo.getText();
            String nombre = tfNombre.getText();
            CicloFormativo ciclo = lvCiclos.getSelectionModel().getSelectedItem();

            Curso curso = cbCurso.getValue().equalsIgnoreCase("Primero") ? Curso.PRIMERO : Curso.SEGUNDO;

            EspecialidadProfesorado especialidad = switch (cbEspecialidad.getValue().toUpperCase()) {
                case "FOL" -> EspecialidadProfesorado.FOL;
                case "SISTEMAS" -> EspecialidadProfesorado.SISTEMAS;
                default -> EspecialidadProfesorado.INFORMATICA;
            };

            int horas = Integer.parseInt(tfHoras.getText());
            int desdoble = Integer.parseInt(tfDesdoble.getText());

            Asignatura asignatura = new Asignatura(
                    codigo,
                    nombre,
                    horas,
                    curso,
                    desdoble,
                    especialidad,
                    ciclo
            );

            VistaGrafica.getInstancia().getControlador().insertar(asignatura);

            Dialogos.mostrarDialogoInformacion("Asignatura añadida", "La asignatura se ha añadido correctamente.");
            ((Stage) btnAceptar.getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            Dialogos.mostrarDialogoError("Error numérico", "Asegúrate de que las horas y desdoble son valores numéricos válidos.");
        } catch (IllegalArgumentException | NullPointerException e) {
            Dialogos.mostrarDialogoError("Error en los datos", e.getMessage());
        } catch (Exception e) {
            Dialogos.mostrarDialogoError("Error inesperado", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        if (Dialogos.mostrarDialogoConfirmacion("Cancelar", "¿Deseas cerrar sin guardar?")) {
            ((Stage) btnCancelar.getScene().getWindow()).close();
        } else {
            event.consume();
        }
    }
}
