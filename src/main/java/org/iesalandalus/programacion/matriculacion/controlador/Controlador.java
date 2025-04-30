package org.iesalandalus.programacion.matriculacion.controlador;

import org.iesalandalus.programacion.matriculacion.modelo.dominio.Alumno;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.Asignatura;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.CicloFormativo;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.Matricula;
import org.iesalandalus.programacion.matriculacion.modelo.Modelo;
import org.iesalandalus.programacion.matriculacion.vista.Vista;
import org.iesalandalus.programacion.matriculacion.vista.texto.VistaTexto;

import javax.naming.OperationNotSupportedException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase Controlador que gestiona la lógica de la aplicación, coordinando las interacciones
 * entre el modelo y la vista. Es responsable de delegar las operaciones de inserción, búsqueda,
 * eliminación y obtención de los datos a la capa del modelo.
 */
public class Controlador {

    // Instancia del modelo que maneja los datos.
    private Modelo modelo;

    // Instancia de la vista que se encarga de la interfaz con el usuario.
    private Vista vista;

    /**
     * Constructor del controlador que recibe las instancias de Modelo y Vista.
     *
     * @param modelo La instancia del modelo que maneja los datos.
     * @param vista La instancia de la vista que interactúa con el usuario.
     * @throws NullPointerException Si el modelo o la vista son nulos.
     */
    public Controlador (Modelo modelo, Vista vista) {
        if (modelo == null) {
            throw new NullPointerException("ERROR: El modelo no puede ser nulo.");
        }
        if (vista == null) {
            throw new NullPointerException("ERROR: La vista no puede ser nula.");
        }
        this.modelo = modelo;
        this.vista = vista;
        this.vista.setControlador(this);
    }

    /**
     * Inicia la ejecución de la aplicación, iniciando tanto el modelo como la vista.
     */
    public void comenzar() throws OperationNotSupportedException {
        this.modelo.comenzar();
        this.vista.comenzar();
    }

    /**
     * Finaliza la ejecución de la aplicación, cerrando el modelo.
     */
    public void terminar() {
        modelo.terminar();
    }

    /**
     * Métodos para insertar, buscar, borrar y obtener ALUMNOS.
     */
    public void insertar(Alumno alumno) throws OperationNotSupportedException, SQLException {
        this.modelo.insertar(alumno);
    }
    public Alumno buscar(Alumno alumno) throws SQLException {
        return this.modelo.buscar(alumno);
    }
    public void borrar(Alumno alumno) throws OperationNotSupportedException, SQLException {
        // Comprobar si el alumno tiene alguna matrícula asociada
        if (!getMatriculas(alumno).isEmpty()) {
            throw new OperationNotSupportedException("ERROR: No se puede borrar un alumno con matrículas asociadas.");
        }
        // Si no tiene matrículas asociadas, borrar el alumno
        this.modelo.borrar(alumno);
    }
    public ArrayList<Alumno> getAlumnos() throws SQLException {
        return this.modelo.getAlumnos();
    }

    /**
     * Métodos para insertar, buscar, borrar y obtener ASIGNATURAS.
     */
    public void insertar(Asignatura asignatura) throws OperationNotSupportedException, SQLException {
        this.modelo.insertar(asignatura);
    }
    public Asignatura buscar(Asignatura asignatura) throws SQLException {
        return this.modelo.buscar(asignatura);
    }
    public void borrar(Asignatura asignatura) throws OperationNotSupportedException, SQLException {

        for (Matricula m: getMatriculas()) {
            if (m.getColeccionAsignaturas().contains(asignatura)) {
                throw new OperationNotSupportedException("ERROR: No se puede borrar una asignatura con matrículas asociadas.");
            }
        }
        this.modelo.borrar(asignatura);
    }
    public ArrayList<Asignatura> getAsignaturas() throws SQLException {
        return this.modelo.getAsignaturas();
    }

    /**
     * Métodos para insertar, buscar, borrar y obtener CICLOS FORMATIVOS.
     */
    public void insertar(CicloFormativo cicloFormativo) throws OperationNotSupportedException, SQLException {
        this.modelo.insertar(cicloFormativo);
    }
    public CicloFormativo buscar(CicloFormativo cicloFormativo) throws SQLException {
        return this.modelo.buscar(cicloFormativo);
    }
    public void borrar(CicloFormativo cicloFormativo) throws OperationNotSupportedException, SQLException {
        // Comprobar si el ciclo formativo tiene alguna matrícula asociada
        if (!getMatriculas(cicloFormativo).isEmpty()) {
            throw new OperationNotSupportedException("ERROR: No se puede borrar un ciclo formativo con matrículas asociadas.");
        }
        // Si no tiene matrículas asociadas, borrar el ciclo formativo
        this.modelo.borrar(cicloFormativo);
    }
    public ArrayList<CicloFormativo> getCiclosFormativos() throws SQLException {
        return this.modelo.getCiclosFormativos();
    }

    /**
     * Métodos para insertar, buscar, borrar y obtener MATRICULAS.
     */
    public void insertar(Matricula matricula) throws OperationNotSupportedException, SQLException {
        this.modelo.insertar(matricula);
    }
    public Matricula buscar(Matricula matricula) throws OperationNotSupportedException, SQLException {
        return this.modelo.buscar(matricula);
    }
    public void borrar(Matricula matricula) throws OperationNotSupportedException, SQLException {
        this.modelo.borrar(matricula);
    }
    public ArrayList<Matricula> getMatriculas() throws OperationNotSupportedException, SQLException {
        return this.modelo.getMatriculas();
    }
    // Obtener matriculas de un alumno.
    public ArrayList<Matricula> getMatriculas(Alumno alumno) throws OperationNotSupportedException, SQLException {
        return this.modelo.getMatriculas(alumno);
    }
    // Obtener matriculas de un ciclo formativo.
    public ArrayList<Matricula> getMatriculas(CicloFormativo cicloFormativo) throws OperationNotSupportedException, SQLException {
        return this.modelo.getMatriculas(cicloFormativo);
    }
    // Obtener matriculas de un curso academico.
    public ArrayList<Matricula> getMatriculas(String cursoAcademico) throws OperationNotSupportedException, SQLException {
        return this.modelo.getMatriculas(cursoAcademico);
    }
}
