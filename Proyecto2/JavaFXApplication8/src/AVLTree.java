
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class AVLTree {

    private Node root;

    private class Node {

        private Platillo data;
        private Node left;
        private Node right;
        private int height;
        private int size;

        public Node(Platillo data) {
            this.data = data;
            this.height = 1;
            this.size = 1;
        }
    }

    public AVLTree() {
        this.root = null;
        cargarDesdeJSON("src/datos/platillos.json");
    }
    public boolean contains(String nombre) {
    Node node = root;

    while (node != null) {
        int cmp = nombre.compareTo(node.data.getNombre());

        if (cmp < 0) {
            node = node.left;
        } else if (cmp > 0) {
            node = node.right;
        } else {
            System.out.println("Si lo contiene");
            return true;
            
        }
    }
System.out.println("No lo contiene");
    return false;
}

public void cargarDesdeJSON(String nombreArchivo) {
    try {
        // Leemos el archivo JSON y lo convertimos a una lista de objetos Platillo
        Gson gson = new Gson();
        FileReader fileReader = new FileReader(nombreArchivo);
        List<Platillo> listaPlatillos = gson.fromJson(fileReader, new TypeToken<List<Platillo>>(){}.getType());
        
        // Insertamos los objetos en el árbol AVL
        for (Platillo platillo : listaPlatillos) {
            insert(platillo);
        }
        
        fileReader.close();
    } catch (IOException e) {
        System.out.println("Error al leer el archivo JSON: " + e.getMessage());
    }
}

    // Método para guardar el árbol en un archivo JSON
    public void guardar(String filename) {
        List<Platillo> listaPlatillos = new ArrayList<>();
        crearLista(root, listaPlatillos);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(listaPlatillos);

        try (Writer writer = new FileWriter(filename)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método auxiliar para crear una lista de objetos Platillo a partir del árbol
    private void crearLista(Node node, List<Platillo> listaPlatillos) {
        if (node != null) {
            crearLista(node.left, listaPlatillos);
            listaPlatillos.add(node.data);
            crearLista(node.right, listaPlatillos);
        }
    }

    public void imprimir() {
        imprimir(root);
    }

    private void imprimir(Node node) {
        if (node != null) {
            imprimir(node.left);
            System.out.println(node.data.toString());
            imprimir(node.right);
        }
    }

    public void insert(Platillo data) {
        root = insert(root, data);
    }

    private Node insert(Node node, Platillo data) {
        if (node == null) {
            return new Node(data);
        }

        int cmp = data.compareTo(node.data);
        if (cmp < 0) {
            node.left = insert(node.left, data);
        } else if (cmp > 0) {
            node.right = insert(node.right, data);
        } else {
            // Duplicate key not allowed
            return node;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        node.size = 1 + size(node.left) + size(node.right);

        int balance = balance(node);

        if (balance > 1 && data.compareTo(node.left.data) < 0) {
            return rotateRight(node);
        }

        if (balance < -1 && data.compareTo(node.right.data) > 0) {
            return rotateLeft(node);
        }

        if (balance > 1 && data.compareTo(node.left.data) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1 && data.compareTo(node.right.data) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private Node rotateLeft(Node node) {
        Node x = node.right;
        Node T2 = x.left;

        x.left = node;
        node.right = T2;

        node.height = 1 + Math.max(height(node.left), height(node.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));

        node.size = 1 + size(node.left) + size(node.right);
        x.size = 1 + size(x.left) + size(x.right);

        return x;
    }

    private Node rotateRight(Node node) {
        Node x = node.left;
        Node T2 = x.right;

        x.right = node;
        node.left = T2;

        node.height = 1 + Math.max(height(node.left), height(node.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));

        node.size = 1 + size(node.left) + size(node.right);
        x.size = 1 + size(x.left) + size(x.right);

        return x;
    }

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private int size(Node node) {
        return node == null ? 0 : node.size;
    }

    private int balance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }
}
