
package com.clases.proyectodanieljava;

import Excepciones.DatosInvalidosException;
import Excepciones.ErrorSistemaException;
import Persistencia.ConexionDB;
import Util.Validador;
import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        try {
            ConexionDB.getInstancia().getConexion();
            System.out.println("Conexión abierta correctamente");
            System.out.println("Singleton (misma instancia): "
                    + (ConexionDB.getInstancia() == ConexionDB.getInstancia()));
        } catch (ErrorSistemaException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            ConexionDB.getInstancia().cerrar();
        }

        probar("nombre con espacios", () -> Validador.noVacio("  Ana ", "El nombre"));
        probar("nombre vacío", () -> Validador.noVacio("   ", "El nombre"));
        probar("correo bueno", () -> Validador.correo("juan@correo.com"));
        probar("correo malo", () -> Validador.correo("juan@"));
        probar("identificación buena", () -> Validador.soloDigitos("1002345678", "La identificación", 5, 20));
        probar("identificación con letras", () -> Validador.soloDigitos("12ab", "La identificación", 5, 20));
        probar("precio negativo", () -> Validador.positivoP(new BigDecimal("-5"), "El precio"));
        probar("stock negativo", () -> Validador.noNegativo(-1, "El stock"));
        probar("stock cero", () -> Validador.noNegativo(0, "El stock"));
    }

    private static void probar(String descripcion, Runnable validacion) {
        try {
            validacion.run();
            System.out.println("OK        | " + descripcion);
        } catch (DatosInvalidosException e) {
            System.out.println("Rechazado | " + descripcion + " -> " + e.getMessage());
        }
    }
}