
package Vista;

import Controlador.CelularControlador;
import Modelo.Persona;

public class MenuCliente implements Menu {

    private final Consola consola;
    private final CelularControlador celulares;

    public MenuCliente(Consola consola, CelularControlador celulares) {
        this.consola = consola;
        this.celulares = celulares;
    }

    @Override
    public void mostrar(Persona usuario) {
        while (true) {
            int opcion = consola.pedirOpcion("MENÚ CLIENTE", "Cerrar sesión", "Ver catálogo de celulares");
            if (opcion == 0) {
                return;
            }
            if (opcion == 1) {
                consola.ejecutar(() -> consola.mostrarCelulares(celulares.listar()));
            } else {
                consola.imprimir("Opción no válida.");
            }
        }
    }
}