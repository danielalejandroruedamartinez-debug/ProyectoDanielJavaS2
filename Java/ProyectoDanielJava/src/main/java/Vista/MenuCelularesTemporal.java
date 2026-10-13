
package Vista;


import Controlador.CelularControlador;
import Modelo.CategoriaGama;
import java.math.BigDecimal;
import java.util.Arrays;

// Esto es temporal se lo he pedido a la ia con el unico propacito de probar lo realizado en este modulo
public class MenuCelularesTemporal {

    private final Consola consola;
    private final CelularControlador celulares;

    public MenuCelularesTemporal(Consola consola, CelularControlador celulares) {
        this.consola = consola;
        this.celulares = celulares;
    }

    public void iniciar() {
        while (true) {
            int opcion = consola.pedirOpcion("CELULARES", "Salir",
                    "Listar celulares", "Registrar celular", "Actualizar celular", "Eliminar celular");
            if (opcion == 0) {
                return;
            }
            switch (opcion) {
                case 1 -> consola.ejecutar(() -> consola.mostrarCelulares(celulares.listar()));
                case 2 -> consola.ejecutar(() -> pedirCelular(null));
                case 3 -> consola.ejecutar(this::actualizarCelular);
                case 4 -> consola.ejecutar(this::eliminarCelular);
                default -> consola.imprimir("Opción no válida.");
            }
        }
    }

    private void actualizarCelular() {
        consola.mostrarCelulares(celulares.listar());
        int id = consola.leerEntero("Id del celular a actualizar: ");
        consola.imprimir("Ingresa los nuevos datos:");
        pedirCelular(id);
    }

    /** Pide los datos del celular. Si idExistente es null registra uno nuevo; si no, actualiza ese. */
    private void pedirCelular(Integer idExistente) {
        String marca = consola.elejir("Marca", celulares.listarMarcas());
        String modelo = consola.leerTexto("Modelo: ");
        BigDecimal precio = consola.leerDecimal("Precio: ");
        int stock = consola.leerEntero("Stock: ");
        String so = consola.elejir("Sistema operativo", celulares.listarSistemasOperativos());
        CategoriaGama gama = consola.elejir("Gama", Arrays.asList(CategoriaGama.values()));

        if (idExistente == null) {
            celulares.registrar(marca, modelo, precio, stock, so, gama);
            consola.imprimir("Celular registrado.");
        } else {
            celulares.actualizar(idExistente, marca, modelo, precio, stock, so, gama);
            consola.imprimir("Celular actualizado.");
        }
    }

    private void eliminarCelular() {
        consola.mostrarCelulares(celulares.listar());
        celulares.eliminar(consola.leerEntero("Id del celular a eliminar: "));
        consola.imprimir("Celular eliminado.");
    }
}
