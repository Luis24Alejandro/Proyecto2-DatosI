import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AppCliente extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        // Creamos el texto de bienvenida
        Text bienvenidoText = new Text("Bienvenido");
        bienvenidoText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        
        // Creamos los botones
        Button boton1 = new Button("Botón 1");
        Button boton2 = new Button("Botón 2");
        
        // Creamos el layout y añadimos los nodos
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10));
        layout.setVgap(10);
        layout.setHgap(10);
        
        layout.add(bienvenidoText, 0, 0, 2, 1);
        layout.add(boton1, 0, 1);
        layout.add(boton2, 1, 1);
        
        // Creamos la escena y la añadimos al escenario
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("App Cliente");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
