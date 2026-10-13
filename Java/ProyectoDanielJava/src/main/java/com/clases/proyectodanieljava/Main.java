
package com.clases.proyectodanieljava;

// al momento de subrr el respectivo comitt los errores de la prueba ya estaran solucionados
import Controlador.CelularControlador;
import Excepciones.ErrorSistemaException;
import Persistencia.CelularDAO;
import Persistencia.ConexionDB;
import Servicio.GestorCelulares;
import Vista.Consola;
import Vista.MenuCelularesTemporal;
import java.sql.Connection;

public class Main {

    public static void main(String[] args) {
        try {
            Connection con = ConexionDB.getInstancia().getConexion();

            // Persistencia -> Servicio -> Controlador -> Vista
            CelularDAO celularDAO = new CelularDAO(con);
            GestorCelulares gestorCelulares = new GestorCelulares(celularDAO);
            CelularControlador celularControlador = new CelularControlador(gestorCelulares);

            Consola consola = new Consola();
            new MenuCelularesTemporal(consola, celularControlador).iniciar();
        } catch (ErrorSistemaException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            ConexionDB.getInstancia().cerrar();
        }
    }
}