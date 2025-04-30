package org.iesalandalus.programacion.matriculacion.vista.grafica;

import org.iesalandalus.programacion.matriculacion.controlador.Controlador;
import org.iesalandalus.programacion.matriculacion.vista.Vista;

public class VistaGrafica extends Vista {
    private static VistaGrafica instancia;

    private VistaGrafica() {
        super();
    }

    public static VistaGrafica getInstancia() {
        if (instancia == null) {
            instancia = new VistaGrafica();
        }
        return instancia;
    }

    public void comenzar() {
        LanzadorVentanaPrincipal.comenzar();
    }

    public void terminar() {
        getControlador().terminar();
    }
}
