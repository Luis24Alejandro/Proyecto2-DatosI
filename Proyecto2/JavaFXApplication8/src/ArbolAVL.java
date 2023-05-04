
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public class ArbolAVL<T extends Comparable<T>> {

    private NodoAVL<T> raiz;

    // Constructor
    public ArbolAVL() {
        raiz = null;
        fromJson("src/datos/platillos.json");
    }

    // Método para guardar el árbol AVL en un archivo JSON
    public void toJson(String archivo) {
        try {
            // Crear el objeto Gson para convertir el árbol a JSON
            Gson gson = new GsonBuilder().create();

            // Convertir el árbol a JSON
            String json = gson.toJson(raiz);

            // Escribir el JSON en el archivo
            FileWriter writer = new FileWriter(archivo);
            writer.write(json);
            writer.close();

            System.out.println("El árbol AVL ha sido guardado en el archivo " + archivo);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para cargar el árbol AVL desde un archivo JSON
    public void fromJson(String archivo) {
        try {
            // Leer el archivo
            FileReader reader = new FileReader(archivo);

            // Crear el objeto Gson para convertir el JSON a árbol AVL
            Gson gson = new GsonBuilder().create();

            // Convertir el JSON a un objeto de tipo NodoAVL
            Type tipo = new TypeToken<NodoAVL<T>>() {
            }.getType();
            NodoAVL<T> nodo = gson.fromJson(reader, tipo);

            // Asignar el nodo raíz al árbol AVL
            raiz = nodo;

            System.out.println("El árbol AVL ha sido cargado desde el archivo " + archivo);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Insertar un elemento en el árbol AVL
    public void insertar(T dato) {
        raiz = insertar(dato, raiz);
        toJson("src/datos/platillos.json");
    }

    private NodoAVL<T> insertar(T dato, NodoAVL<T> nodo) {
        if (nodo == null) {
            return new NodoAVL<T>(dato);
        }

        if (dato instanceof String && nodo.getDato() instanceof String) {
  if (((String) dato).compareTo((String) nodo.getDato()) < 0) {
            nodo.setIzquierdo(insertar(dato, nodo.getIzquierdo()));
        }} else {
            nodo.setDerecho(insertar(dato, nodo.getDerecho()));
        }

        // Rebalancear el árbol
        nodo = balancear(nodo);

        return nodo;
    }

    // Rebalancear el árbol AVL
    private NodoAVL<T> balancear(NodoAVL<T> nodo) {
        int balance = altura(nodo.getIzquierdo()) - altura(nodo.getDerecho());

        // Si el nodo está desbalanceado hacia la izquierda
        if (balance > 1) {
            // Si el subárbol izquierdo está desbalanceado hacia la izquierda
            if (altura(nodo.getIzquierdo().getIzquierdo()) >= altura(nodo.getIzquierdo().getDerecho())) {
                nodo = rotacionDerecha(nodo);
            } else { // Si el subárbol izquierdo está desbalanceado hacia la derecha
                nodo.setIzquierdo(rotacionIzquierda(nodo.getIzquierdo()));
                nodo = rotacionDerecha(nodo);
            }
        } // Si el nodo está desbalanceado hacia la derecha
        else if (balance < -1) {
            // Si el subárbol derecho está desbalanceado hacia la derecha
            if (altura(nodo.getDerecho().getDerecho()) >= altura(nodo.getDerecho().getIzquierdo())) {
                nodo = rotacionIzquierda(nodo);
            } else { // Si el subárbol derecho está desbalanceado hacia la izquierda
                nodo.setDerecho(rotacionDerecha(nodo.getDerecho()));
                nodo = rotacionIzquierda(nodo);
            }
        }

        return nodo;
    }

// Rotación simple a la derecha
    private NodoAVL<T> rotacionDerecha(NodoAVL<T> nodo) {
        NodoAVL<T> auxiliar = nodo.getIzquierdo();
        nodo.setIzquierdo(auxiliar.getDerecho());
        auxiliar.setDerecho(nodo);
        return auxiliar;
    }

// Rotación simple a la izquierda
    private NodoAVL<T> rotacionIzquierda(NodoAVL<T> nodo) {
        NodoAVL<T> auxiliar = nodo.getDerecho();
        nodo.setDerecho(auxiliar.getIzquierdo());
        auxiliar.setIzquierdo(nodo);
        return auxiliar;
    }

// Altura de un nodo
    private int altura(NodoAVL<T> nodo) {
        if (nodo == null) {
            return 0;
        } else {
            return 1 + Math.max(altura(nodo.getIzquierdo()), altura(nodo.getDerecho()));
        }
    }

// Recorrido en inorden del árbol AVL
    public void inorden() {
        inorden(raiz);
    }

    private void inorden(NodoAVL<T> nodo) {
        if (nodo != null) {
            inorden(nodo.getIzquierdo());
            System.out.println(nodo.getDato());
            inorden(nodo.getDerecho());
        }
    }

    public class NodoAVL<T> {

        private T dato;
        private NodoAVL<T> izquierdo;
        private NodoAVL<T> derecho;
// Constructor

        public NodoAVL(T dato) {
            this.dato = dato;
            izquierdo = null;
            derecho = null;
        }

// Getters y setters
        public T getDato() {
            return dato;
        }

        public void setDato(T dato) {
            this.dato = dato;
        }

        public NodoAVL<T> getIzquierdo() {
            return izquierdo;
        }

        public void setIzquierdo(NodoAVL<T> izquierdo) {
            this.izquierdo = izquierdo;
        }

        public NodoAVL<T> getDerecho() {
            return derecho;
        }

        public void setDerecho(NodoAVL<T> derecho) {
            this.derecho = derecho;
        }
    }
}
