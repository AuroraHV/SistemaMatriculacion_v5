package org.iesalandalus.programacion.matriculacion.vista.grafica.controladores;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.stage.WindowEvent;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.Matricula;
import org.iesalandalus.programacion.matriculacion.vista.grafica.VistaGrafica;

import org.iesalandalus.programacion.matriculacion.controlador.Controlador;
import org.iesalandalus.programacion.matriculacion.modelo.dominio.*;
import org.iesalandalus.programacion.matriculacion.vista.grafica.VistaGrafica;
import org.iesalandalus.programacion.matriculacion.vista.grafica.recursos.LocalizadorRecursos;
import org.iesalandalus.programacion.matriculacion.vista.grafica.utilidades.Dialogos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static javafx.scene.input.KeyCode.ESCAPE;

public class ControladorVentanaPrincipal {

    // INICIO y BARRA MENÚ

    @FXML private Button btnEntrar;
    @FXML private TabPane tpSistema;
    @FXML private Tab tbAlumnos;
    @FXML private Tab tbAsignaturas;
    @FXML private Tab tbMatriculas;
    @FXML private Tab tbCiclos;

    // ALUMNOS

    private final ObservableList<Alumno> alumnosObservable = FXCollections.observableArrayList();
    private List<Alumno> coleccionAlumnos = new ArrayList<>();
    private List<Matricula> coleccionMatriculas = new ArrayList<>();

    @FXML private TextField tfBuscarAlumno;

    @FXML private TableView<Alumno> tvAlumnos;
    @FXML private TableColumn<Alumno, String> tcNombreAlumno;
    @FXML private TableColumn<Alumno, String> tcDniAlumno;
    @FXML private TableColumn<Alumno, String> tcCorreoAlumno;
    @FXML private TableColumn<Alumno, String> tcTelefonoAlumno;
    @FXML private TableColumn<Alumno, LocalDate> tcNacimientoAlumno;
    @FXML private TableColumn<Alumno, Integer> tcNiaAlumno;

    @FXML private TableView<Matricula> tvMatriculaAlumno;
    @FXML private TableColumn<Matricula, Integer> tcIDMatriculaAs;
    @FXML private TableColumn<Matricula, String> tcAlumnoMatriculaAs;
    @FXML private TableColumn<Matricula, String> tcDNIMatriculaAs;
    @FXML private TableColumn<Matricula, String> tcFechaMatriculaAs;
    @FXML private TableColumn<Matricula, String> tcAnulacionMatriculaAs;
    @FXML private TableColumn<Matricula, String> tcAsignaturasMatriculaAs;

    // CICLOS

    private List<CicloFormativo> coleccionCiclos = new ArrayList<>();
    private final ObservableList<CicloFormativo> ciclosObservable = FXCollections.observableArrayList();

    @FXML private TableView<CicloFormativo> tvCiclos;
    @FXML private TableColumn<CicloFormativo, Integer> tcCodigoCiclo;
    @FXML private TableColumn<CicloFormativo, String> tcNombreCiclo;
    @FXML private TableColumn<CicloFormativo, String> tcFamiliaCiclo;
    @FXML private TableColumn<CicloFormativo, String> tcGradoCiclo;
    @FXML private TableColumn<CicloFormativo, Integer> tcHorasCiclo;
    @FXML private TableColumn<CicloFormativo, String> tcTipoGradoCiclo;
    @FXML private TableColumn<CicloFormativo, Integer> tcAnosCiclo;
    @FXML private TableColumn<CicloFormativo, String> tcModalidadCiclo;
    @FXML private TableColumn<CicloFormativo, String> tcEdicionesCiclo;

    @FXML private TextField tfBuscarCiclo;

    @FXML private TableView<Matricula> tvMatriculaCiclo;
    @FXML private TableColumn<Matricula, String> tcCicloMatriculaAsC;
    @FXML private TableColumn<Matricula, Integer> tcCodigoMatriculaAsC;
    @FXML private TableColumn<Matricula, Integer> tcIDMatriculaAsC;
    @FXML private TableColumn<Matricula, String> tcMatriculacionMatriculaAsC;
    @FXML private TableColumn<Matricula, String> tcAnulacionMatriculaC;
    @FXML private TableColumn<Matricula, String> tcAsignaturasMatriculaAsC;

    // ASIGNATURAS

    @FXML private TableView<Asignatura> tvAsignaturas;
    @FXML private TableColumn<Asignatura, String> tcCodigoAsignatura;
    @FXML private TableColumn<Asignatura, String> tcNombreAsignatura;
    @FXML private TableColumn<Asignatura, String> tcCicloAsignatura;
    @FXML private TableColumn<Asignatura, Curso> tcCursoAsignatura;
    @FXML private TableColumn<Asignatura, EspecialidadProfesorado> tcEspecialidadAsignatura;
    @FXML private TableColumn<Asignatura, Integer> tcHorasAsignatura;
    @FXML private TableColumn<Asignatura, Integer> tcHorasDesdobleAsignatura;

    @FXML private TextField tfBuscarAsignatura;

    @FXML private TableView<CicloFormativo> tvCicloAsignatura;
    @FXML private TableColumn<CicloFormativo, String> tcNombreAsignaturaCA;
    @FXML private TableColumn<CicloFormativo, String> tcCodigoAsignaturaCA;
    @FXML private TableColumn<CicloFormativo, String> tcNombreCicloCA;
    @FXML private TableColumn<CicloFormativo, Integer> tcCodigoCicloCA;
    @FXML private TableColumn<CicloFormativo, String> tcFamiliaCicloCA;
    @FXML private TableColumn<CicloFormativo, Integer> tcHorasCicloCA;
    @FXML private TableColumn<CicloFormativo, String> tcTipoGradoCicloCA;
    @FXML private TableColumn<CicloFormativo, String> tcGradoCicloCA;
    @FXML private TableColumn<CicloFormativo, Integer> tcAniosCicloCA;
    @FXML private TableColumn<CicloFormativo, String> tcModalidadCicloCA;
    @FXML private TableColumn<CicloFormativo, String> tcEdicionesCicloCA;


    private final ObservableList<Asignatura> asignaturasObservable = FXCollections.observableArrayList();
    private List<Asignatura> coleccionAsignaturas = new ArrayList<>();

    // MATRICULAS

