package org.iesalandalus.programacion.matriculacion.vista.grafica.utilidades;

import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.iesalandalus.programacion.matriculacion.vista.grafica.recursos.LocalizadorRecursos;

import java.time.LocalDate;
import java.util.Optional;

public class Dialogos {

	private static final String CSS = LocalizadorRecursos.class.getResource("estilos/estilos.css").toExternalForm();

	private Dialogos() {
		//Evitamos que se puedan instanciar objetos
	}

	public static void mostrarDialogoError(String titulo, String contenido, Stage propietario) {
		Alert dialogo = new Alert(AlertType.ERROR);
		dialogo.getDialogPane().getStylesheets().add(CSS);
		dialogo.setTitle(titulo);
		dialogo.setHeaderText(null);
		dialogo.setContentText(contenido);
		if (propietario != null) {
			dialogo.initModality(Modality.APPLICATION_MODAL);
			dialogo.initOwner(propietario);
		}
		dialogo.showAndWait();
		if (propietario != null)
			propietario.close();
	}

	public static void mostrarDialogoError(String titulo, String contenido) {
		Dialogos.mostrarDialogoError(titulo, contenido, null);
	}

	public static void mostrarDialogoInformacion(String titulo, String contenido, Stage propietario) {
		Alert dialogo = new Alert(AlertType.INFORMATION);
		dialogo.getDialogPane().getStylesheets().add(CSS);
		dialogo.setTitle(titulo);
		dialogo.setHeaderText(null);
		dialogo.setContentText(contenido);
		if (propietario != null) {
			dialogo.initModality(Modality.APPLICATION_MODAL);
			dialogo.initOwner(propietario);
		}
		dialogo.showAndWait();
		if (propietario != null)
			propietario.close();
	}

	public static void mostrarDialogoInformacion(String titulo, String contenido) {
		Dialogos.mostrarDialogoInformacion(titulo, contenido, null);
	}

	public static void mostrarDialogoInformacionPersonalizado(String titulo, Pane contenido, Stage propietario) {
		Alert dialogo = new Alert(AlertType.INFORMATION);
		dialogo.getDialogPane().getStylesheets().add(CSS);
		dialogo.setTitle(titulo);
		dialogo.setHeaderText(null);
		dialogo.setContentText(null);
		dialogo.getDialogPane().setHeader(contenido);
		if (propietario != null) {
			dialogo.initModality(Modality.APPLICATION_MODAL);
			dialogo.initOwner(propietario);
		}
		dialogo.showAndWait();
		if (propietario != null)
			propietario.close();
	}

	public static void mostrarDialogoInformacionPersonalizado(String titulo, Pane contenido) {
		Dialogos.mostrarDialogoInformacionPersonalizado(titulo, contenido, null);
	}

	public static void mostrarDialogoAdvertencia(String titulo, String contenido, Stage propietario) {
		Alert dialogo = new Alert(AlertType.WARNING);
		dialogo.getDialogPane().getStylesheets().add(CSS);
		dialogo.setTitle(titulo);
		dialogo.setHeaderText(null);
		dialogo.setContentText(contenido);
		if (propietario != null) {
			dialogo.initModality(Modality.APPLICATION_MODAL);
			dialogo.initOwner(propietario);
		}
		dialogo.showAndWait();
		if (propietario != null)
			propietario.close();
	}

	public static void mostrarDialogoAdvertencia(String titulo, String contenido) {
		Dialogos.mostrarDialogoAdvertencia(titulo, contenido, null);
	}

	public static String mostrarDialogoTexto(String titulo, String contenido) {
		TextInputDialog dialogo = new TextInputDialog();
		dialogo.getDialogPane().getStylesheets().add(CSS);
		dialogo.setGraphic(null);
		dialogo.setTitle(titulo);
		dialogo.setHeaderText(null);
		dialogo.setContentText(contenido);

		Optional<String> respuesta = dialogo.showAndWait();
		return (respuesta.isPresent() ? respuesta.get() : null);
	}

	public static boolean mostrarDialogoConfirmacion(String titulo, String contenido, Stage propietario) {
		Alert dialogo = new Alert(AlertType.CONFIRMATION);
		dialogo.getDialogPane().getStylesheets().add(CSS);
		dialogo.setTitle(titulo);
		dialogo.setHeaderText(null);
		dialogo.setContentText(contenido);
		if (propietario != null) {
			dialogo.initModality(Modality.APPLICATION_MODAL);
			dialogo.initOwner(propietario);
		}

		Optional<ButtonType> respuesta = dialogo.showAndWait();
		return (respuesta.isPresent() && respuesta.get() == ButtonType.OK);
	}

	public static boolean mostrarDialogoConfirmacion(String titulo, String contenido) {
		return mostrarDialogoConfirmacion(titulo, contenido, null);
	}
	public static LocalDate mostrarDialogoFecha(String titulo, String mensaje) {
		Dialog<LocalDate> dialogo = new Dialog<>();
		dialogo.setTitle(titulo);
		dialogo.setHeaderText(mensaje);
		dialogo.getDialogPane().getStylesheets().add(CSS);

		DatePicker datePicker = new DatePicker(LocalDate.now());

		dialogo.getDialogPane().setContent(datePicker);

		ButtonType okButton = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
		dialogo.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

		dialogo.setResultConverter(dialogButton -> {
			if (dialogButton == okButton) {
				return datePicker.getValue();
			}
			return null;
		});

		Optional<LocalDate> resultado = dialogo.showAndWait();
		return resultado.orElse(null);
	}

}
