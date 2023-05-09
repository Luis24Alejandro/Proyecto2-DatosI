// Imports que se utilizaron
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
//import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AppCliente extends Application {    
    // Iniciar variables
    private VBox contenedorBotones;
    private static Label labelUsuarioReg;
    private static Label labelPasswordReg;
    private static Label labelRespuesta;
    private VBox menu0;
    private VBox menu1;
    private static TextField campoUsuarioReg;
    private static PasswordField campoPasswordReg;
    private String HOST = "localhost";
    private int PUERTO = 8080;
    private ObservableList<AppCliente.Usuario> listaUsuarios;
    private String respuesta;
    private TextField campoUsuarioEditar;
    private TextField campoContraEditar;
    private Label labelUsuarioEdit;
    private Label labelPasswordEdit;
    private TextField campoNewUsuarioEditar;
    private Label labelNewUsuarioEdit;
    private Label labelNewPasswordEdit;
    private TextField campoNewContraEditar;
    private Usuario usuarioSeleccionado;
    TextField campoFiltro;
    private VBox menu2;
    private Label labelNombrePlatillo;
    private TextField campoNombrePlatillo;
    private Label labelCalorias;
    private TextField campoCalorias;
    private Label labelTiempoPrep;
    private TextField campoTiempoPrep;
    private Label labelPrecio;
    private TextField campoPrecio;
        
    /**
     * Clase que define un botón personalizado con las características deseadas.
     */
    public class BotonPersonalizado extends Button {    
        /**
        * Constructor que configura las propiedades del botón personalizado.
        * @param texto El texto que se mostrará en el botón.
        */
        public BotonPersonalizado(String texto) { 
            super(texto);
            //this.setPrefWidth(200);
            this.setAlignment(Pos.CENTER_LEFT);
            this.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
            this.setFont(Font.font("Roboto", 15));
            this.setCursor(Cursor.HAND);
            this.setOnMouseEntered(e -> this.setStyle("-fx-background-color: #f2f2f2; -fx-text-fill: black;"));
            this.setOnMouseExited(e -> this.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
        }
    }

    @Override

    public void start(Stage primaryStage) {    
        // Se establece una configuración que comparten la mayoría de los botones 
        String estiloBotones = "-fx-background-color: transparent; -fx-text-fill: white;";
        
        // Botón que muestra u oculta las opciones que el usuario tiene
        BotonPersonalizado botonMostrarOcultar = new BotonPersonalizado("☰");
        
        // Crear los botones de la barra de opciones 
        BotonPersonalizado boton1 = new BotonPersonalizado("Nuevo Pedido");
        BotonPersonalizado boton2 = new BotonPersonalizado("Historial de pedidos");
        BotonPersonalizado boton3 = new BotonPersonalizado("Agregar platillos");
        BotonPersonalizado boton4 = new BotonPersonalizado("Por definir...1");
        BotonPersonalizado boton5 = new BotonPersonalizado("Por definir...2");
        
        // Crear el panel que cambiará según el botón presionado
        StackPane panelDerecho = new StackPane();
        panelDerecho.setStyle("-fx-background-color: #9BA4B5;"); // Establecer un color de fondo
        
        // Crear un texto en el panel derecho inicial
        Text texto = new Text("Seleccione una opción de la barra lateral");
        texto.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        panelDerecho.getChildren().add(texto);
        
        // Crear el VBox del menú de botones
        contenedorBotones = new VBox(20); // Crea un VBox con 20pix de espacio vertical entre los botones
        contenedorBotones.setPadding(new Insets(10)); // Crea un margen a la derecha e izquierda del botón de 10pix
        contenedorBotones.setStyle("-fx-background-color: #19376D;"); // Establecer un color de fondo del VBox
        contenedorBotones.setAlignment(Pos.TOP_LEFT); // Establece el el inicio del VBox arriba a la izquierda
        
        // Crear un contenedor para el panel derecho
        BorderPane contenedorDerecho = new BorderPane(panelDerecho);
        contenedorDerecho.setPadding(new Insets(1));

        // Crear un contenedor para todo
        BorderPane contenedorPrincipal = new BorderPane();
        contenedorPrincipal.setLeft(contenedorBotones);
        contenedorPrincipal.setCenter(contenedorDerecho);
        panelDerecho.getChildren().add(botonMostrarOcultar);
        
        // Establece la acción del botón de ocultar la barra lateral
        botonMostrarOcultar.setOnAction(e -> {
            if (contenedorBotones.isVisible()) {
                // Si el contenedor de botones es visible, lo ocultamos y expandimos el contenedor derecho
                contenedorBotones.setVisible(false);
                contenedorDerecho.setPrefWidth(Double.MAX_VALUE);
                contenedorPrincipal.setLeft(null);
                contenedorDerecho.setAlignment(botonMostrarOcultar, Pos.CENTER_RIGHT); // Alineamos el botón a la derecha
                contenedorDerecho.getChildren().add(botonMostrarOcultar); // Añade el botón de ocultar la barra lateral
            } else {
                // Si el contenedor de botones es ocultado, lo mostramos y restauramos el tamaño del contenedor derecho
                contenedorBotones.setVisible(true);
                contenedorDerecho.setPrefWidth(Region.USE_COMPUTED_SIZE);
                contenedorPrincipal.setLeft(contenedorBotones);
                BorderPane.setAlignment(botonMostrarOcultar, Pos.CENTER_LEFT); // Alineamos el botón a la izquierda

                contenedorBotones.getChildren().add(0, botonMostrarOcultar);
            }
        });
      
        // Añade los botones al menú lateral
        contenedorBotones.getChildren().addAll(botonMostrarOcultar, boton1, boton2, boton3, boton4, boton5);
        
        // Crea los VBox que se abren cuando se oprime un botón
        menu0 = new VBox(10);
        menu1 = new VBox(10);
        menu2 = new VBox(10);
        
        // Label de la opción para realizar pedidos  
        Label labelNuevoPedido = new Label("Realizar un nuevo pedido");
        labelNuevoPedido.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
        labelNuevoPedido.setTextFill(Color.web("#19376D"));
        
        menu0.setPrefSize(100, 100);
        labelUsuarioReg = new Label("USERNAME");
        labelUsuarioReg.setFont(Font.font("Roboto", FontWeight.BOLD, 10));
        labelUsuarioReg.setTextFill(Color.web("#CCCCCC"));

        labelUsuarioReg.setLayoutX(50);
        labelUsuarioReg.setLayoutY(60);

        campoUsuarioReg = new TextField();
        campoUsuarioReg.setStyle("-fx-background-radius: 20; -fx-background-color: #5C5CA5;");
        campoUsuarioReg.setPrefWidth(300);
        campoUsuarioReg.setLayoutX(0);
        campoUsuarioReg.setLayoutY(0);

        labelPasswordReg = new Label("PASSWORD");
        labelPasswordReg.setFont(Font.font("Roboto", FontWeight.BOLD, 10));
        labelPasswordReg.setTextFill(Color.web("#CCCCCC"));
        labelPasswordReg.setLayoutX(50);
        labelPasswordReg.setLayoutY(100);

        campoPasswordReg = new PasswordField();
        campoPasswordReg.setStyle("-fx-background-radius: 20; -fx-background-color: #5C5CA5;");
        campoPasswordReg.setPrefWidth(300);
        campoPasswordReg.setLayoutX(0);
        campoPasswordReg.setLayoutY(0);

        MenuButton btnAgregarAdm = new MenuButton("Agregar", 140, 30, 5);

        Pane paneUsuario = new Pane(campoUsuarioReg);
        paneUsuario.setMaxWidth(300); // Establece el ancho máximo del pane
        paneUsuario.setMinWidth(50);
        Pane panePassword = new Pane(campoPasswordReg);
        panePassword.setMaxWidth(300); // Establece el ancho máximo del pane
        panePassword.setMinWidth(50);
        menu0.setAlignment(Pos.CENTER);
        menu1.setAlignment(Pos.CENTER);
        HBox btnAgregarcontenedor = new HBox();
        btnAgregarcontenedor.getChildren().add(btnAgregarAdm);
        btnAgregarcontenedor.setAlignment(Pos.CENTER);
        labelRespuesta = new Label(respuesta);
        menu0.getChildren().addAll(labelNuevoPedido, labelUsuarioReg, paneUsuario, labelPasswordReg, panePassword, btnAgregarcontenedor, labelRespuesta);
        
        btnAgregarAdm.setOnMouseClicked(event -> {
            if (campoPasswordReg.getText().isEmpty()) {
                campoPasswordReg.setPromptText("Contraseña vacia");
            }
            if (campoUsuarioReg.getText().isEmpty()) {
                campoUsuarioReg.setPromptText("Nombre de usuario vacio");

            }
            if (!campoPasswordReg.getText().isEmpty() && !campoUsuarioReg.getText().isEmpty()) {

                try {
                    enviarRegistro();
                    //gameMenu.setVisible(false);
                } catch (IOException ex) {
                    Logger.getLogger(VentanaConBotones.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(VentanaConBotones.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        boton1.setOnAction(e -> {
            panelDerecho.getChildren().setAll(menu0);
            boton1.setStyle("-fx-background-color: gray; -fx-text-fill: white;");// Cambia el color del botón cuando se hace click
            
        });

        boton2.setOnAction(e -> {
            panelDerecho.getChildren().setAll(menu1);
            boton2.setStyle("-fx-background-color: gray; -fx-text-fill: white;"); // Cambia el color del botón cuando se hace click   
        });
        
        labelUsuarioEdit = new Label("USERNAME");
        labelUsuarioEdit.setFont(Font.font("Roboto", FontWeight.BOLD, 10));
        labelUsuarioEdit.setTextFill(Color.web("#CCCCCC"));
        labelUsuarioEdit.setLayoutX(50);
        labelUsuarioEdit.setLayoutY(60);

        campoUsuarioEditar = new TextField();
        campoUsuarioEditar.setStyle("-fx-background-radius: 20; -fx-background-color: #5C5CA5;");
        campoUsuarioEditar.setPrefWidth(200);

        campoUsuarioEditar.setLayoutY(0);

        labelNewUsuarioEdit = new Label("NEW USERNAME");
        labelNewUsuarioEdit.setFont(Font.font("Roboto", FontWeight.BOLD, 10));
        labelNewUsuarioEdit.setTextFill(Color.web("#CCCCCC"));
        labelNewUsuarioEdit.setLayoutX(50);
        labelNewUsuarioEdit.setLayoutY(60);

        campoNewUsuarioEditar = new TextField();
        campoNewUsuarioEditar.setStyle("-fx-background-radius: 20; -fx-background-color: #5C5CA5;");
        campoNewUsuarioEditar.setPrefWidth(200);

        campoNewUsuarioEditar.setLayoutY(0);

        labelPasswordEdit = new Label("PASSWORD");
        labelPasswordEdit.setFont(Font.font("Roboto", FontWeight.BOLD, 10));
        labelPasswordEdit.setTextFill(Color.web("#CCCCCC"));
        labelPasswordEdit.setLayoutX(50);
        labelPasswordEdit.setLayoutY(100);

        campoContraEditar = new TextField();
        campoContraEditar.setStyle("-fx-background-radius: 20; -fx-background-color: #5C5CA5;");
        campoContraEditar.setPrefWidth(200);

        campoContraEditar.setLayoutY(0);

        labelNewPasswordEdit = new Label("NEW PASSWORD");
        labelNewPasswordEdit.setFont(Font.font("Roboto", FontWeight.BOLD, 10));
        labelNewPasswordEdit.setTextFill(Color.web("#CCCCCC"));
        labelNewPasswordEdit.setLayoutX(50);
        labelNewPasswordEdit.setLayoutY(100);

        campoNewContraEditar = new TextField();
        campoNewContraEditar.setStyle("-fx-background-radius: 20; -fx-background-color: #5C5CA5;");
        campoNewContraEditar.setPrefWidth(200);

        campoNewContraEditar.setLayoutY(0);

        Label labelELAdmin = new Label("Editar o Eliminar Administrador");
        labelELAdmin.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
        labelELAdmin.setTextFill(Color.web("#19376D"));

        leerUsuariosDesdeArchivo();

        ListView<AppCliente.Usuario> listView = new ListView<>(listaUsuarios);
        //listView.setPrefHeight(100);

        // Agrega el listener de selección al ListView
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Guarda el objeto seleccionado en una variable
            usuarioSeleccionado = newValue;
            if (usuarioSeleccionado != null) {
                campoUsuarioEditar.setText(usuarioSeleccionado.getNombreUsuario());
                campoUsuarioEditar.setDisable(true);
                campoContraEditar.setDisable(true);
                campoContraEditar.setText(usuarioSeleccionado.getContrasena());
                System.out.println(usuarioSeleccionado.getContrasena());
            } else if (usuarioSeleccionado == null) {
                campoUsuarioEditar.setText("");
                campoUsuarioEditar.setDisable(false);
                campoContraEditar.setDisable(false);
                campoContraEditar.setText("");
                System.out.println("");
            }
        });

        campoFiltro = new TextField();
        campoFiltro.setPromptText("Buscar usuario...");
        campoFiltro.setOnKeyReleased(e -> {
            String filtro = campoFiltro.getText().toLowerCase();
            Predicate<AppCliente.Usuario> predicado = usuario -> usuario.getNombreUsuario().toLowerCase().contains(filtro);
            FilteredList<AppCliente.Usuario> listaFiltrada = new FilteredList<>(listaUsuarios, predicado);
            listView.setItems(listaFiltrada);
        });

        MenuButton btnEliminarAdm = new MenuButton("Eliminar", 140, 30, 5);
        MenuButton btnEditarAdm = new MenuButton("Editar", 140, 30, 5);
        Button btnActualizarListview = new Button("⟲");
        menu1.getChildren().addAll(labelELAdmin, campoFiltro);
        HBox contenedorCampoEliminarEditar = new HBox();
        VBox contenedorListView = new VBox();
        contenedorListView.setTranslateX(-20);
        btnEliminarAdm.setTranslateY(15);
        btnEditarAdm.setTranslateY(15);
        VBox contenedorlisActualizar = new VBox();
        contenedorlisActualizar.getChildren().addAll(btnActualizarListview, listView);
        VBox contenerdorContraUser = new VBox();
        contenerdorContraUser.setTranslateX(20);
        contenerdorContraUser.getChildren().addAll(labelUsuarioEdit, campoUsuarioEditar, labelPasswordEdit, campoContraEditar, labelNewUsuarioEdit, campoNewUsuarioEditar, labelNewPasswordEdit, campoNewContraEditar, btnEditarAdm);
        contenerdorContraUser.setAlignment(Pos.CENTER);
        contenedorListView.setPrefSize(300, 200);
        contenedorListView.getChildren().addAll(contenedorlisActualizar, btnEliminarAdm);
        contenedorCampoEliminarEditar.getChildren().addAll(contenedorListView, contenerdorContraUser);
        contenedorCampoEliminarEditar.setAlignment(Pos.CENTER);
        menu1.getChildren().addAll(contenedorCampoEliminarEditar, labelRespuesta);

        Path path = (Path) Paths.get("src/datos/administradores.xml");
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            path.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            Thread watchThread = new Thread(() -> {
                while (true) {
                    WatchKey key;
                    try {
                        key = watchService.take();
                    } catch (InterruptedException e) {
                        return;
                    }
                    for (WatchEvent<?> event : key.pollEvents()) {
                        Path changed = (Path) event.context();
                        if (changed.endsWith("administradores.xml")) {
                            Platform.runLater(() -> leerUsuariosDesdeArchivo());
                        }
                    }
                    key.reset();
                }
            });
            watchThread.setDaemon(true);
            watchThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnEliminarAdm.setOnMouseClicked(event -> {
            KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_RELEASED, "a", "a", KeyCode.A, false, false, false, false);
            campoFiltro.fireEvent(keyEvent);
        });
        btnEliminarAdm.setOnMouseClicked(event -> {
            if (campoContraEditar.getText().isEmpty()) {
                campoContraEditar.setPromptText("Contraseña vacia");
            }
            if (campoUsuarioEditar.getText().isEmpty()) {
                campoUsuarioEditar.setPromptText("Nombre de usuario vacio");
            }
            if (!campoContraEditar.getText().isEmpty() && !campoUsuarioEditar.getText().isEmpty()) {
                try {
                    enviarEliminarAdm();
                    campoFiltro.setText("a");
                    campoFiltro.clear();
                    //gameMenu.setVisible(false);
                } catch (IOException ex) {
                    Logger.getLogger(VentanaConBotones.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(VentanaConBotones.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        btnEditarAdm.setOnMouseClicked(event -> {

            if (campoNewContraEditar.getText().isEmpty()) {

                campoNewContraEditar.setPromptText("Contraseña vacia");
            }
            if (campoNewUsuarioEditar.getText().isEmpty()) {
                campoNewUsuarioEditar.setText(usuarioSeleccionado.getNombreUsuario());
                campoNewUsuarioEditar.setPromptText("Nombre de usuario vacio");
            }

            if (!campoContraEditar.getText().isEmpty() && !campoUsuarioEditar.getText().isEmpty() && !campoNewContraEditar.getText().isEmpty() && !campoNewUsuarioEditar.getText().isEmpty()) {
                try {
                    enviarEditarAdm();
                    //gameMenu.setVisible(false);
                } catch (IOException ex) {
                    Logger.getLogger(VentanaConBotones.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(VentanaConBotones.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        boton3.setOnAction(e
                -> {
            panelDerecho.getChildren().setAll(menu2);
            boton1.setStyle(estiloBotones);
            boton2.setStyle(estiloBotones);
            boton3.setStyle("-fx-background-color: gray; -fx-text-fill: white;");
            boton4.setStyle(estiloBotones);
            boton5.setStyle(estiloBotones);
        }
        );
        Label labelAgregarPlatillo = new Label("Agregar Platillo");
        labelAgregarPlatillo.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
        labelAgregarPlatillo.setTextFill(Color.web("#19376D"));

// Definir el filtro de texto
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?\\d*")) { // Solo permitir caracteres numéricos y el signo de menos
                return change;
            }
            return null;
        };

// Crear el formateador de texto y asignarlo al TextField
        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), null, integerFilter);
        TextFormatter<Integer> textFormatter2 = new TextFormatter<>(new IntegerStringConverter(), null, integerFilter);
        TextFormatter<Integer> textFormatter3 = new TextFormatter<>(new IntegerStringConverter(), null, integerFilter);
        
        labelNombrePlatillo = new Label("Nombre del platillo");
        labelNombrePlatillo.setFont(Font.font("Roboto", FontWeight.BOLD, 10));
        labelNombrePlatillo.setTextFill(Color.web("#CCCCCC"));

        labelNombrePlatillo.setLayoutX(50);
        labelNombrePlatillo.setLayoutY(60);

        campoNombrePlatillo = new TextField();
        campoNombrePlatillo.setStyle("-fx-background-radius: 20; -fx-background-color: #5C5CA5;");
        campoNombrePlatillo.setPrefWidth(300);
        campoNombrePlatillo.setLayoutX(0);
        campoNombrePlatillo.setLayoutY(0);

        labelCalorias = new Label("Calorias");
        labelCalorias.setFont(Font.font("Roboto", FontWeight.BOLD, 10));
        labelCalorias.setTextFill(Color.web("#CCCCCC"));
        labelCalorias.setLayoutX(50);
        labelCalorias.setLayoutY(100);

        campoCalorias = new TextField();
        campoCalorias.setStyle("-fx-background-radius: 20; -fx-background-color: #5C5CA5;");
        campoCalorias.setPrefWidth(300);
        campoCalorias.setLayoutX(0);
        campoCalorias.setLayoutY(0);
        campoCalorias.setTextFormatter(textFormatter);
        labelTiempoPrep = new Label("Tiempo de preparacion");
        labelTiempoPrep.setFont(Font.font("Roboto", FontWeight.BOLD, 10));
        labelTiempoPrep.setTextFill(Color.web("#CCCCCC"));
        labelTiempoPrep.setLayoutX(50);
        labelTiempoPrep.setLayoutY(100);

        campoTiempoPrep = new TextField();
        campoTiempoPrep.setStyle("-fx-background-radius: 20; -fx-background-color: #5C5CA5;");
        campoTiempoPrep.setPrefWidth(300);
        campoTiempoPrep.setLayoutX(0);
        campoTiempoPrep.setLayoutY(0);
        campoTiempoPrep.setTextFormatter(textFormatter2);
        labelPrecio = new Label("Precio");
        labelPrecio.setFont(Font.font("Roboto", FontWeight.BOLD, 10));
        labelPrecio.setTextFill(Color.web("#CCCCCC"));
        labelPrecio.setLayoutX(50);
        labelPrecio.setLayoutY(100);

        campoPrecio = new TextField();
        campoPrecio.setStyle("-fx-background-radius: 20; -fx-background-color: #5C5CA5;");
        campoPrecio.setPrefWidth(300);
        campoPrecio.setLayoutX(0);
        campoPrecio.setLayoutY(0);
        campoPrecio.setTextFormatter(textFormatter3);
        MenuButton btnAgregarPlatillo = new MenuButton("Agregar", 140, 30, 5);

        Pane paneNombrePlatillo = new Pane(campoNombrePlatillo);
        paneNombrePlatillo.setMaxWidth(300); // Establece el ancho máximo del pane
        paneNombrePlatillo.setMinWidth(50);
        Pane paneCalorias = new Pane(campoCalorias);
        paneCalorias.setMaxWidth(300); // Establece el ancho máximo del pane
        paneCalorias.setMinWidth(50);
        Pane paneTiempoPrep = new Pane(campoTiempoPrep);
        paneTiempoPrep.setMaxWidth(300); // Establece el ancho máximo del pane
        paneTiempoPrep.setMinWidth(50);
        Pane panePrecio = new Pane(campoPrecio);
        panePrecio.setMaxWidth(300); // Establece el ancho máximo del pane
        panePrecio.setMinWidth(50);
        menu2.setAlignment(Pos.CENTER);

        HBox btnAgregarPlatcontenedor = new HBox();
        btnAgregarPlatcontenedor.getChildren().add(btnAgregarPlatillo);
        btnAgregarPlatcontenedor.setAlignment(Pos.CENTER);
        labelRespuesta = new Label(respuesta);

        menu2.getChildren().addAll(labelAgregarPlatillo, labelNombrePlatillo, paneNombrePlatillo, labelCalorias, paneCalorias, labelTiempoPrep, paneTiempoPrep, labelPrecio, panePrecio, btnAgregarPlatcontenedor, labelRespuesta);
        btnAgregarPlatillo.setOnMouseClicked(event -> {
            if (campoNombrePlatillo.getText().isEmpty()) {
                campoNombrePlatillo.setPromptText("Ingresa un nombre de usuario");
            }
            if (campoCalorias.getText().isEmpty()) {
                campoCalorias.setPromptText("Ingresa las calorias");
            }
            if (campoTiempoPrep.getText().isEmpty()) {
                campoTiempoPrep.setPromptText("Ingresa el tiempo de preparacion");
            }
            if (campoPrecio.getText().isEmpty()) {
                campoPrecio.setPromptText("Ingresa un precio");
            }
            if (!campoNombrePlatillo.getText().isEmpty() && !campoCalorias.getText().isEmpty()&&!campoTiempoPrep.getText().isEmpty()&&!campoPrecio.getText().isEmpty()) {
                try {
                    enviarAgregarPlatillo();

                    //gameMenu.setVisible(false);
                } catch (IOException ex) {
                    Logger.getLogger(VentanaConBotones.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(VentanaConBotones.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        boton4.setOnAction(e
                -> {
            panelDerecho.getChildren().setAll(new Text("Botón 4 presionado"));
            boton1.setStyle(estiloBotones);
            boton2.setStyle(estiloBotones);
            boton3.setStyle(estiloBotones);
            boton4.setStyle("-fx-background-color: gray; -fx-text-fill: white;");
            boton5.setStyle(estiloBotones);
        }
        );
        boton5.setOnAction(e
                -> {
            panelDerecho.getChildren().setAll(new Text("Botón 4 presionado"));
            boton1.setStyle(estiloBotones);
            boton2.setStyle(estiloBotones);
            boton3.setStyle(estiloBotones);
            boton4.setStyle(estiloBotones);

            boton5.setStyle("-fx-background-color: gray; -fx-text-fill: white;");
        }
        );
        // Crear la escena
        Scene escena = new Scene(contenedorPrincipal, 800, 600);

        // Configurar la ventana
        primaryStage.setScene(escena);

        primaryStage.setTitle(
                "Ventana con botones");
        primaryStage.show();
    }

    private void leerUsuariosDesdeArchivo() {
        List<Usuario> usuarios = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/datos/administradores.xml"));

            NodeList nodeList = document.getElementsByTagName("administrador");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String nombreUsuario = element.getAttribute("nombreUsuario");
                    String contrasena = element.getAttribute("contrasena");
                    Usuario usuario = new Usuario(nombreUsuario, contrasena);
                    usuarios.add(usuario);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaUsuarios = FXCollections.observableArrayList(usuarios);
    }

    private static class Usuario {

        private String nombreUsuario;
        private String contrasena;

        public Usuario(String nombreUsuario, String contrasena) {
            this.nombreUsuario = nombreUsuario;
            this.contrasena = contrasena;
        }

        public String getNombreUsuario() {
            return nombreUsuario;
        }

        public String getContrasena() {
            return contrasena;
        }

        @Override
        public String toString() {
            return nombreUsuario;
        }
    }

    private void enviarRegistro() throws IOException, ClassNotFoundException {
        // Obtener credenciales del usuario

        String usuario = campoUsuarioReg.getText();
        String password = campoPasswordReg.getText();

        // Enviar credenciales al servidor (código aquí)
        // Conectar con el servidor
        Socket servidorSocket = new Socket(HOST, PUERTO);

        // Crear flujos de entrada/salida para comunicación con el servidor
        ObjectOutputStream salida = new ObjectOutputStream(servidorSocket.getOutputStream());
        ObjectInputStream entrada = new ObjectInputStream(servidorSocket.getInputStream());
        // Obtener credenciales del usuario

        // Crear objeto Mensaje con la acción "register" y los datos de usuario y contraseña
        //Credenciales credenciales = new Credenciales(usuario, password);
        Mensaje mensaje = new Mensaje("registerAdm", usuario, password, "", "");

        // Enviar objeto Mensaje al servidor
        salida.writeObject(mensaje);

        // Leer respuesta del servidor
        respuesta = (String) entrada.readObject();
        if (!menu0.getChildren().contains(labelRespuesta)) {
            labelRespuesta.setText(respuesta);
            labelRespuesta.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
            labelRespuesta.setTextFill(Color.web("#19376D"));
            menu0.getChildren().add(labelRespuesta);
        }
        KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_RELEASED, "a", "a", KeyCode.A, false, false, false, false);
        campoFiltro.fireEvent(keyEvent);
        labelRespuesta.setText(respuesta);
        labelRespuesta.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
        labelRespuesta.setTextFill(Color.web("#19376D"));
    }

    private void enviarEliminarAdm() throws IOException, ClassNotFoundException {
        // Obtener credenciales del usuario

        String usuario = campoUsuarioEditar.getText();
        String password = campoContraEditar.getText();

        // Enviar credenciales al servidor (código aquí)
        // Conectar con el servidor
        Socket servidorSocket = new Socket(HOST, PUERTO);

        // Crear flujos de entrada/salida para comunicación con el servidor
        ObjectOutputStream salida = new ObjectOutputStream(servidorSocket.getOutputStream());
        ObjectInputStream entrada = new ObjectInputStream(servidorSocket.getInputStream());
        // Obtener credenciales del usuario

        // Crear objeto Mensaje con la acción "register" y los datos de usuario y contraseña
        //Credenciales credenciales = new Credenciales(usuario, password);
        Mensaje mensaje = new Mensaje("deleteAdm", usuario, password, "", "");

        // Enviar objeto Mensaje al servidor
        salida.writeObject(mensaje);

        // Leer respuesta del servidor
        respuesta = (String) entrada.readObject();
        if (!menu1.getChildren().contains(labelRespuesta)) {
            labelRespuesta.setText(respuesta);
            labelRespuesta.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
            labelRespuesta.setTextFill(Color.web("#19376D"));
            menu1.getChildren().add(labelRespuesta);
        }
        KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_RELEASED, "a", "a", KeyCode.A, false, false, false, false);
        campoFiltro.fireEvent(keyEvent);
        labelRespuesta.setText(respuesta);
        labelRespuesta.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
        labelRespuesta.setTextFill(Color.web("#19376D"));
    }

    private void enviarEditarAdm() throws IOException, ClassNotFoundException {
        // Obtener credenciales del usuario
        String usuario = campoUsuarioEditar.getText();
        String usuarioNew = campoNewUsuarioEditar.getText();
        String passwordNew = campoNewContraEditar.getText();
        String password = campoContraEditar.getText();

        // Enviar credenciales al servidor (código aquí)
        // Conectar con el servidor
        Socket servidorSocket = new Socket(HOST, PUERTO);

        // Crear flujos de entrada/salida para comunicación con el servidor
        ObjectOutputStream salida = new ObjectOutputStream(servidorSocket.getOutputStream());
        ObjectInputStream entrada = new ObjectInputStream(servidorSocket.getInputStream());
        // Obtener credenciales del usuario

        // Crear objeto Mensaje con la acción "register" y los datos de usuario y contraseña
        //Credenciales credenciales = new Credenciales(usuario, password);
        Mensaje mensaje = new Mensaje("editAdm", usuario, password, usuarioNew, passwordNew);
        System.out.println(passwordNew);

        // Enviar objeto Mensaje al servidor
        salida.writeObject(mensaje);

        // Leer respuesta del servidor
        respuesta = (String) entrada.readObject();
        if (!menu1.getChildren().contains(labelRespuesta)) {
            labelRespuesta.setText(respuesta);
            labelRespuesta.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
            labelRespuesta.setTextFill(Color.web("#19376D"));
            menu1.getChildren().add(labelRespuesta);
        }
        KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_RELEASED, "a", "a", KeyCode.A, false, false, false, false);
        campoFiltro.fireEvent(keyEvent);
        KeyEvent keyEvent2 = new KeyEvent(KeyEvent.KEY_RELEASED, "a", "a", KeyCode.A, false, false, false, false);
        campoFiltro.fireEvent(keyEvent2);
        labelRespuesta.setText(respuesta);
        labelRespuesta.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
        labelRespuesta.setTextFill(Color.web("#19376D"));
    }

    private void enviarAgregarPlatillo() throws IOException, ClassNotFoundException {
        // Obtener credenciales del usuario
        String NombrePlatillo = campoNombrePlatillo.getText();
        String calorias = campoCalorias.getText();
        String tiempoPrep = campoTiempoPrep.getText();
        String precio = campoPrecio.getText();

        // Enviar credenciales al servidor (código aquí)
        // Conectar con el servidor
        Socket servidorSocket = new Socket(HOST, PUERTO);

        // Crear flujos de entrada/salida para comunicación con el servidor
        ObjectOutputStream salida = new ObjectOutputStream(servidorSocket.getOutputStream());
        ObjectInputStream entrada = new ObjectInputStream(servidorSocket.getInputStream());
        // Obtener credenciales del usuario

        // Crear objeto Mensaje con la acción "register" y los datos de usuario y contraseña
        //Credenciales credenciales = new Credenciales(usuario, password);
        Mensaje mensaje = new Mensaje("agregarPlatillo", NombrePlatillo, calorias, tiempoPrep, precio);

        // Enviar objeto Mensaje al servidor
        salida.writeObject(mensaje);

        // Leer respuesta del servidor
        respuesta = (String) entrada.readObject();
        if (!menu2.getChildren().contains(labelRespuesta)) {
            labelRespuesta.setText(respuesta);
            labelRespuesta.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
            labelRespuesta.setTextFill(Color.web("#19376D"));
            menu2.getChildren().add(labelRespuesta);
        }
        labelRespuesta.setText(respuesta);
        labelRespuesta.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
        labelRespuesta.setTextFill(Color.web("#19376D"));
    }
    
    private static class MenuButton extends StackPane {
        private Text text;
        /**
         *
         * Crea una nueva instancia de la clase MenuButton con un nombre, una
         * posición x y y y una traslación dada.
         *
         * @param name el nombre del botón
         *
         * @param x la posición x del botón
         *
         * @param y la posición y del botón
         *
         * @param translate la traslación del botón
         */
        public MenuButton(String name, int x, int y, int translate) {
            Font pixelFont = Font.font("Roboto", FontWeight.BOLD, 16);
            //Font pixelFont = Font.loadFont("file:/C:/Users/ulise/Desktop/TEC/Algoritmos y estructura de datos I/BuscaMinas/ventana busca minas/src/fonts/digital-7.ttf", 20);
            text = new Text(name);
            text.setFont(pixelFont);
            text.setFill(Color.WHITE);

            Rectangle bg = new Rectangle(x, y);
            bg.setArcWidth(10);
            bg.setArcHeight(10);
            bg.setOpacity(0.6);
            Color color = Color.web("#19376D");
            bg.setFill(color);

            bg.setEffect(new GaussianBlur(2));

            setAlignment(Pos.CENTER);
            setRotate(-0.5);
            getChildren().addAll(bg, text);

            setOnMouseEntered(event -> {
                bg.setTranslateX(translate);
                text.setTranslateX(translate);
                bg.setFill(Color.GREY);
                text.setFill(Color.WHITE);
            });
            setOnMouseExited(event -> {
                bg.setTranslateX(0);
                text.setTranslateX(0);
                bg.setFill(color);
                text.setFill(Color.WHITE);
            });
            DropShadow drop = new DropShadow(50, Color.GREY);
            drop.setInput(new Glow());

            setOnMousePressed(event -> setEffect(drop));
            setOnMouseReleased(event -> setEffect(null));
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
