package org.iesalandalus.programacion.matriculacion.vista.texto;

/**
 * Enumeración Opcion que representa las diferentes opciones disponibles en el menú de la aplicación.
 * Cada opción está asociada a una cadena que se muestra en la consola.
 * Las opciones incluyen insertar, buscar, borrar y mostrar alumnos, ciclos formativos, asignaturas y matrículas.
 */
public enum Opcion {
    //1-Opciones del menu
    SALIR("Salir") {
        @Override
        public void ejecutar() {
            System.out.println("Salir");
            vistaTexto.terminar();
        }
    },
    INSERTAR_ALUMNO("Insertar alumno") {
        @Override
        public void ejecutar() {
            System.out.println("------------------------");
            System.out.println("Insertar alumno");
            System.out.println("------------------------");
            vistaTexto.insertarAlumno();
        }
    },
    BUSCAR_ALUMNO("Buscar alumno") {
        @Override
        public void ejecutar() {
            System.out.println("------------------------");
            System.out.println("Buscar alumno");
            System.out.println("------------------------");
            vistaTexto.buscarAlumno();
        }
    },
    BORRAR_ALUMNO("Borrar alumno") {
        @Override
        public void ejecutar() {
            System.out.println("------------------------");
            System.out.println("Borrar alumno");
            System.out.println("------------------------");
            vistaTexto.borrarAlumno();
        }
    },
    MOSTRAR_ALUMNOS("Mostrar alumnos") {
        @Override
        public void ejecutar() {
            System.out.println("------------------------");
            System.out.println("Mostrar alumno");
            System.out.println("------------------------");
            vistaTexto.mostrarAlumnos();
        }
    },
    INSERTAR_CICLO_FORMATIVO("Insertar ciclo formativo") {
        @Override
        public void ejecutar() {
            System.out.println("------------------------");
            System.out.println("Insertar ciclo formativo");
            System.out.println("------------------------");
            vistaTexto.insertarCicloFormativo();
        }
    },
    BUSCAR_CICLO_FORMATIVO("Buscar ciclo formativo") {
        @Override
        public void ejecutar() {
            System.out.println("------------------------");
            System.out.println("Buscar ciclo formativo");
            System.out.println("------------------------");
            vistaTexto.buscarCicloFormativo();
        }
    },
    BORRAR_CICLO_FORMATIVO("Borrar ciclo formativo") {
        @Override
        public void ejecutar() {
            System.out.println("------------------------");
            System.out.println("Borrar ciclo formativo");
            System.out.println("------------------------");
            vistaTexto.borrarCicloFormativo();
        }
    },
    MOSTRAR_CICLOS_FORMATIVOS("Mostrar ciclos formativos") {
        @Override
        public void ejecutar() {
            System.out.println("------------------------");
            System.out.println("Mostrar ciclo formativo");
            System.out.println("------------------------");
            vistaTexto.mostrarCiclosFormativos();
        }
    },
    INSERTAR_ASIGNATURA("Insertar asignatura") {
        @Override
        public void ejecutar() {
            System.out.println("------------------------");
            System.out.println("Insertar asignatura");
            System.out.println("------------------------");
            vistaTexto.insertarAsignatura();
        }
    },
    BUSCAR_ASIGNATURA("Buscar asignatura") {
        @Override
        public void ejecutar() {
            System.out.println("------------------------");
            System.out.println("Buscar asignatura");
            System.out.println("------------------------");
            vistaTexto.buscarAsignatura();
        }
    },
    BORRAR_ASIGNATURA("Borrar asignatura") {
        @Override
        public void ejecutar() {
            System.out.println("------------------------");
            System.out.println("Borrar asignatura");
            System.out.println("------------------------");
            vistaTexto.borrarAsignatura();
        }
    },
    MOSTRAR_ASIGNATURAS("Mostrar asignaturas") {
        @Override
        public void ejecutar() {
            System.out.println("------------------------");
            System.out.println("Mostrar asignaturas");
            System.out.println("------------------------");
            vistaTexto.mostrarAsignaturas();
        }
    },
    INSERTAR_MATRICULA("Insertar matrícula") {
        @Override
        public void ejecutar() {
            System.out.println("------------------------");
            System.out.println("Insertar matrícula");
            System.out.println("------------------------");
            vistaTexto.insertarMatricula();
        }
    },
    BUSCAR_MATRICULA("Buscar matrícula") {
        @Override
        public void ejecutar() {
            System.out.println("------------------------");
            System.out.println("Buscar matrícula");
            System.out.println("------------------------");
            vistaTexto.buscarMatricula();
        }
    },
    ANULAR_MATRICULA("Anular matrícula") {
        @Override
        public void ejecutar() {
            System.out.println("------------------------");
            System.out.println("Anular matrícula");
            System.out.println("------------------------");
            vistaTexto.anularMatricula();
        }
    },
    MOSTRAR_MATRICULAS("Mostrar matrículas") {
        @Override
        public void ejecutar() {
            System.out.println("------------------------");
            System.out.println("Mostrar matrículas");
            System.out.println("------------------------");
            vistaTexto.mostrarMatriculas();
        }
    },
    MOSTRAR_MATRICULAS_ALUMNO("Mostrar matrículas de un alumno") {
        @Override
        public void ejecutar() {
            System.out.println("---------------------------------");
            System.out.println("Mostrar matrículas de un alumno");
            System.out.println("---------------------------------");
            vistaTexto.mostrarMatriculasPorAlumno();
        }
    },
    MOSTRAR_MATRICULAS_CICLO_FORMATIVO("Mostrar matrículas de un ciclo formativo") {
        @Override
        public void ejecutar() {
            System.out.println("------------------------------------------");
            System.out.println("Mostrar matrículas de un ciclo formativo");
            System.out.println("------------------------------------------");
            vistaTexto.mostrarMatriculasPorCicloFormativo();
        }
    },
    MOSTRAR_MATRICULAS_CURSO_ACADEMICO("Mostrar matrículas de un curso académico") {
        @Override
        public void ejecutar() {
            System.out.println("------------------------------------------");
            System.out.println("Mostrar matrículas de un curso academico");
            System.out.println("------------------------------------------");
            vistaTexto.mostrarMatriculasPorCursoAcademico();
        }
    };

    // Atributo que contiene la cadena a mostrar asociada a cada opción.
    private final String mensajeAMostrar;

    // Instancia de la vista asociada a la opción.
    private static VistaTexto vistaTexto;

    /**
     * Constructor de la enumeración Opcion que asocia un mensaje a mostrar a cada opción.
     * @param mensajeAMostrar Mensaje descriptivo que se mostrará cuando se imprima la opción.
     */
    private Opcion (String mensajeAMostrar){
        this.mensajeAMostrar=mensajeAMostrar;
    }

    /**
     * Método abstracto que debe ser implementado en cada opción del enum.
     */
    public abstract void ejecutar();

    /**
     * Método para establecer la vista que se usará en la ejecución de las opciones.
     *
     * @param vistaTexto Instancia de la clase Vista.
     */
    public static void setVista(VistaTexto vistaTexto) {
        Opcion.vistaTexto = vistaTexto;
    }

    /**
     * Devuelve la representación en cadena de la opción, que incluye su índice en la enumeración
     * seguido del mensaje a mostrar.
     * @return Representación en cadena de la opción.
     */
    @Override
    public String toString() {
        return this.ordinal() + ".- " + this.mensajeAMostrar;
    }
}