    @FXML private TableView<Matricula> tvMatriculas;
    @FXML private TableColumn<Matricula, Integer> tcIDMatricula;
    @FXML private TableColumn<Matricula, String> tcAlumnoMatricula;
    @FXML private TableColumn<Matricula, String> tcDNIMatricula;
    @FXML private TableColumn<Matricula, String> tcCursoMatricula;
    @FXML private TableColumn<Matricula, String> tcAsignaturasMatricula;
    @FXML private TableColumn<Matricula, String> tcMatriculacionMatricula;
    @FXML private TableColumn<Matricula, String> tcAnulacionMatricula;

    @FXML private TableView<Matricula> tvMatriculaCurso;
    @FXML private TableColumn<Matricula, String> tcCursoMatriculaAsM;
    @FXML private TableColumn<Matricula, Integer> tcIDMatriculaAsM;
    @FXML private TableColumn<Matricula, String> tcAsignaturasMatriculaAsM;
    @FXML private TableColumn<Matricula, String> tcMatriculacionMatriculaAsM;
    @FXML private TableColumn<Matricula, String> tcAnulacionMatriculaAsM;

    @FXML private TextField tfBuscarMatriculas;

    private final ObservableList<Matricula> matriculasObservable = FXCollections.observableArrayList();


    @FXML
    public void initialize() {

        //INICIO
        btnEntrar.setDefaultButton(true);

        //ALUMNOS

        tvAlumnos.setPlaceholder(new Label("Cargando alumnos..."));
        tvMatriculaAlumno.setPlaceholder(new Label("Selecciona un alumno."));

        Task<Void> cargarTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    coleccionAlumnos = VistaGrafica.getInstancia().getControlador().getAlumnos();
                    coleccionMatriculas = VistaGrafica.getInstancia().getControlador().getMatriculas();

                    Platform.runLater(() -> {
                        alumnosObservable.setAll(coleccionAlumnos);
                        mostrarAlumnos();
                    });
                } catch (Exception e) {
                    Platform.runLater(() ->
                            tvAlumnos.setPlaceholder(new Label("Error al cargar los alumnos."))
                    );
                    e.printStackTrace();
                }
                return null;
            }
        };
        new Thread(cargarTask).start();

        tfBuscarAlumno.textProperty().addListener((obs, oldVal, newVal) -> filtrarAlumnos(newVal));

        tvAlumnos.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                mostrarAlumnoSeleccionado(newSel);
                mostrarMatriculaAlumno(newSel);
            } else {
                tvMatriculaAlumno.getItems().clear();
                tvMatriculaAlumno.setPlaceholder(new Label("Selecciona un alumno."));
            }
        });
        tvMatriculaAlumno.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                mostrarMatriculaSeleccionada(newSel);
            }
        });

        ContextMenu menu = new ContextMenu();
        MenuItem anadirItem = new MenuItem("Añadir alumno");
        anadirItem.setOnAction(e -> anadirAlumno(null));
        MenuItem eliminarItem = new MenuItem("Eliminar alumno");
        eliminarItem.setOnAction(e -> eliminarAlumno(null));
        menu.getItems().addAll(anadirItem, eliminarItem);
        tvAlumnos.setContextMenu(menu);

        //CICLOS

        tvCiclos.setPlaceholder(new Label("Cargando ciclos..."));
        tvMatriculaCiclo.setPlaceholder(new Label("Selecciona un ciclo."));

        Task<Void> cargarCiclosTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    coleccionCiclos = VistaGrafica.getInstancia().getControlador().getCiclosFormativos();
                    coleccionMatriculas = VistaGrafica.getInstancia().getControlador().getMatriculas();
                    Platform.runLater(() -> {
                        ciclosObservable.setAll(coleccionCiclos);
                        mostrarTablaCiclos();
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> tvCiclos.setPlaceholder(new Label("Error al cargar los ciclos.")));
                    e.printStackTrace();
                }
                return null;
            }
        };
        new Thread(cargarCiclosTask).start();

        tfBuscarCiclo.textProperty().addListener((obs, oldVal, newVal) -> filtrarCiclos(newVal));

        tvCiclos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mostrarCicloSeleccionado(newVal);
            } else {
                tvMatriculaCiclo.getItems().clear();
                tvMatriculaCiclo.setPlaceholder(new Label("Selecciona un ciclo."));
            }
        });

        tvMatriculaCiclo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mostrarMatriculaCicloSeleccionada(newVal);
            }
        });

        ContextMenu menuCiclo = new ContextMenu();
        MenuItem anadirCiclo = new MenuItem("Añadir ciclo");
        anadirCiclo.setOnAction(e -> anadirCiclo(null));
        MenuItem eliminarCiclo = new MenuItem("Eliminar ciclo");
        eliminarCiclo.setOnAction(e -> eliminarCiclo(null));
        menuCiclo.getItems().addAll(anadirCiclo, eliminarCiclo);
        tvCiclos.setContextMenu(menuCiclo);

        // ASIGNATURAS

        tvAsignaturas.setPlaceholder(new Label("Cargando asignaturas..."));
        tvCicloAsignatura.setPlaceholder(new Label("Selecciona una asignatura."));

        Task<Void> cargarAsignaturasTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    coleccionAsignaturas = VistaGrafica.getInstancia().getControlador().getAsignaturas();
                    Platform.runLater(() -> {
                        asignaturasObservable.setAll(coleccionAsignaturas);
                        mostrarTablaAsignaturas();
                    });
                } catch (Exception e) {
                    Platform.runLater(() ->
                            tvAsignaturas.setPlaceholder(new Label("Error al cargar las asignaturas."))
                    );
                    e.printStackTrace();
                }
                return null;
            }
        };
        new Thread(cargarAsignaturasTask).start();

        tfBuscarAsignatura.textProperty().addListener((obs, oldVal, newVal) -> filtrarAsignaturas(newVal));

        tvAsignaturas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                mostrarAsignaturaSeleccionada(newSel);
                mostrarCicloAsignatura(newSel);
            }
        });
        tvCicloAsignatura.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mostrarCicloAsignaturaSeleccionado(newVal);
            }
        });

        ContextMenu menuAsignaturas = new ContextMenu();
        MenuItem anadirAsig = new MenuItem("Añadir Asignatura");
        anadirAsig.setOnAction(e -> anadirAsignatura(null));
        MenuItem eliminarAsig = new MenuItem("Eliminar Asignatura");
        eliminarAsig.setOnAction(e -> eliminarAsignatura(null));
        menuAsignaturas.getItems().addAll(anadirAsig, eliminarAsig);
        tvAsignaturas.setContextMenu(menuAsignaturas);

        // MATRÍCULAS

        tvMatriculas.setPlaceholder(new Label("Cargando matrículas..."));
        tvMatriculaCurso.setPlaceholder(new Label("Selecciona una matrícula."));

        Task<Void> cargarMatriculasTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    coleccionMatriculas = VistaGrafica.getInstancia().getControlador().getMatriculas();
                    Platform.runLater(() -> {
                        matriculasObservable.setAll(coleccionMatriculas);
                        mostrarTablaMatriculas();
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> tvMatriculas.setPlaceholder(new Label("Error al cargar las matrículas.")));
                    e.printStackTrace();
                }
                return null;
            }
        };
        new Thread(cargarMatriculasTask).start();

        tfBuscarMatriculas.textProperty().addListener((obs, oldVal, newVal) -> filtrarMatriculas(newVal));
        tvMatriculas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                mostrarDetallesMatricula(newSel);
                mostrarLaMatriculaSeleccionada(newSel);
                mostrarMatriculaCurso(newSel); //Esta es la clave
            } else {
                tvMatriculaCurso.getItems().clear();
                tvMatriculaCurso.setPlaceholder(new Label("Selecciona una matrícula."));
            }
        });
        tvMatriculaCurso.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                mostrarMatriculaCursoSeleccionada(newSel);
            }
        });

        ContextMenu menuMatriculas = new ContextMenu();
        MenuItem registrarMat = new MenuItem("Registrar Matrícula");
        registrarMat.setOnAction(e -> registrarMatricula(null));
        MenuItem anularMat = new MenuItem("Anular Matrícula");
        anularMat.setOnAction(e -> anularMatricula(null));
        menuMatriculas.getItems().addAll(registrarMat, anularMat);
        tvMatriculas.setContextMenu(menuMatriculas);
    }

    // BARRA MENÚ

    @FXML
    void irInicio(ActionEvent event) {
        tpSistema.getSelectionModel().selectFirst(); // Va al tab "Inicio"
    }

    @FXML
    void irAlumnos(ActionEvent event) {
        tpSistema.getSelectionModel().select(tbAlumnos);
    }

    @FXML
    void irCiclos(ActionEvent event) {
        tpSistema.getSelectionModel().select(tbCiclos);
    }

    @FXML
    void irAsignaturas(ActionEvent event) {
        tpSistema.getSelectionModel().select(tbAsignaturas);
    }

    @FXML
    void irMatriculas(ActionEvent event) {
        tpSistema.getSelectionModel().select(tbMatriculas);
    }

    @FXML
    void salir(ActionEvent event) {
        boolean confirmar = Dialogos.mostrarDialogoConfirmacion("Salir", "¿Realmente quieres salir de la aplicación?");
        if (confirmar) Platform.exit();
        btnEntrar.setDefaultButton(true);
    }

    @FXML
    void anadirElemento(ActionEvent event) {
        Tab seleccionado = tpSistema.getSelectionModel().getSelectedItem();

        if (seleccionado.equals(tbAlumnos)) {
            anadirAlumno(null);
        } else if (seleccionado.equals(tbAsignaturas)) {
            anadirAsignatura(null);
        } else if (seleccionado.equals(tbCiclos)) {
            anadirCiclo(null);
        } else if (seleccionado.equals(tbMatriculas)) {
            registrarMatricula(null);
        } else {
            Dialogos.mostrarDialogoInformacion("Información", "Accede al apartado donde quieres realizar la inserción.");
        }
    }

    @FXML
    void eliminarElemento(ActionEvent event) {
        Tab seleccionado = tpSistema.getSelectionModel().getSelectedItem();

        if (seleccionado.equals(tbAlumnos)) {
            eliminarAlumno(null);
        } else if (seleccionado.equals(tbAsignaturas)) {
            eliminarAsignatura(null);
        } else if (seleccionado.equals(tbCiclos)) {
            eliminarCiclo(null);
        } else if (seleccionado.equals(tbMatriculas)) {
            anularMatricula(null);
        } else {
            Dialogos.mostrarDialogoInformacion("Información", "Accede al apartado donde quieres realizar la eliminación o anulación.");
        }
    }

    @FXML
    public void acercaDe(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(LocalizadorRecursos.class.getResource("vistas/VentanaAcercaDe.fxml"));

            Parent root = loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Acerca de");
            ventana.initModality(Modality.APPLICATION_MODAL);
            ventana.setScene(new Scene(root));
            ventana.setResizable(false);
            ventana.getIcons().add(new javafx.scene.image.Image(LocalizadorRecursos.class.getResourceAsStream("imagenes/logo_ies_al_andalus.png")));
            ventana.showAndWait();
        } catch (Exception e) {
            Dialogos.mostrarDialogoError("Error", "No se pudo abrir la ventana Acerca de.");
            e.printStackTrace();
        }
    }

    // INICIO

    @FXML public void entrarAlSistema(ActionEvent event) {
        tpSistema.getSelectionModel().select(tbAlumnos);
    }

    // ALUMNOS

    private void mostrarAlumnos() {
        tcNombreAlumno.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcDniAlumno.setCellValueFactory(new PropertyValueFactory<>("dni"));
        tcCorreoAlumno.setCellValueFactory(new PropertyValueFactory<>("correo"));
        tcTelefonoAlumno.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        tcNacimientoAlumno.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        tcNiaAlumno.setCellValueFactory(new PropertyValueFactory<>("nia"));

        tcNacimientoAlumno.setCellFactory(col -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            @Override
            protected void updateItem(LocalDate fecha, boolean empty) {
                super.updateItem(fecha, empty);
                setText((empty || fecha == null) ? "" : fecha.format(formatter));
            }
        });

        tvAlumnos.setItems(alumnosObservable);
        tvAlumnos.setPlaceholder(new Label("No hay alumnos disponibles."));
    }

    private void mostrarAlumnoSeleccionado(Alumno alumno) {
        System.out.println("Alumno seleccionado: " + alumno);
        mostrarMatriculaAlumno(alumno);
    }
    private void mostrarMatriculaSeleccionada(Matricula matricula) {
        System.out.println("Matrícula asociada al alumno seleccionado: " + matricula);
    }

    private void filtrarAlumnos(String filtro) {
        FilteredList<Alumno> filtrados = new FilteredList<>(alumnosObservable, a -> true);
        filtrados.setPredicate(a -> {
            if (filtro == null || filtro.isBlank()) return true;
            String texto = filtro.toLowerCase();
            return a.getNombre().toLowerCase().contains(texto)
                    || a.getDni().toLowerCase().startsWith(texto)
                    || a.getCorreo().toLowerCase().contains(texto)
                    || a.getTelefono().toLowerCase().startsWith(texto)
                    || String.valueOf(a.getNia()).startsWith(texto);
        });
        tvAlumnos.setItems(filtrados);
    }

    private void mostrarMatriculaAlumno(Alumno alumno) {
        List<Matricula> matriculas = coleccionMatriculas.stream()
                .filter(m -> m.getAlumno().equals(alumno))
                .toList();

        ObservableList<Matricula> obs = FXCollections.observableArrayList(matriculas);
        tvMatriculaAlumno.setItems(obs);

        tvMatriculaAlumno.setPlaceholder(new Label("No hay matrículas asociadas."));

        tcAlumnoMatriculaAs.setCellValueFactory(m -> new SimpleStringProperty(m.getValue().getAlumno().getNombre()));
        tcDNIMatriculaAs.setCellValueFactory(m -> new SimpleStringProperty(m.getValue().getAlumno().getDni()));
        tcIDMatriculaAs.setCellValueFactory(m -> new SimpleIntegerProperty(m.getValue().getIdMatricula()).asObject());

        tcFechaMatriculaAs.setCellValueFactory(m -> {
            LocalDate fecha = m.getValue().getFechaMatriculacion();
            return new SimpleStringProperty(fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        });

        tcAnulacionMatriculaAs.setCellValueFactory(m -> {
            LocalDate fecha = m.getValue().getFechaAnulacion();
            return new SimpleStringProperty((fecha != null) ? fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "No anulada");
        });

        tcAsignaturasMatriculaAs.setCellValueFactory(m -> {
            String nombres = m.getValue().getColeccionAsignaturas().stream()
                    .map(Asignatura::getNombre)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("Sin asignaturas");
            return new SimpleStringProperty(nombres);
        });
    }

    @FXML
    void anadirAlumno(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(LocalizadorRecursos.class.getResource("vistas/VentanaAlumno.fxml"));
            Parent root = loader.load();
            ControladorVentanaAlumno controlador = loader.getController();
            controlador.cargarAlumnos(coleccionAlumnos, alumnosObservable);

            Stage ventana = new Stage();
            ventana.setScene(new Scene(root));
            ventana.setTitle("Añadir Alumno");
            ventana.setOnCloseRequest(e->confirmaCierreVentana(ventana,e));
            ventana.initModality(Modality.APPLICATION_MODAL);
            ventana.setResizable(false);
            ventana.getIcons().add(new javafx.scene.image.Image(LocalizadorRecursos.class.getResourceAsStream("imagenes/logo_ies_al_andalus.png")));
            ventana.showAndWait();

            coleccionAlumnos = new ArrayList<>(VistaGrafica.getInstancia().getControlador().getAlumnos());
            alumnosObservable.setAll(coleccionAlumnos);
        } catch (Exception e) {
            Dialogos.mostrarDialogoError("Error", "No se pudo abrir la ventana de añadir alumno.");
            e.printStackTrace();
        }
    }

    @FXML
    void eliminarAlumno(ActionEvent event) {
        Alumno alumno = tvAlumnos.getSelectionModel().getSelectedItem();

        if (alumno == null) {
            Dialogos.mostrarDialogoAdvertencia("Advertencia", "Selecciona un alumno para eliminar.");
            return;
        }

        if (Dialogos.mostrarDialogoConfirmacion("Eliminar", "¿Seguro que quieres eliminar a " + alumno.getNombre() + "?")) {
            try {
                VistaGrafica.getInstancia().getControlador().borrar(alumno);
                coleccionAlumnos = new ArrayList<>(VistaGrafica.getInstancia().getControlador().getAlumnos());
                alumnosObservable.setAll(coleccionAlumnos);
            } catch (Exception e) {
                Dialogos.mostrarDialogoError("Error", "No se pudo eliminar el alumno.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void ordenarAlumnosAscendente(ActionEvent event) {
        List<Alumno> ordenados = coleccionAlumnos.stream()
                .sorted((a1, a2) -> a1.getNombre().compareToIgnoreCase(a2.getNombre()))
                .toList();
        alumnosObservable.setAll(ordenados);
    }

    @FXML
    public void ordenarAlumnosDescendente(ActionEvent event) {
        List<Alumno> ordenados = coleccionAlumnos.stream()
                .sorted((a1, a2) -> a2.getNombre().compareToIgnoreCase(a1.getNombre()))
                .toList();
        alumnosObservable.setAll(ordenados);
    }

    // CICLOS

    private void mostrarTablaCiclos() {
        try {
            tcCodigoCiclo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
            tcNombreCiclo.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            tcFamiliaCiclo.setCellValueFactory(new PropertyValueFactory<>("familiaProfesional"));
            tcHorasCiclo.setCellValueFactory(new PropertyValueFactory<>("horas"));

            tcGradoCiclo.setCellValueFactory(c ->
                    new SimpleStringProperty(c.getValue().getGrado().getNombre())
            );

            tcTipoGradoCiclo.setCellValueFactory(c -> {
                Grado grado = c.getValue().getGrado();
                String tipo = (grado instanceof GradoD) ? "D" : "E";
                return new SimpleStringProperty(tipo);
            });

            tcAnosCiclo.setCellValueFactory(c -> {
                Grado grado = c.getValue().getGrado();
                int anios = (grado instanceof GradoD gd) ? gd.getNumAnios() :
                        (grado instanceof GradoE ge) ? ge.getNumAnios() : -1;
                return new SimpleIntegerProperty(anios).asObject();
            });

            tcModalidadCiclo.setCellValueFactory(c -> {
                Grado grado = c.getValue().getGrado();
                return new SimpleStringProperty((grado instanceof GradoD gd) ? gd.getModalidad().toString() : "-");
            });

            tcEdicionesCiclo.setCellValueFactory(c -> {
                Grado grado = c.getValue().getGrado();
                return new SimpleStringProperty((grado instanceof GradoE ge) ? String.valueOf(ge.getNumEdiciones()) : "-");
            });

            tvCiclos.setItems(ciclosObservable);
            coleccionCiclos = new ArrayList<>(VistaGrafica.getInstancia().getControlador().getCiclosFormativos());
            ciclosObservable.setAll(coleccionCiclos);
            if (coleccionCiclos.isEmpty()) {
                tvCiclos.setPlaceholder(new Label("No hay ciclos disponibles."));
            }else {
                tvCiclos.setPlaceholder(null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filtrarCiclos(String filtro) {
        FilteredList<CicloFormativo> filtrados = new FilteredList<>(ciclosObservable, c -> true);
        filtrados.setPredicate(ciclo -> {
            if (filtro == null || filtro.isBlank()) return true;
            String texto = filtro.toLowerCase();

            return String.valueOf(ciclo.getCodigo()).contains(texto)
                    || ciclo.getNombre().toLowerCase().contains(texto)
                    || ciclo.getFamiliaProfesional().toLowerCase().contains(texto)
                    || String.valueOf(ciclo.getHoras()).startsWith(texto)
                    || ciclo.getGrado().toString().toLowerCase().startsWith(texto);
        });

        tvCiclos.setItems(filtrados);

        if (filtrados.isEmpty()) {
            tvCiclos.setPlaceholder(new Label("No hay ciclos disponibles."));
        }
    }

    private void mostrarMatriculaCiclo(CicloFormativo ciclo) {
        try {
            if (ciclo == null) {
                tvMatriculaCiclo.getItems().clear();
                tvMatriculaCiclo.setPlaceholder(new Label("Selecciona un ciclo."));
                return;
            }

            List<Matricula> asociadas = coleccionMatriculas.stream()
                    .filter(m -> m.getColeccionAsignaturas().stream()
                            .anyMatch(a -> a.getCicloFormativo().equals(ciclo)))
                    .toList();


            ObservableList<Matricula> matriculas = FXCollections.observableArrayList(asociadas);
            tvMatriculaCiclo.setItems(matriculas);
            if (matriculas.isEmpty()) {
                tvMatriculaCiclo.setPlaceholder(new Label("No hay matrículas asociadas."));            }

            tcCicloMatriculaAsC.setCellValueFactory(m ->
                    new SimpleStringProperty(
                            m.getValue().getColeccionAsignaturas().getFirst().getCicloFormativo().getNombre()
                    ));

            tcCodigoMatriculaAsC.setCellValueFactory(m ->
                    new SimpleIntegerProperty(
                            m.getValue().getColeccionAsignaturas().getFirst().getCicloFormativo().getCodigo()
                    ).asObject());

            tcIDMatriculaAsC.setCellValueFactory(m ->
                    new SimpleIntegerProperty(m.getValue().getIdMatricula()).asObject());

            tcMatriculacionMatriculaAsC.setCellValueFactory(m ->
                    new SimpleStringProperty(m.getValue().getFechaMatriculacion()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

            tcAnulacionMatriculaC.setCellValueFactory(m -> {
                LocalDate fecha = m.getValue().getFechaAnulacion();
                return new SimpleStringProperty((fecha != null) ?
                        fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "No anulada");
            });

            tcAsignaturasMatriculaAsC.setCellValueFactory(m -> {
                String asignaturas = m.getValue().getColeccionAsignaturas()
                        .stream()
                        .map(Asignatura::getNombre)
                        .reduce((a1, a2) -> a1 + ", " + a2)
                        .orElse("Sin asignaturas");
                return new SimpleStringProperty(asignaturas);
            });

        } catch (Exception e) {
            e.printStackTrace();
            Dialogos.mostrarDialogoError("Error", "No se pudieron cargar las matrículas del ciclo.");
            tvMatriculaCiclo.setPlaceholder(new Label("Error al cargar."));
        }
    }

    private void mostrarCicloSeleccionado(CicloFormativo ciclo) {
        System.out.println("Ciclo seleccionado: " + ciclo);
        mostrarMatriculaCiclo(ciclo);
    }

    private void mostrarMatriculaCicloSeleccionada(Matricula matricula) {
        System.out.println("Matrícula asociada al ciclo seleccionado: " + matricula);
    }


    @FXML
    void anadirCiclo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(LocalizadorRecursos.class.getResource("vistas/VentanaCiclo.fxml"));
            Parent root = loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Añadir Ciclo");
            ventana.setOnCloseRequest(e->confirmaCierreVentana(ventana,e));
            ventana.setScene(new Scene(root));
            ventana.initModality(Modality.APPLICATION_MODAL);
            ventana.setResizable(false);
            ventana.getIcons().add(new javafx.scene.image.Image(LocalizadorRecursos.class.getResourceAsStream("imagenes/logo_ies_al_andalus.png")));
            ventana.showAndWait();

            coleccionCiclos = new ArrayList<>(VistaGrafica.getInstancia().getControlador().getCiclosFormativos());
            ciclosObservable.setAll(coleccionCiclos);
        } catch (Exception e) {
            Dialogos.mostrarDialogoError("Error", "No se pudo abrir la ventana de ciclo.");
        }
    }

    @FXML
    void eliminarCiclo(ActionEvent event) {
        CicloFormativo ciclo = tvCiclos.getSelectionModel().getSelectedItem();
        if (ciclo == null) {
            Dialogos.mostrarDialogoAdvertencia("Advertencia", "Selecciona un ciclo para eliminar.");
            return;
        }

        if (Dialogos.mostrarDialogoConfirmacion("Eliminar", "¿Seguro que quieres eliminar el ciclo " + ciclo.getNombre() + "?")) {
            try {
                VistaGrafica.getInstancia().getControlador().borrar(ciclo);
                coleccionCiclos = new ArrayList<>(VistaGrafica.getInstancia().getControlador().getCiclosFormativos());
                ciclosObservable.setAll(coleccionCiclos);
            } catch (Exception e) {
                Dialogos.mostrarDialogoError("Error", "No se pudo eliminar el ciclo.");
            }
        }
    }

    @FXML
    public void ordenarCiclosAscendente(ActionEvent event) {
        List<CicloFormativo> ordenados = coleccionCiclos.stream()
                .sorted((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre()))
                .toList();
        ciclosObservable.setAll(ordenados);
    }

    @FXML
    public void ordenarCiclosDescendente(ActionEvent event) {
        List<CicloFormativo> ordenados = coleccionCiclos.stream()
                .sorted((c1, c2) -> c2.getNombre().compareToIgnoreCase(c1.getNombre()))
                .toList();
        ciclosObservable.setAll(ordenados);
    }

    // ASIGNATURAS

    private void mostrarTablaAsignaturas() {
        try {
            tcCodigoAsignatura.setCellValueFactory(new PropertyValueFactory<>("codigo"));
            tcNombreAsignatura.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            tcCicloAsignatura.setCellValueFactory(a ->
                    new SimpleStringProperty(a.getValue().getCicloFormativo().getNombre()));
            tcCursoAsignatura.setCellValueFactory(new PropertyValueFactory<>("curso"));
            tcEspecialidadAsignatura.setCellValueFactory(new PropertyValueFactory<>("especialidadProfesorado"));
            tcHorasAsignatura.setCellValueFactory(new PropertyValueFactory<>("horasAnuales"));
            tcHorasDesdobleAsignatura.setCellValueFactory(new PropertyValueFactory<>("horasDesdoble"));

            tvAsignaturas.setItems(asignaturasObservable);
            tvAsignaturas.setPlaceholder(new Label("No hay asignaturas disponibles."));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filtrarAsignaturas(String filtro) {
        FilteredList<Asignatura> filtradas = new FilteredList<>(asignaturasObservable, a -> true);
        filtradas.setPredicate(asignatura -> {
            if (filtro == null || filtro.isBlank()) return true;
            String texto = filtro.toLowerCase();
            return asignatura.getNombre().toLowerCase().contains(texto)
                    || asignatura.getCodigo().toLowerCase().startsWith(texto)
                    || asignatura.getCicloFormativo().getNombre().toLowerCase().contains(texto);
        });
        tvAsignaturas.setItems(filtradas);

        if (filtradas.isEmpty()) {
            tvAsignaturas.setPlaceholder(new Label("No hay asignaturas que coincidan."));
        }
    }

    private void mostrarAsignaturaSeleccionada(Asignatura asignatura) {
        System.out.println("Asignatura seleccionada: " + asignatura);
    }
    private void mostrarCicloAsignaturaSeleccionado(CicloFormativo ciclo) {
        System.out.println("Ciclo asociado a la asignatura seleccionada: " + ciclo);
    }

    @FXML
    void anadirAsignatura(ActionEvent event) {
        try {
            List<CicloFormativo> listaCiclos = VistaGrafica.getInstancia().getControlador().getCiclosFormativos();
            if (listaCiclos == null || listaCiclos.isEmpty()) {
                Dialogos.mostrarDialogoAdvertencia("Sin ciclos", "No hay ciclos formativos disponibles para asociar una asignatura.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(LocalizadorRecursos.class.getResource("vistas/VentanaAsignatura.fxml"));
            Parent root = loader.load();
            Stage ventana = new Stage();
            ventana.setTitle("Añadir Asignatura");
            ventana.setOnCloseRequest(e->confirmaCierreVentana(ventana,e));
            ventana.setScene(new Scene(root));
            ventana.initModality(Modality.APPLICATION_MODAL);
            ventana.setResizable(false);
            ventana.getIcons().add(new javafx.scene.image.Image(LocalizadorRecursos.class.getResourceAsStream("imagenes/logo_ies_al_andalus.png")));
            ventana.showAndWait();

            coleccionAsignaturas = new ArrayList<>(VistaGrafica.getInstancia().getControlador().getAsignaturas());
            asignaturasObservable.setAll(coleccionAsignaturas);
        } catch (Exception e) {
            Dialogos.mostrarDialogoError("Error", "No se pudo abrir la ventana de asignaturas.");
        }
    }

    @FXML
    void eliminarAsignatura(ActionEvent event) {
        Asignatura asignatura = tvAsignaturas.getSelectionModel().getSelectedItem();
        if (asignatura == null) {
            Dialogos.mostrarDialogoAdvertencia("Advertencia", "Selecciona una asignatura para eliminar.");
            return;
        }

        if (Dialogos.mostrarDialogoConfirmacion("Eliminar", "¿Seguro que quieres eliminar la asignatura " + asignatura.getNombre() + "?")) {
            try {
                VistaGrafica.getInstancia().getControlador().borrar(asignatura);
                coleccionAsignaturas = new ArrayList<>(VistaGrafica.getInstancia().getControlador().getAsignaturas());
                asignaturasObservable.setAll(coleccionAsignaturas);
            } catch (Exception e) {
                Dialogos.mostrarDialogoError("Error", "No se pudo eliminar la asignatura.");
            }
        }
    }

    private void mostrarCicloAsignatura(Asignatura asignatura) {
        if (asignatura == null) {
            tvCicloAsignatura.getItems().clear();
            tvCicloAsignatura.setPlaceholder(new Label("Selecciona una asignatura."));
            return;
        }

        CicloFormativo ciclo = asignatura.getCicloFormativo();
        ObservableList<CicloFormativo> datos = FXCollections.observableArrayList();
        datos.add(ciclo);
        tvCicloAsignatura.setItems(datos);

        tcNombreAsignaturaCA.setCellValueFactory(c -> new SimpleStringProperty(asignatura.getNombre()));
        tcCodigoAsignaturaCA.setCellValueFactory(c -> new SimpleStringProperty(asignatura.getCodigo()));
        tcNombreCicloCA.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        tcCodigoCicloCA.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getCodigo()).asObject());
        tcFamiliaCicloCA.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFamiliaProfesional()));
        tcHorasCicloCA.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getHoras()).asObject());

        tcTipoGradoCicloCA.setCellValueFactory(c -> {
            Grado grado = c.getValue().getGrado();
            return new SimpleStringProperty((grado instanceof GradoD) ? "D" : "E");
        });

        tcGradoCicloCA.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getGrado().getNombre()));

        tcAniosCicloCA.setCellValueFactory(c -> {
            Grado grado = c.getValue().getGrado();
            int anios = (grado instanceof GradoD gd) ? gd.getNumAnios()
                    : (grado instanceof GradoE ge) ? ge.getNumAnios() : -1;
            return new SimpleIntegerProperty(anios).asObject();
        });

        tcModalidadCicloCA.setCellValueFactory(c -> {
            Grado grado = c.getValue().getGrado();
            return new SimpleStringProperty((grado instanceof GradoD gd) ? gd.getModalidad().toString() : "-");
        });

        tcEdicionesCicloCA.setCellValueFactory(c -> {
            Grado grado = c.getValue().getGrado();
            return new SimpleStringProperty((grado instanceof GradoE ge) ? String.valueOf(ge.getNumEdiciones()) : "-");
        });
    }

    @FXML
    public void ordenarAsignaturasAscendente(ActionEvent event) {
        List<Asignatura> ordenadas = coleccionAsignaturas.stream()
                .sorted(Comparator.comparing(Asignatura::getNombre, String::compareToIgnoreCase))
                .toList();
        asignaturasObservable.setAll(ordenadas);
    }

    @FXML
    public void ordenarAsignaturasDescendente(ActionEvent event) {
        List<Asignatura> ordenadas = coleccionAsignaturas.stream()
                .sorted(Comparator.comparing(Asignatura::getNombre, String::compareToIgnoreCase).reversed())
                .toList();
        asignaturasObservable.setAll(ordenadas);
    }

    // MATRICULAS

    private void mostrarTablaMatriculas() {
        tcIDMatricula.setCellValueFactory(m -> new SimpleIntegerProperty(m.getValue().getIdMatricula()).asObject());
        tcAlumnoMatricula.setCellValueFactory(m -> new SimpleStringProperty(m.getValue().getAlumno().getNombre()));
        tcDNIMatricula.setCellValueFactory(m -> new SimpleStringProperty(m.getValue().getAlumno().getDni()));
        tcCursoMatricula.setCellValueFactory(m -> new SimpleStringProperty(m.getValue().getCursoAcademico()));
        tcMatriculacionMatricula.setCellValueFactory(m ->
                new SimpleStringProperty(
                        m.getValue().getFechaMatriculacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                )
        );

        tcAnulacionMatricula.setCellValueFactory(m -> {
            LocalDate fecha = m.getValue().getFechaAnulacion();
            return new SimpleStringProperty(
                    (fecha != null) ? fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "No anulada"
            );
        });

        tcAsignaturasMatricula.setCellValueFactory(m ->
                new SimpleStringProperty(obtenerNombresAsignaturas(m.getValue()))
        );

        tvMatriculas.setItems(matriculasObservable);
        tvMatriculas.setPlaceholder(new Label("No hay matrículas disponibles."));
    }

    private String obtenerNombresAsignaturas(Matricula matricula) {
        StringBuilder sb = new StringBuilder();
        for (Asignatura a : matricula.getColeccionAsignaturas()) {
            sb.append(a.getNombre()).append(", ");
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 2) : "Sin asignaturas";
    }

    private void mostrarDetallesMatricula(Matricula matricula) {
        tcCursoMatriculaAsM.setCellValueFactory(m -> new SimpleStringProperty(m.getValue().getCursoAcademico()));
        tcIDMatriculaAsM.setCellValueFactory(m -> new SimpleIntegerProperty(m.getValue().getIdMatricula()).asObject());
        tcAsignaturasMatriculaAsM.setCellValueFactory(m -> {
            String asignaturas = m.getValue().getColeccionAsignaturas().stream()
                    .map(Asignatura::getNombre)
                    .reduce((a, b) -> a + ", " + b).orElse("Sin asignaturas");
            return new SimpleStringProperty(asignaturas);
        });
        tcMatriculacionMatriculaAsM.setCellValueFactory(m ->
                new SimpleStringProperty(m.getValue().getFechaMatriculacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        );
        tcAnulacionMatriculaAsM.setCellValueFactory(m -> {
            LocalDate fecha = m.getValue().getFechaAnulacion();
            return new SimpleStringProperty((fecha != null) ? fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "No anulada");
        });

        tvMatriculaCurso.setItems(FXCollections.observableArrayList(matricula));
    }

    private void filtrarMatriculas(String filtro) {
        FilteredList<Matricula> filtradas = new FilteredList<>(matriculasObservable, m -> true);
        filtradas.setPredicate(m -> {
            if (filtro == null || filtro.isBlank()) return true;

            String texto = filtro.toLowerCase().trim();

            String asignaturas = m.getColeccionAsignaturas().stream()
                    .map(Asignatura::getNombre)
                    .reduce((a, b) -> a + " " + b)
                    .orElse("")
                    .toLowerCase();

            return m.getAlumno().getNombre().toLowerCase().contains(texto)
                    || m.getAlumno().getDni().toLowerCase().startsWith(texto)
                    || String.valueOf(m.getIdMatricula()).startsWith(texto)
                    || asignaturas.contains(texto);
        });

        tvMatriculas.setItems(filtradas);
    }

    @FXML
    void registrarMatricula(ActionEvent event) {
        try {
            List<Alumno> listaAlumnos = VistaGrafica.getInstancia().getControlador().getAlumnos();
            if (listaAlumnos == null || listaAlumnos.isEmpty()) {
                Dialogos.mostrarDialogoAdvertencia("Sin Alumnos", "No hay alumnos disponibles para asociar una matrícula.");
                return;
            }
            List<Asignatura> listaAsignaturas = VistaGrafica.getInstancia().getControlador().getAsignaturas();
            if (listaAsignaturas == null || listaAsignaturas.isEmpty()) {
                Dialogos.mostrarDialogoAdvertencia("Sin Asignaturas", "No hay asignaturas disponibles para asociar una matrícula.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(LocalizadorRecursos.class.getResource("vistas/VentanaMatricula.fxml"));
            Parent root = loader.load();
            Stage ventana = new Stage();
            ventana.setScene(new Scene(root));
            ventana.setTitle("Registrar Matrícula");
            ventana.setOnCloseRequest(e->confirmaCierreVentana(ventana,e));
            ventana.initModality(Modality.APPLICATION_MODAL);
            ventana.setResizable(false);
            ventana.getIcons().add(new javafx.scene.image.Image(LocalizadorRecursos.class.getResourceAsStream("imagenes/logo_ies_al_andalus.png")));
            ventana.showAndWait();

            coleccionMatriculas = VistaGrafica.getInstancia().getControlador().getMatriculas();
            matriculasObservable.setAll(coleccionMatriculas);
        } catch (Exception e) {
            Dialogos.mostrarDialogoError("Error", "No se pudo abrir la ventana de matrícula.");
        }
    }

    @FXML
    void anularMatricula(ActionEvent event) {
        Matricula matricula = tvMatriculas.getSelectionModel().getSelectedItem();

        if (matricula == null) {
            Dialogos.mostrarDialogoAdvertencia("Advertencia", "Selecciona una matrícula para anular.");
            return;
        }

        if (matricula.getFechaAnulacion() != null) {
            Dialogos.mostrarDialogoAdvertencia("Matrícula anulada",
                    "La matrícula ya tiene una fecha de anulación: "
                            + matricula.getFechaAnulacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            boolean eliminar = Dialogos.mostrarDialogoConfirmacion("Eliminar",
                    "¿Quieres eliminar completamente esta matrícula del sistema?");
            if (eliminar) {
                try {
                    VistaGrafica.getInstancia().getControlador().borrar(matricula);
                    actualizarListaMatriculas();
                    Dialogos.mostrarDialogoInformacion("Eliminada", "Matrícula eliminada correctamente.");
                } catch (Exception e) {
                    Dialogos.mostrarDialogoError("Error", "No se pudo eliminar la matrícula.");
                    e.printStackTrace();
                }
            }
            return;
        }
        LocalDate fechaAnulacion = Dialogos.mostrarDialogoFecha("Fecha de Anulación", "Selecciona una fecha de anulación:");
        if (fechaAnulacion == null) {
            return;
        }
        try {
            matricula.setFechaAnulacion(fechaAnulacion);
            VistaGrafica.getInstancia().getControlador().borrar(matricula);
            VistaGrafica.getInstancia().getControlador().insertar(matricula);

            boolean eliminar = Dialogos.mostrarDialogoConfirmacion("Eliminar",
                    "¿Quieres eliminar completamente esta matrícula del sistema?");
            if (eliminar) {
                VistaGrafica.getInstancia().getControlador().borrar(matricula);
            }

            actualizarListaMatriculas();
            Dialogos.mostrarDialogoInformacion("Anulada", "La matrícula ha sido actualizada correctamente.");

        } catch (Exception e) {
            Dialogos.mostrarDialogoError("Error", "No se pudo anular la matrícula.");
            e.printStackTrace();
        }
    }

    private void actualizarListaMatriculas() {
        try {
            coleccionMatriculas = VistaGrafica.getInstancia().getControlador().getMatriculas();
            matriculasObservable.setAll(coleccionMatriculas);
        } catch (Exception e) {
            Dialogos.mostrarDialogoError("Error", "No se pudieron recargar las matrículas.");
            e.printStackTrace();
        }
    }

    private void mostrarMatriculaCurso(Matricula matricula) {
        ObservableList<Matricula> datos = FXCollections.observableArrayList();
        datos.add(matricula);
        tvMatriculaCurso.setItems(datos);

        tcCursoMatriculaAsM.setCellValueFactory(m -> new SimpleStringProperty(m.getValue().getCursoAcademico()));
        tcIDMatriculaAsM.setCellValueFactory(m -> new SimpleIntegerProperty(m.getValue().getIdMatricula()).asObject());

        tcAsignaturasMatriculaAsM.setCellFactory(column -> new TableCell<>() {
            private final Text text = new Text();

            {
                text.wrappingWidthProperty().bind(tcAsignaturasMatriculaAsM.widthProperty().subtract(10));
                setGraphic(text);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    text.setText(null);
                } else {
                    text.setText(item);
                }
            }
        });

        tcAsignaturasMatriculaAsM.setCellValueFactory(m -> {
            String resultado = m.getValue().getColeccionAsignaturas().stream()
                    .map(a -> {
                        Grado grado = a.getCicloFormativo().getGrado();
                        String extraInfo;
                        if (grado instanceof GradoD gd) {
                            extraInfo = gd.getModalidad().toString();
                        } else if (grado instanceof GradoE ge) {
                            int num = ge.getNumEdiciones();
                            extraInfo = num + (num == 1 ? " edición" : " ediciones");
                        } else {
                            extraInfo = "N/A";
                        }
                        return a.getNombre() + " (" + extraInfo + ")";
                    })
                    .reduce((a, b) -> a + "\n" + b)
                    .orElse("Sin asignaturas");

            return new SimpleStringProperty(resultado);
        });

        tcMatriculacionMatriculaAsM.setCellValueFactory(m -> new SimpleStringProperty(
                m.getValue().getFechaMatriculacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        ));

        tcAnulacionMatriculaAsM.setCellValueFactory(m -> {
            LocalDate fecha = m.getValue().getFechaAnulacion();
            return new SimpleStringProperty((fecha != null) ? fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "No anulada");
        });
    }

    private void mostrarLaMatriculaSeleccionada(Matricula matricula) {
        System.out.println("Matrícula seleccionada: " + matricula);
    }
    private void mostrarMatriculaCursoSeleccionada(Matricula matricula) {
        System.out.println("Matrícula mostrada por curso académico: " + matricula);
    }

    @FXML
    public void ordenarMatriculasAscendente(ActionEvent event) {
        List<Matricula> ordenadas = coleccionMatriculas.stream()
                .sorted(Comparator.comparing((Matricula m) -> m.getAlumno().getNombre().toLowerCase())
                        .thenComparing(Matricula::getFechaMatriculacion))
                .toList();

        matriculasObservable.setAll(ordenadas);
    }

    @FXML
    public void ordenarMatriculasDescendente(ActionEvent event) {
        List<Matricula> ordenadas = coleccionMatriculas.stream()
                .sorted(Comparator.comparing((Matricula m) -> m.getAlumno().getNombre().toLowerCase())
                        .thenComparing(Matricula::getFechaMatriculacion)
                        .reversed())
                .toList();

        matriculasObservable.setAll(ordenadas);
    }

    private void confirmaCierreVentana(Stage ventanaCerrar, WindowEvent e)
    {
        if (Dialogos.mostrarDialogoConfirmacion("Ventana", "¿Deseas cerrar sin guardar?"))
        {
            ventanaCerrar.close();
        }
        else
            e.consume();
    }

}
