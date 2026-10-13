
package com.clases.proyectodanieljava;

// al momento de subrr el respectivo comitt los errores de la prueba ya estaran solucionados
import Controlador.CelularControlador;
import Controlador.ClienteControlador;
import Excepciones.ErrorSistemaException;
import Persistencia.CelularDAO;
import Persistencia.ClienteDAO;
import Persistencia.ConexionDB;
import Persistencia.UsuarioDAO;
import Servicio.GestorCelulares;
import Servicio.GestorClientes;
import Vista.Consola;
import Vista.MenuCelularesTemporal;
import Vista.MenuClienteTemporal;
import java.sql.Connection;

public class Main {

    public static void main(String[] args) {
        try {
            Connection con = ConexionDB.getInstancia().getConexion();

            // Persistencia
            CelularDAO celularDAO = new CelularDAO(con);
            ClienteDAO clienteDAO = new ClienteDAO(con);
            UsuarioDAO usuarioDAO = new UsuarioDAO(con);

            // Servicio
            GestorCelulares gestorCelulares = new GestorCelulares(celularDAO);
            GestorClientes gestorClientes = new GestorClientes(clienteDAO, usuarioDAO);

            // Controlador
            CelularControlador celularControlador = new CelularControlador(gestorCelulares);
            ClienteControlador clienteControlador = new ClienteControlador(gestorClientes);

            // Vista
            Consola consola = new Consola();
            MenuCelularesTemporal menuCelulares = new MenuCelularesTemporal(consola, celularControlador);
            MenuClienteTemporal menuClientes = new MenuClienteTemporal(consola, clienteControlador);

            OUTER:
            while (true) {
                int opcion = consola.pedirOpcion("PRUEBAS", "Salir", "Celulares", "Clientes");
                switch (opcion) {
                    case 0 -> {
                        break OUTER;
                    }
                    case 1 -> menuCelulares.iniciar();
                    case 2 -> menuClientes.iniciar();
                    default -> consola.imprimir("Opción no válida.");
                }
            }
        } catch (ErrorSistemaException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            ConexionDB.getInstancia().cerrar();
        }
    }
}