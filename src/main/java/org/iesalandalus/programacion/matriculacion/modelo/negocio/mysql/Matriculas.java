package org.iesalandalus.programacion.matriculacion.modelo.negocio.mysql;

import org.iesalandalus.programacion.matriculacion.modelo.dominio.*;
import org.iesalandalus.programacion.matriculacion.modelo.negocio.IMatriculas;
import org.iesalandalus.programacion.matriculacion.modelo.negocio.mysql.utilidades.MySQL;

import javax.naming.OperationNotSupportedException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Clase que gestiona una colección de matrículas.
 * Permite insertar, buscar, borrar y obtener información sobre las matrículas,
 * así como filtrarlas por alumno, curso académico o ciclo formativo.
 */
public class Matriculas implements IMatriculas {

    private Connection conexion;
    private static Matriculas instancia = null;
    private ArrayList<Matricula> coleccionMatriculas;


    /**
     * Constructor de la clase Matriculas. Inicia la colección de matrículas.
     */
    public Matriculas() {
        coleccionMatriculas = new ArrayList<>();
        comenzar();
    }

    /**
     * Obtiene la instancia de la clase Matriculas.
     *
     * @return La instancia de la clase Matriculas.
     */
    public static Matriculas getInstancia() {
        if (instancia == null) {
            instancia = new Matriculas();
        }
        return instancia;
    }

    @Override
    public void comenzar() {
        conexion = MySQL.establecerConexion();
    }

    @Override
    public void terminar() {
        MySQL.cerrarConexion();

    }

    /**
     * Obtiene una copia profunda de la colección de matrículas.
     *
     * @return Un array con una copia de las matrículas.
     * @throws OperationNotSupportedException Si no se puede copiar la colección.
     * @throws SQLException Si hay problemas con la base de datos.
     */
    @Override
    public ArrayList<Matricula> get() throws OperationNotSupportedException, SQLException {
        ArrayList<Matricula> copiaMatriculas = new ArrayList<>();
        String query = """
        SELECT m.idMatricula,
               m.cursoAcademico,
               m.fechaMatriculacion,
               m.fechaAnulacion,
               m.dni
        FROM matricula m
        LEFT JOIN alumno a ON m.dni = a.dni
        ORDER BY m.fechaMatriculacion DESC, a.nombre
        """;

        Statement sentencia = conexion.createStatement();
        ResultSet rs = sentencia.executeQuery(query);

        while (rs.next()) {
            try {
                Alumno alumno = Alumnos.getInstancia().buscar(
                        new Alumno("ficticio", rs.getString("dni"), "ficticio@gmail.com", "627673847", LocalDate.of(2000, 1, 1))
                );

                ArrayList<Asignatura> asignaturas = getAsignaturasMatricula(rs.getInt("idMatricula"));

                // Crear matrícula con fecha actual (válida)
                Matricula matricula = new Matricula(
                        rs.getInt("idMatricula"),
                        rs.getString("cursoAcademico"),
                        LocalDate.now(), // Temporal para evitar excepción
                        alumno,
                        asignaturas
                );

                // Luego modificar la fecha real mediante reflexión
                Field fFechaMat = Matricula.class.getDeclaredField("fechaMatriculacion");
                fFechaMat.setAccessible(true);
                fFechaMat.set(matricula, rs.getDate("fechaMatriculacion").toLocalDate());

                if (rs.getDate("fechaAnulacion") != null) {
                    Field fFechaAnulacion = Matricula.class.getDeclaredField("fechaAnulacion");
                    fFechaAnulacion.setAccessible(true);
                    fFechaAnulacion.set(matricula, rs.getDate("fechaAnulacion").toLocalDate());
                }
                copiaMatriculas.add(matricula);

            } catch (Exception e) {
                System.err.println("No se pudo cargar una matrícula: " + e.getMessage());
            }
        }
        return copiaMatriculas;
    }


    /**
     * Obtiene las asignaturas de una matrícula.
     *
     * @param idMatricula El ID de la matrícula.
     * @return Un array con las asignaturas de la matrícula.
     * @throws SQLException Si hay problemas con la base de datos.
     */
    private ArrayList<Asignatura> getAsignaturasMatricula(int idMatricula) throws SQLException{
        String query = """
    			SELECT a.codigo
					, a.nombre
					, a.horasAnuales
					, a.curso
					, a.horasDesdoble
					, a.especialidadProfesorado
					, a.codigoCicloFormativo
				FROM asignaturasMatricula am
				LEFT JOIN asignatura a ON am.codigo = a.codigo
				WHERE am.idMatricula = ?
    			""";
        PreparedStatement pstmt = conexion.prepareStatement(query);
        pstmt.setInt(1, idMatricula);
        ResultSet rs = pstmt.executeQuery();
        ArrayList<Asignatura> asignaturas = new ArrayList<>();
        while (rs.next()) {
            CicloFormativo cicloFormativo = CiclosFormativos.getInstancia().buscar(new CicloFormativo(
                    rs.getInt("codigoCicloFormativo"), "ficticio", new GradoE("gradoe", 1, 1), "ficticio", 1));
            Asignatura asignatura = new Asignatura(
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getInt("horasAnuales"),
                    Curso.valueOf(rs.getString("curso").toUpperCase()),
                    rs.getInt("horasDesdoble"),
                    EspecialidadProfesorado.valueOf(rs.getString("especialidadProfesorado").toUpperCase()),
                    cicloFormativo);
            asignaturas.add(asignatura);
        }
        return asignaturas;
    }

