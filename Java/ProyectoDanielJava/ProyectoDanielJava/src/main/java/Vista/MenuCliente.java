
package Vista;

import Controlador.CelularControlador;
import Controlador.VentaControlador;
import Modelo.Cliente;
import Modelo.Persona;
import Modelo.Venta;
import java.util.List;

public class MenuCliente implements Menu {

    private final Consola consola;
    private final CelularControlador celulares;
    private final VentaControlador ventas;

    public MenuCliente(Consola consola, CelularControlador celulares, VentaControlador ventas) {
        this.consola = consola;
        this.celulares = celulares;
        this.ventas = ventas;
    }

    @Override
    public void mostrar(Persona usuario) {
        Cliente cliente = (Cliente) usuario;
        
        while (true) {
            int opcion = consola.pedirOpcion("MENÚ CLIENTE", "Cerrar sesión",
                    "Ver catálogo de celulares", "Mis compras");
            switch (opcion) {
                case 0 -> {
                    return;
                }
                case 1 -> consola.ejecutar(() -> consola.mostrarCelulares(celulares.listar()));
                case 2 -> consola.ejecutar(() -> misCompras(cliente));
                default -> consola.imprimir("Opción no válida.");
            }
        }
    }

    private void misCompras(Cliente cliente) {
        List<Venta> compras = ventas.misCompras(cliente);
        
        if (compras.isEmpty()) {
            consola.imprimir("Todavía no tienes compras.");
            return;
        }
        compras.forEach(consola::mostrarDetalleVenta);
    }
}