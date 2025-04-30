package org.iesalandalus.programacion.matriculacion.vista.grafica.controladores;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.Alumno;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.Asignatura;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.Matricula;
import org.iesalandalus.programacion.matriculacion.vista.grafica.VistaGrafica;
import org.iesalandalus.programacion.matriculacion.vista.grafica.utilidades.Dialogos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ControladorVentanaMatricula {

    @FXML private TextField tfDniAlumno;
    @FXML private TextField tfIdMatricula;
    @FXML private TextField tfBuscarAsignatura;
    @FXML private ListView<Asignatura> lvAsignaturas;
    @FXML private DatePicker dpFechaMatricula;
    @FXML private DatePicker dpFechaAnulacion;
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;

    private final ObservableList<Asignatura> asignaturasDisponibles = FXCollections.observableArrayList();
    private final FilteredList<Asignatura> asignaturasFiltradas = new FilteredList<>(asignaturasDisponibles, a -> true);
    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private void initialize() {
        try {
            List<Asignatura> lista = VistaGrafica.getInstancia().getControlador().getAsignaturas();
            asignaturasDisponibles.setAll(lista);
            lvAsignaturas.setItems(asignaturasFiltradas);
            lvAsignaturas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            dpFechaMatricula.setValue(LocalDate.now());

            dpFechaMatricula.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || date.isAfter(LocalDate.now()));
                }
            });

            dpFechaAnulacion.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || date.isAfter(LocalDate.now()));
                }
            });

            dpFechaMatricula.setConverter(new javafx.util.StringConverter<>() {
                @Override
                public String toString(LocalDate date) {
                    return (date != null) ? date.format(formatoFecha) : "";
                }

                @Override
                public LocalDate fromString(String string) {
                    return (string != null && !string.isBlank()) ? LocalDate.parse(string, formatoFecha) : null;
                }
            });

            dpFechaAnulacion.setConverter(new javafx.util.StringConverter<>() {
                @Override
                public String toString(LocalDate date) {
                    return (date != null) ? date.format(formatoFecha) : "";
                }

                @Override
                public LocalDate fromString(String string) {
                    return (string != null && !string.isBlank()) ? LocalDate.parse(string, formatoFecha) : null;
                }
            });

            tfBuscarAsignatura.textProperty().addListener((obs, oldVal, newVal) -> {
                asignaturasFiltradas.setPredicate(asignatura -> {
                    if (newVal == null || newVal.isBlank()) return true;
                    String texto = newVal.toLowerCase();
                    return asignatura.getNombre().toLowerCase().contains(texto)
                            || asignatura.getCodigo().toLowerCase().startsWith(texto);
                });
            });


        } catch (Exception e) {
            Dialogos.mostrarDialogoError("Error", "No se pudieron cargar las asignaturas.");
        }

        btnAceptar.setDefaultButton(true);
        btnCancelar.setCancelButton(true);
    }

    @FXML
    private void registrarMatricula(ActionEvent event) {
        try {
            String dni = tfDniAlumno.getText();
            int id = Integer.parseInt(tfIdMatricula.getText());
            List<Asignatura> seleccionadas = lvAsignaturas.getSelectionModel().getSelectedItems();

            if (dni.isBlank() || seleccionadas.isEmpty() || dpFechaMatricula.getValue() == null) {
                Dialogos.mostrarDialogoAdvertencia("Datos incompletos", "Por favor, rellena todos los campos obligatorios.");
                return;
            }

            Alumno ficticio = new Alumno("Temp", dni, "temp@temp.com", "600000000", LocalDate.of(2000, 1, 1));
            Alumno alumnoBuscado = VistaGrafica.getInstancia().getControlador().buscar(ficticio);

            if (alumnoBuscado == null) {
                Dialogos.mostrarDialogoError("No encontrado", "No existe ningún alumno con ese DNI.");
                return;
            }

            String cursoAcademico = obtenerCursoAcademico(dpFechaMatricula.getValue());
            Matricula nueva = new Matricula(
                    id,
                    cursoAcademico,
                    dpFechaMatricula.getValue(),
                    alumnoBuscado,
                    new ArrayList<>(seleccionadas)
            );


            if (dpFechaAnulacion.getValue() != null) {
                nueva.setFechaAnulacion(dpFechaAnulacion.getValue());
            }

            VistaGrafica.getInstancia().getControlador().insertar(nueva);
            Dialogos.mostrarDialogoInformacion("Éxito", "Matrícula registrada correctamente.");
            ((Stage) btnAceptar.getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            Dialogos.mostrarDialogoError("Error de formato", "El ID debe ser un número válido.");
        } catch (Exception e) {
            Dialogos.mostrarDialogoError("Error", e.getMessage());
        }
    }

    private String obtenerCursoAcademico(LocalDate fecha) {
        int anio = fecha.getYear();
        return String.format("%02d-%02d", anio % 100, (anio + 1) % 100);
    }

    @FXML
    private void cancelar(ActionEvent event) {
        boolean confirmar = Dialogos.mostrarDialogoConfirmacion(
                "Cancelar matrícula",
                "¿Deseas cerrar sin guardar?"
        );
        if (confirmar) {
            ((Stage) btnCancelar.getScene().getWindow()).close();
        }
    }

}