    /**
     * Obtiene el tamaño actual de la colección (número de matrículas almacenadas).
     *
     * @return El tamaño actual de la colección.
     * @throws SQLException Si hay problemas con la base de datos.
     */
    public int getTamano() throws SQLException {
        String query = """
	    		SELECT COUNT(codigo) 
	    		FROM matricula
	    		""";
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        return rs.getInt(1);
    }

    /**
     * Inserta una matrícula en la colección.
     *
     * @param matricula La matrícula a insertar.
     * @throws OperationNotSupportedException Si no se pueden insertar más matrículas
     *         o si ya existe una matrícula con el mismo identificador.
     * @throws NullPointerException Si la matrícula es nula.
     * @throws SQLException Si hay problemas con la base de datos.
     */
    public void insertar(Matricula matricula) throws OperationNotSupportedException, SQLException {
        if (matricula == null) {
            throw new NullPointerException("ERROR: No se puede insertar una matrícula nula.");
        }
        if (buscar(matricula) != null) {
            throw new OperationNotSupportedException("ERROR: Ya existe una matrícula con ese identificador.");
        }
        String query = """
				INSERT INTO matricula
					(idMatricula,
					cursoAcademico,
					fechaMatriculacion,
					fechaAnulacion,
					dni)
				VALUES
					(?, ?, ?, ?, ?)
				""";
        PreparedStatement pstmt = conexion.prepareStatement(query);
        pstmt.setInt(1, matricula.getIdMatricula());
        pstmt.setString(2, matricula.getCursoAcademico());
        pstmt.setDate(3, java.sql.Date.valueOf(matricula.getFechaMatriculacion()));
        if (matricula.getFechaAnulacion() == null) {
            pstmt.setNull(4, java.sql.Types.DATE);
        }else {
            pstmt.setDate(4, java.sql.Date.valueOf(matricula.getFechaAnulacion()));
        }
        pstmt.setString(5, matricula.getAlumno().getDni());
        pstmt.executeUpdate();
        insertarAsignaturasMatricula(matricula.getIdMatricula(), matricula.getColeccionAsignaturas());
    }

    /**
     * Inserta las asignaturas de una matrícula en la colección.
     *
     * @param idMatricula El ID de la matrícula.
     * @param coleccionAsigntauras La colección de asignaturas de la matrícula.
     * @throws SQLException Si hay problemas con la base de datos.
     */
    private void insertarAsignaturasMatricula(int idMatricula, ArrayList<Asignatura> coleccionAsigntauras) throws SQLException{
        String query = """
    			INSERT INTO asignaturasMatricula
					(idMatricula
				    ,codigo)
				VALUES 
					(?, ?)
    			""";
        PreparedStatement pstmt = conexion.prepareStatement(query);
        for (Asignatura asignatura : coleccionAsigntauras) {
            pstmt.setInt(1, idMatricula);
            pstmt.setString(2, asignatura.getCodigo());
            pstmt.executeUpdate();
        }
    }

    /**
     * Busca una matrícula en la colección.
     *
     * @param matricula La matrícula a buscar.
     * @return La matrícula encontrada o null si no se encuentra.
     * @throws OperationNotSupportedException Si no se puede buscar la matrícula.
     * @throws SQLException Si hay problemas con la base de datos.
     * @throws NullPointerException Si la matrícula es nula.
     */
    public Matricula buscar(Matricula matricula) throws OperationNotSupportedException, SQLException {
        if (matricula == null) {
            throw new NullPointerException("ERROR: No se puede buscar una matrícula nula.");
        }
        String query = """
        		SELECT m.idMatricula,
	                m.cursoAcademico,
	                m.fechaMatriculacion,
	                m.fechaAnulacion,
	                m.dni
                FROM matricula m
                WHERE m.idMatricula = ?
        		""";
        PreparedStatement pstmt = conexion.prepareStatement(query);
        pstmt.setInt(1, matricula.getIdMatricula());
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            Alumno alumno = Alumnos.getInstancia().buscar(new Alumno("ficticio", rs.getString("dni"), "ficticio@fake.com", "666554433", LocalDate.of(2000, 1, 1)));
            Matricula matriculaEncontrada = new Matricula(rs.getInt("idMatricula"),
                    rs.getString("cursoAcademico"),
                    LocalDate.now(),
                    alumno,
                    getAsignaturasMatricula(rs.getInt("idMatricula")));
            try {
                Field fFechaMat = Matricula.class.getDeclaredField("fechaMatriculacion");
                fFechaMat.setAccessible(true);
                fFechaMat.set(matricula, rs.getDate("fechaMatriculacion").toLocalDate());

                if (rs.getDate("fechaAnulacion") != null) {
                    Field fFechaAnulacion = Matricula.class.getDeclaredField("fechaAnulacion");
                    fFechaAnulacion.setAccessible(true);
                    fFechaAnulacion.set(matricula, rs.getDate("fechaAnulacion").toLocalDate());
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                System.err.println("Error al asignar fechas por reflexión: " + e.getMessage());
            }
            return matriculaEncontrada;
        }
        return null;
    }

