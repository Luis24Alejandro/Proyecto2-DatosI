
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ListaUsuariosApp extends Application {

    private ObservableList<Usuario> listaUsuarios;

    @Override
    public void start(Stage primaryStage) {
        leerUsuariosDesdeArchivo();

        ListView<Usuario> listView = new ListView<>(listaUsuarios);
        TextField campoFiltro = new TextField();
        campoFiltro.setPromptText("Buscar usuario...");
        campoFiltro.setOnKeyReleased(e -> {
            String filtro = campoFiltro.getText().toLowerCase();
            Predicate<Usuario> predicado = usuario -> usuario.getNombreUsuario().toLowerCase().contains(filtro);
            FilteredList<Usuario> listaFiltrada = new FilteredList<>(listaUsuarios, predicado);
            listView.setItems(listaFiltrada);
        });

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setTop(campoFiltro);
        root.setCenter(listView);

        Scene scene = new Scene(root, 300, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Lista de Usuarios");
        primaryStage.show();
    }

    private void leerUsuariosDesdeArchivo() {
        List<Usuario> usuarios = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("src/datos/usuarios.xml"));

            NodeList nodeList = document.getElementsByTagName("usuario");
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

    public static void main(String[] args) {
        launch(args);
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
}
