package org.iesalandalus.programacion.matriculacion.modelo.negocio.mysql.utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase que gestiona la conexión con la base de datos MySQL. Utiliza el patrón
 * Singleton para asegurar que solo haya una conexión activa en todo el sistema.
 * Esta clase proporciona métodos para establecer y cerrar la conexión con la
 * base de datos MySQL, y gestiona la configuración necesaria para interactuar
 * con la base de datos del sistema de matrícula.
 */
public class MySQL {
    // Constantes para la configuración de la base de datos.
    private static final String HOST = "dbsistemamatriculacion.crwaeekcyj8s.us-east-1.rds.amazonaws.com";
    //private static final String HOST = "dbsistemamatriculacion.c38aw5xnhrps.us-east-1.rds.amazonaws.com";
    //private static final String HOST = "mysql.indalosoftware.com";
    //private static final String USUARIO = "auri";
    //private static final String CONTRASENA = "auri_daw_2425**..";
    private static final String ESQUEMA = "sistemamatriculacion";
    private static final String USUARIO = "sistemamatriculacion";
    private static final String CONTRASENA = "sistemamatriculacion-2025";
    // Variable de conexión estática.
    private static Connection conexion;

    /**
     * Constructor privado para evitar la instanciación directa de la clase.
     * Esta clase es utilizada solo para gestionar la conexión a la base de datos,
     * por lo que no debe ser instanciada.
     */
    private MySQL() {}

    /**
     * Establece una conexión a la base de datos MySQL utilizando el patrón Singleton.
     * Si la conexión ya ha sido establecida previamente, se reutiliza la misma
     * conexión.
     *
     * @return La conexión activa a la base de datos MySQL.
     */
    public static Connection establecerConexion() {
        if (conexion == null) {
            try {
                // Cargar el driver de MySQL.
                Class.forName("com.mysql.cj.jdbc.Driver");

                // URL de conexión.
                String url = "jdbc:mysql://" + HOST + ":3306/" + ESQUEMA;

                // Establecer conexión.
                conexion = DriverManager.getConnection(url, USUARIO, CONTRASENA);
                System.out.println("Conexión establecida con éxito.");
            } catch (ClassNotFoundException e) {
                System.out.println("Error: No se encontró el driver de MySQL.");
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("Error al conectar a la base de datos.");
                e.printStackTrace();
            }
        }
        return conexion;
    }

    /**
     * Cierra la conexión a la base de datos MySQL, si está activa.
     * Se asegura de que la conexión se cierre correctamente y se libera el recurso.
     */
    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
                System.out.println("Conexión cerrada correctamente.");
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión.");
                e.printStackTrace();
            }
        }
    }
}
