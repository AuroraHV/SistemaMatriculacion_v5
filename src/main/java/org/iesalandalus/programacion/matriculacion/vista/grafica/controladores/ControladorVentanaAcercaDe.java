package org.iesalandalus.programacion.matriculacion.vista.grafica.controladores;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class ControladorVentanaAcercaDe {
    public void cerrarVentana(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}