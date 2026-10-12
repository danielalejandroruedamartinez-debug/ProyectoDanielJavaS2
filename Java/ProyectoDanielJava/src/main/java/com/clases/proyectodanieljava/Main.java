
package com.clases.proyectodanieljava;

import Controlador.CelularControlador;
import Excepciones.DatosInvalidosException;
import Excepciones.ErrorSistemaException;
import Modelo.CategoriaGama;
import Modelo.Celular;
import Persistencia.CelularDAO;
import Persistencia.ConexionDB;
import Servicio.GestorCelulares;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            Connection con = ConexionDB.getInstancia().getConexion();
            CelularDAO celularDAO = new CelularDAO(con);
            GestorCelulares gestor = new GestorCelulares(celularDAO);
            CelularControlador controlador = new CelularControlador(gestor);

            System.out.println("--- 1. Lista inicial ---");
            imprimir(controlador.listar());

            System.out.println("\n--- 2. Registrar ---");
            intentar("registrar celular válido",
                    () -> controlador.registrar("Samsung", "Test X", new BigDecimal("1000000"), 3, "Android", CategoriaGama.MEDIA));
            intentar("precio negativo",
                    () -> controlador.registrar("Samsung", "Malo", new BigDecimal("-5"), 3, "Android", CategoriaGama.MEDIA));
            intentar("stock negativo",
                    () -> controlador.registrar("Samsung", "Malo", new BigDecimal("1000"), -1, "Android", CategoriaGama.MEDIA));
            intentar("modelo vacío",
                    () -> controlador.registrar("Samsung", "  ", new BigDecimal("1000"), 1, "Android", CategoriaGama.MEDIA));
            intentar("marca que no existe",
                    () -> controlador.registrar("Nokia", "Malo", new BigDecimal("1000"), 1, "Android", CategoriaGama.MEDIA));

            System.out.println("\n--- 3. Lista con el celular nuevo ---");
            List<Celular> lista = controlador.listar();
            imprimir(lista);

            // Busca el celular de prueba por su modelo; si no está, no toca nada más
            Celular prueba = lista.stream()
                    .filter(c -> c.getModelo().equals("Test X"))
                    .findFirst()
                    .orElse(null);
            if (prueba == null) {
                System.out.println("\nNo se registró el celular de prueba. Se detiene aquí para no tocar los datos reales.");
                return;
            }
            int idPrueba = prueba.getId();

            System.out.println("\n--- 4. Actualizar ---");
            intentar("actualizar el celular de prueba",
                    () -> controlador.actualizar(idPrueba, "Samsung", "Test X Pro", new BigDecimal("1200000"), 7, "Android", CategoriaGama.ALTA));
            imprimir(controlador.listar());

            System.out.println("\n--- 5. Eliminar ---");
            intentar("eliminar el celular de prueba", () -> controlador.eliminar(idPrueba));
            intentar("eliminar el celular 1 (ya tiene ventas)", () -> controlador.eliminar(1));
            intentar("eliminar un id que no existe", () -> controlador.eliminar(9999));

            System.out.println("\n--- 6. Lista final (igual que la inicial) ---");
            imprimir(controlador.listar());
        } catch (ErrorSistemaException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            ConexionDB.getInstancia().cerrar();
        }
    }

    private static void imprimir(List<Celular> lista) {
        for (Celular c : lista) {
            System.out.println(c.getId() + " | " + c.getNombreCompleto() + " | " + c.getPrecio()
                    + " | stock " + c.getStock() + " | " + c.getSistemaOperativo() + " | " + c.getGama());
        }
    }

    private static void intentar(String descripcion, Runnable accion) {
        try {
            accion.run();
            System.out.println("OK        | " + descripcion);
        } catch (DatosInvalidosException | ErrorSistemaException e) {
            System.out.println("Rechazado | " + descripcion + " -> " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("            Causa real: " + e.getCause().getMessage());
            }
        }
    }
}