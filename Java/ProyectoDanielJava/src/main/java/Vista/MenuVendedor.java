
package Vista;

import Controlador.CelularControlador;
import Controlador.ClienteControlador;
import Modelo.Persona;

public class MenuVendedor implements Menu {

    private final Consola consola;
    private final CelularControlador celulares;
    private final ClienteControlador clientes;

    public MenuVendedor(Consola consola, CelularControlador celulares, ClienteControlador clientes) {
        this.consola = consola;
        this.celulares = celulares;
        this.clientes = clientes;
    }

    @Override
    public void mostrar(Persona usuario) {
        
        while (true) {
            int opcion = consola.pedirOpcion("MENÚ VENDEDOR", "Cerrar sesión",
                    "Listar celulares", "Registrar cliente", "Listar clientes");
            if (opcion == 0) {
                return;
            }
            switch (opcion) {
                case 1 -> consola.ejecutar(() -> consola.mostrarCelulares(celulares.listar()));
                case 2 -> consola.ejecutar(this::registrarCliente);
                case 3 -> consola.ejecutar(() -> consola.mostrarClientes(clientes.listar()));
                default -> consola.imprimir("Opción no válida.");
            }
        }
    }

    private void registrarCliente() {
        
        String nombre = consola.leerTexto("Nombre: ");
        String identificacion = consola.leerTexto("Identificación: ");
        String correo = consola.leerTexto("Correo: ");
        String telefono = consola.leerTexto("Teléfono: ");
        String contraseña = consola.leerTexto("Contraseña: ");
        clientes.registrar(nombre, identificacion, correo, telefono, contraseña);
        consola.imprimir("Cliente registrado.");
    }
}
