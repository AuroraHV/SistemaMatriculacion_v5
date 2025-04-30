package org.iesalandalus.programacion.matriculacion.vista;

import org.iesalandalus.programacion.matriculacion.vista.grafica.VistaGrafica;
import org.iesalandalus.programacion.matriculacion.vista.texto.VistaTexto;

public enum FactoriaVista {
    TEXTO {
        @Override
        public Vista crear() {
            return new VistaTexto();
        }
    },
    GRAFICA {
        @Override
        public Vista crear() {
            return VistaGrafica.getInstancia();
        }
    };

    public abstract Vista crear();

}