    /**
     * Borra una matrícula de la colección.
     *
     * @param matricula La matrícula a borrar.
     * @throws OperationNotSupportedException Si no existe la matrícula a borrar.
     * @throws SQLException Si hay problemas con la base de datos.
     * @throws NullPointerException Si la matrícula es nula.
     */
    public void borrar(Matricula matricula) throws OperationNotSupportedException, SQLException {
        if (matricula == null) {
            throw new NullPointerException("ERROR: No se puede borrar una matrícula nula.");
        }
        if (buscar(matricula) == null) {
            throw new OperationNotSupportedException("ERROR: No existe ninguna matrícula como la indicada.");
        }
        if (matricula.getFechaAnulacion() == null) {
            // Caso 1: Anular la matrícula (establecer fecha de anulación).
            String query = """
                UPDATE matricula SET fechaAnulacion = ?
                WHERE idMatricula = ?
                """;
            try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
                pstmt.setDate(1, java.sql.Date.valueOf(LocalDate.now())); // Establece la fecha actual como fecha de anulación
                pstmt.setInt(2, matricula.getIdMatricula());
                pstmt.executeUpdate();
                System.out.println("Matrícula anulada correctamente en la base de datos.");
            }
        } else {
            // Caso 2: Eliminar completamente la matrícula.
            String query = """
               DELETE FROM matricula 
               WHERE idMatricula = ?
               """;
            try (PreparedStatement pstmt = conexion.prepareStatement(query)) {
                pstmt.setInt(1, matricula.getIdMatricula());
                pstmt.executeUpdate();
            }
        }
    }

    /**
     * Obtiene las matrículas asociadas a un alumno específico.
     *
     * @param alumno El alumno del cual obtener las matrículas.
     * @return Un arreglo con las matrículas del alumno.
     * @throws OperationNotSupportedException Si no se puede obtener las matrículas.
     * @throws SQLException Si hay problemas con la base de datos.
     */
    public ArrayList<Matricula> get(Alumno alumno) throws OperationNotSupportedException, SQLException {
        ArrayList<Matricula> copiaMatriculas = new ArrayList<>();
        String query = """
        SELECT m.idMatricula, m.cursoAcademico, m.fechaMatriculacion, m.fechaAnulacion,
               m.dni, a.nombre, a.telefono, a.correo, a.fechaNacimiento 
        FROM matricula m
        LEFT JOIN alumno a ON m.dni = a.dni
        WHERE a.dni = ?
        ORDER BY m.fechaMatriculacion DESC, a.nombre
    """;
        PreparedStatement pstmt = conexion.prepareStatement(query);
        pstmt.setString(1, alumno.getDni());
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            try {
                Alumno a = new Alumno(rs.getString("nombre"), rs.getString("dni"),
                        rs.getString("correo"), rs.getString("telefono"),
                        rs.getDate("fechaNacimiento").toLocalDate());

                Matricula matricula = new Matricula(
                        rs.getInt("idMatricula"),
                        rs.getString("cursoAcademico"),
                        LocalDate.now(),
                        a,
                        getAsignaturasMatricula(rs.getInt("idMatricula"))
                );

                // REFLEXIÓN para fijar fechas reales
                Field fMat = Matricula.class.getDeclaredField("fechaMatriculacion");
                fMat.setAccessible(true);
                fMat.set(matricula, rs.getDate("fechaMatriculacion").toLocalDate());

                if (rs.getDate("fechaAnulacion") != null) {
                    Field fAnu = Matricula.class.getDeclaredField("fechaAnulacion");
                    fAnu.setAccessible(true);
                    fAnu.set(matricula, rs.getDate("fechaAnulacion").toLocalDate());
                }

                copiaMatriculas.add(matricula);
            } catch (Exception e) {
                System.err.println("No se pudo cargar una matrícula: " + e.getMessage());
            }
        }
        return copiaMatriculas;
    }


    /**
     * Obtiene las matrículas asociadas a un curso académico específico.
     *
     * @param cursoAcademico El curso académico del cual obtener las matrículas.
     * @return Un arreglo con las matrículas del curso académico.
     * @throws OperationNotSupportedException Si no se puede obtener las matrículas.
     * @throws SQLException Si hay problemas con la base de datos.
     */
    public ArrayList<Matricula> get(String cursoAcademico) throws OperationNotSupportedException, SQLException {
        ArrayList<Matricula> copiaMatriculas = new ArrayList<>();
        String query = """
        SELECT m.idMatricula, m.cursoAcademico, m.fechaMatriculacion, m.fechaAnulacion,
               m.dni, a.nombre, a.telefono, a.correo, a.fechaNacimiento 
        FROM matricula m
        LEFT JOIN alumno a ON m.dni = a.dni
        WHERE m.cursoAcademico = ?
        ORDER BY m.fechaMatriculacion DESC, a.nombre
    """;
        PreparedStatement pstmt = conexion.prepareStatement(query);
        pstmt.setString(1, cursoAcademico);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            try {
                Alumno a = new Alumno(rs.getString("nombre"), rs.getString("dni"),
                        rs.getString("correo"), rs.getString("telefono"),
                        rs.getDate("fechaNacimiento").toLocalDate());

                Matricula matricula = new Matricula(
                        rs.getInt("idMatricula"),
                        rs.getString("cursoAcademico"),
                        LocalDate.now(),
                        a,
                        getAsignaturasMatricula(rs.getInt("idMatricula"))
                );

                Field fMat = Matricula.class.getDeclaredField("fechaMatriculacion");
                fMat.setAccessible(true);
                fMat.set(matricula, rs.getDate("fechaMatriculacion").toLocalDate());

                if (rs.getDate("fechaAnulacion") != null) {
                    Field fAnu = Matricula.class.getDeclaredField("fechaAnulacion");
                    fAnu.setAccessible(true);
                    fAnu.set(matricula, rs.getDate("fechaAnulacion").toLocalDate());
                }

                copiaMatriculas.add(matricula);
            } catch (Exception e) {
                System.err.println("No se pudo cargar una matrícula: " + e.getMessage());
            }
        }
        return copiaMatriculas;
    }


    /**
     * Obtiene las matrículas asociadas a un ciclo formativo específico.
     *
     * @param cicloFormativo El ciclo formativo del cual obtener las matrículas.
     * @return Un arreglo con las matrículas del ciclo formativo.
     * @throws OperationNotSupportedException Si no se puede obtener las matrículas.
     * @throws SQLException Si hay problemas con la base de datos.
     */
    public ArrayList<Matricula> get(CicloFormativo cicloFormativo) throws OperationNotSupportedException, SQLException {
        ArrayList<Matricula> copiaMatriculas = new ArrayList<>();
        String query = """
        SELECT m.idMatricula, m.cursoAcademico, m.fechaMatriculacion, m.fechaAnulacion,
               m.dni, al.nombre, al.telefono, al.correo, al.fechaNacimiento 
        FROM matricula m
        LEFT JOIN asignaturasMatricula am ON m.idMatricula = am.idMatricula
        LEFT JOIN asignatura a ON am.codigo = a.codigo
        LEFT JOIN alumno al ON m.dni = al.dni
        WHERE a.codigoCicloFormativo = ?
        GROUP BY m.idMatricula
        ORDER BY m.fechaMatriculacion DESC, al.nombre
    """;
        PreparedStatement pstmt = conexion.prepareStatement(query);
        pstmt.setInt(1, cicloFormativo.getCodigo());
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            try {
                Alumno al = new Alumno(rs.getString("nombre"), rs.getString("dni"),
                        rs.getString("correo"), rs.getString("telefono"),
                        rs.getDate("fechaNacimiento").toLocalDate());

                Matricula matricula = new Matricula(
                        rs.getInt("idMatricula"),
                        rs.getString("cursoAcademico"),
                        LocalDate.now(),
                        al,
                        getAsignaturasMatricula(rs.getInt("idMatricula"))
                );
                Field fMat = Matricula.class.getDeclaredField("fechaMatriculacion");
                fMat.setAccessible(true);
                fMat.set(matricula, rs.getDate("fechaMatriculacion").toLocalDate());

                if (rs.getDate("fechaAnulacion") != null) {
                    Field fAnu = Matricula.class.getDeclaredField("fechaAnulacion");
                    fAnu.setAccessible(true);
                    fAnu.set(matricula, rs.getDate("fechaAnulacion").toLocalDate());
                }
                copiaMatriculas.add(matricula);
            } catch (Exception e) {
                System.err.println("No se pudo cargar una matrícula: " + e.getMessage());
            }
        }
        return copiaMatriculas;
    }
}
