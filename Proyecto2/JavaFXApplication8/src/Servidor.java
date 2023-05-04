
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.stream.XMLStreamException;

public class Servidor {

    private static final int PUERTO = 8080;

    public static void main(String[] args) throws XMLStreamException, FileNotFoundException, IOException, ClassNotFoundException {
        // Crear un árbol binario de búsqueda para usuarios
        BinarySearchTreeUsr usuarios = new BinarySearchTreeUsr();
        BinarySearchTreeAdm administradores = new BinarySearchTreeAdm();
        // ArbolAVL<Platillo> platillos = new ArbolAVL<>();
        AVLTree arbol = new AVLTree();
        Platillo p1 = new Platillo("Hamburguesas", 500, 30, 100.0);
        Platillo p2 = new Platillo("Tacos", 400, 20, 80.0);
        Platillo p3 = new Platillo("Quesadillas", 600, 25, 90.0);
        Platillo p4 = new Platillo("Pollo", 550, 35, 110.0);
        Platillo p6 = new Platillo("carne", 550, 35, 110.0);
//
//        arbol.insert(p6);
//
//        arbol.insert(p2);
//        arbol.guardar("src/datos/platillos.json");
        arbol.imprimir();

        // Agregar usuarios válidos
        usuarios.insert(new Usuario("usuario8", "password8"));
        administradores.insert(new Usuario("usuario8", "password8"));
        usuarios.edit("usuario8", "usuario8", "uli");
        administradores.edit("usuario8", "usuario8", "uli");

        //administradores.insert(new Usuario("admin", "123"));
        // usuarios.guardarUsuariosEnXML("usuas.xml");
        //usuarios.guardarUsuariosEnXML("file:C:/Users/ulise/Desktop/TEC/Algoritmos y estructura de datos I/Proyecto2/JavaFXApplication8/src/usuarios.xml");
        usuarios.toXml();
        administradores.toXml();
        usuarios.printInOrder();

//        platillos.insertar(p5);
//        platillos.toJson("src/datos/platillos.json");
//        platillos.fromJson("src/datos/platillos.json");
//platillos.inorden();
        try {
            // Crear un servidor socket
            ServerSocket servidorSocket = new ServerSocket(PUERTO);
            System.out.println("Servidor iniciado");

            while (true) {
                // Esperar a que un cliente se conecte
                Socket clienteSocket = servidorSocket.accept();
                System.out.println("Cliente conectado: " + clienteSocket.getInetAddress());

                // Crear flujos de entrada/salida para comunicación con el cliente
                ObjectOutputStream salida = new ObjectOutputStream(clienteSocket.getOutputStream());
                ObjectInputStream entrada = new ObjectInputStream(clienteSocket.getInputStream());

                // Leer objeto Mensaje del cliente
                Mensaje mensaje = (Mensaje) entrada.readObject();

                // Verificar acción del mensaje
                if (mensaje.getAccion().equals("login")) {
                    // Verificar credenciales con el árbol de usuarios válidos
                    boolean credencialesCorrectas = usuarios.contains(mensaje.getUsuario())
                            && usuarios.get(mensaje.getUsuario()).getPassword().equals(mensaje.getPassword());

                    // Enviar respuesta al cliente
                    if (credencialesCorrectas) {
                        salida.writeObject("OK");
                    } else {
                        salida.writeObject("ERROR");
                    }
                } else if (mensaje.getAccion().equals("register")) {

                    // Enviar respuesta al cliente
                    if (usuarios.contains(mensaje.getUsuario())) {
                        salida.writeObject("Nombre de usuario no disponible");
                    } else {
                        usuarios.insert(new Usuario(mensaje.getUsuario(), mensaje.getPassword()));
                        salida.writeObject("Se ha registrado con exito");
                    }
                } else if (mensaje.getAccion().equals("registerAdm")) {

                    // Enviar respuesta al cliente
                    if (administradores.contains(mensaje.getUsuario())) {
                        salida.writeObject("Usuario no disponible");
                    } else {
                        administradores.insert(new Usuario(mensaje.getUsuario(), mensaje.getPassword()));
                        salida.writeObject("Usuario registrado con exito");
                    }
                } else if (mensaje.getAccion().equals("deleteAdm")) {

                    // Enviar respuesta al cliente
                    if (administradores.contains(mensaje.getUsuario())) {
                        administradores.delete(mensaje.getUsuario());
                        administradores.toXml();
                        salida.writeObject("Usuario eliminado");
                    } else {

                        salida.writeObject("Usuario no encontrado");
                    }
                } else if (mensaje.getAccion().equals("editAdm")) {

                    // Enviar respuesta al cliente
                    if (administradores.contains(mensaje.getUsuario())) {
                        if (mensaje.getUsuario().equals(mensaje.getUsuarioNew())) {
                            System.out.println("son iguales");
                        }
                        if (mensaje.getPasswordNew() == "") {
//                            System.out.println(mensaje.getUsuario());
//                            System.out.println(mensaje.getUsuarioNew());
//                            System.out.println(mensaje.getPasswordNew());
                            administradores.edit(mensaje.getUsuario(), mensaje.getUsuarioNew(), mensaje.getPassword());
                            administradores.toXml();
                            salida.writeObject("Usuario Editado");

                        } else {
//                            System.out.println(mensaje.getUsuario());
//                            System.out.println(mensaje.getUsuarioNew());
//                            System.out.println(mensaje.getPasswordNew());
                            administradores.edit(mensaje.getUsuario().toString(), mensaje.getUsuarioNew().toString(), mensaje.getPasswordNew());
                            administradores.toXml();
                            salida.writeObject("Usuario Editado");
                        }

                    } else {

                        salida.writeObject("Usuario no encontrado");
                    }
                } else if (mensaje.getAccion().equals("agregarPlatillo")) {

                    // Enviar respuesta al cliente
                    if (arbol.contains(mensaje.getUsuario())) {
                        salida.writeObject("Ya existe este platillo");
                    } else {
                        
                        arbol.insert(new Platillo(mensaje.getUsuario(), Integer.parseInt(mensaje.getPassword()),Integer.parseInt(mensaje.getUsuarioNew()), Integer.parseInt(mensaje.getPasswordNew())));
                        salida.writeObject("Platillo agregado con exito");
                        arbol.guardar("src/datos/platillos.json");
                    }
                }

//                }
//                registerAdm
//                // Leer credenciales del cliente
//                String nombreUsuario = entrada.readObject().toString();
//                Credenciales credenciales = (Credenciales) ;
//                System.out.println("Credenciales recibidas: " + credenciales);
//
//                // Verificar credenciales con el árbol de usuarios válidos
//                boolean credencialesCorrectas = usuarios.contains(credenciales.getUsuario())
//                        && usuarios.get(credenciales.getUsuario()).getPassword().equals(credenciales.getPassword());
//
//                // Enviar respuesta al cliente
//                if (credencialesCorrectas) {
//                    salida.writeObject("OK");
//                } else {
//                    salida.writeObject("ERROR");
//                }
                // Cerrar flujos de entrada/salida y conexión con el cliente
                entrada.close();
                salida.close();
                clienteSocket.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
