package org.iesalandalus.programacion.matriculacion.vista;

import org.iesalandalus.programacion.matriculacion.controlador.Controlador;

public abstract class Vista {
    private Controlador controlador;

    /**
     * Establece el controlador de la vista.
     *
     * @param controlador El controlador que se asignará a la vista.
     * @throws NullPointerException Si el controlador es nulo.
     */
    public void setControlador(Controlador controlador) {
        if (controlador == null) {
            throw new NullPointerException("ERROR: El controlador no puede ser nulo.");
        }
        this.controlador = controlador;
    }

    public Controlador getControlador() {
        return controlador;
    }

    public abstract void comenzar();

    public abstract void terminar();
}
