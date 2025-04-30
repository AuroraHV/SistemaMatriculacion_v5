package org.iesalandalus.programacion.matriculacion.vista.grafica.controladores;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.*;
import org.iesalandalus.programacion.matriculacion.vista.grafica.VistaGrafica;
import org.iesalandalus.programacion.matriculacion.vista.grafica.utilidades.Dialogos;

public class ControladorVentanaCiclo {

    @FXML private TextField tfCodigo;
    @FXML private TextField tfNombreCiclo;
    @FXML private TextField tfFamilia;
    @FXML private TextField tfHoras;
    @FXML private ComboBox<String> cbTipoGrado;
    @FXML private TextField tfNombreGrado;
    @FXML private TextField tfAnosGrado;
    @FXML private Label lblModalidad;
    @FXML private ComboBox<Modalidad> cbModalidad;
    @FXML private Label lblEdiciones;
    @FXML private TextField tfEdiciones;
    @FXML private Button btnAnadir;
    @FXML private Button btnCancelar;

    private final ObservableList<String> tiposGrado = FXCollections.observableArrayList("Grado D", "Grado E");

    @FXML
    public void initialize() {
        cbTipoGrado.setItems(tiposGrado);
        cbTipoGrado.setOnAction(e -> actualizarCamposGrado());
        // ⚠️ Cargamos correctamente los enums como items del ComboBox
        cbModalidad.setItems(FXCollections.observableArrayList(Modalidad.values()));
        cbModalidad.setPromptText("Seleccionar"); // Texto por defecto
        cbModalidad.getSelectionModel().clearSelection(); // Aseguramos que no haya valor seleccionado al principio

        Platform.runLater(() -> tfCodigo.requestFocus());
    }

    private void actualizarCamposGrado() {
        String seleccionado = cbTipoGrado.getValue();
        if ("Grado D".equals(seleccionado)) {
            lblModalidad.setVisible(true);
            cbModalidad.setVisible(true);
            cbModalidad.setPromptText("Seleccionar modalidad");
            cbModalidad.getSelectionModel().clearSelection();

            lblEdiciones.setVisible(false);
            tfEdiciones.setText("");
            tfEdiciones.setPromptText("");
            tfEdiciones.setVisible(false);

        } else if ("Grado E".equals(seleccionado)) {
            lblEdiciones.setVisible(true);
            tfEdiciones.setPromptText("Mín. 1");
            tfEdiciones.setVisible(true);

            lblModalidad.setVisible(false);
            cbModalidad.getSelectionModel().clearSelection();
            cbModalidad.setPromptText("Seleccionar modalidad");
            cbModalidad.setVisible(false);

        }
    }

    @FXML
    public void anadirCiclo(ActionEvent event) {
        try {
            // Validar primero que ningún campo obligatorio esté vacío
            if (tfCodigo.getText().isBlank() || tfNombreCiclo.getText().isBlank() ||
                    tfFamilia.getText().isBlank() || tfHoras.getText().isBlank() ||
                    cbTipoGrado.getValue() == null || tfNombreGrado.getText().isBlank() ||
                    tfAnosGrado.getText().isBlank() ||
                    ("Grado D".equals(cbTipoGrado.getValue()) && cbModalidad.getValue() == null) ||
                    ("Grado E".equals(cbTipoGrado.getValue()) && tfEdiciones.getText().isBlank())) {

                Dialogos.mostrarDialogoError("Campos incompletos", "Por favor, rellena todos los campos requeridos.");
                return;
            }

            int codigo = Integer.parseInt(tfCodigo.getText());
            String nombreCiclo = tfNombreCiclo.getText();
            String familia = tfFamilia.getText();
            int horas = Integer.parseInt(tfHoras.getText());
            String tipoStr = cbTipoGrado.getValue();
            String nombreGrado = tfNombreGrado.getText();
            int anos = Integer.parseInt(tfAnosGrado.getText());

            Grado grado;

            if ("Grado D".equals(tipoStr)) {
                Modalidad modalidad = cbModalidad.getValue();
                grado = new GradoD(nombreGrado, anos, modalidad);
            } else {
                int ediciones = Integer.parseInt(tfEdiciones.getText());
                grado = new GradoE(nombreGrado, anos, ediciones);
            }

            CicloFormativo ciclo = new CicloFormativo(codigo, familia, grado, nombreCiclo, horas);

            VistaGrafica.getInstancia().getControlador().insertar(ciclo);
            Dialogos.mostrarDialogoInformacion("Ciclo añadido", "Ciclo formativo añadido correctamente.");
            ((Stage) btnAnadir.getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            Dialogos.mostrarDialogoError("Error numérico", "Asegúrate de introducir valores numéricos válidos.");
        } catch (IllegalArgumentException | NullPointerException e) {
            Dialogos.mostrarDialogoError("Error en los datos", e.getMessage());
        } catch (Exception e) {
            Dialogos.mostrarDialogoError("Error inesperado", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void cancelar(ActionEvent event) {
        if (Dialogos.mostrarDialogoConfirmacion("Cancelar", "¿Deseas cerrar sin guardar?")) {
            ((Stage) btnCancelar.getScene().getWindow()).close();
        } else {
            event.consume();
        }
    }
}
