package org.iesalandalus.programacion.matriculacion.vista.grafica;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.iesalandalus.programacion.matriculacion.vista.grafica.controladores.ControladorVentanaPrincipal;
import org.iesalandalus.programacion.matriculacion.vista.grafica.recursos.LocalizadorRecursos;
import org.iesalandalus.programacion.matriculacion.vista.grafica.utilidades.Dialogos;

import java.util.Optional;

import static javafx.application.Application.launch;

public class LanzadorVentanaPrincipal extends Application {
    public static void comenzar() {
        launch();
    }

    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LocalizadorRecursos.class.getResource("vistas/VentanaPrincipal.fxml"));

            escenarioPrincipal.setOnCloseRequest(e->confirmaCierreVentana(escenarioPrincipal,e));
            Parent raiz = fxmlLoader.load();
            Scene escena = new Scene(raiz, 900, 600);

            escena.setOnKeyPressed((KeyEvent event) -> {
                switch (event.getCode()) {
                    case ESCAPE -> {
                        boolean salir = Dialogos.mostrarDialogoConfirmacion("Salir", "¿Realmente quieres salir de la aplicación?");
                        if (salir) {
                            VistaGrafica.getInstancia().getControlador().terminar();
                            Platform.exit();
                        }
                    }
                }
            });

            escenarioPrincipal.setTitle("Sistema de Matriculación");
            escenarioPrincipal.setScene(escena);
            escenarioPrincipal.setResizable(false);

            escenarioPrincipal.getIcons().add(new javafx.scene.image.Image(LocalizadorRecursos.class.getResourceAsStream("imagenes/logo_ies_al_andalus.png")));
            escenarioPrincipal.show();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    private void confirmaCierreVentana(Stage escenarioCerrar, WindowEvent e)
    {
        if (Dialogos.mostrarDialogoConfirmacion("Ventana Principal", "¿Realmente quieres salir de la aplicación?"))
        {
            escenarioCerrar.close();
        }
        else
            e.consume();
    }

}


