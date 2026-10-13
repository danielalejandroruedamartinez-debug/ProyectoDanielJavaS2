
package Vista;

import Controlador.ClienteControlador;

/** TEMPORAL: sirve para probar Clientes. Después sus métodos pasan a los menús por rol. */
public class MenuClienteTemporal {

    private final Consola consola;
    private final ClienteControlador clientes;

    public MenuClienteTemporal(Consola consola, ClienteControlador clientes) {
        this.consola = consola;
        this.clientes = clientes;
    }

    public void iniciar() {
        while (true) {
            int opcion = consola.pedirOpcion("CLIENTES", "Volver",
                    "Listar clientes", "Registrar cliente", "Actualizar cliente", "Eliminar cliente");
            if (opcion == 0) {
                return;
            }
            switch (opcion) {
                case 1:
                    consola.ejecutar(() -> consola.mostrarClientes(clientes.listar()));
                    break;
                case 2:
                    consola.ejecutar(this::registrarCliente);
                    break;
                case 3:
                    consola.ejecutar(this::actualizarCliente);
                    break;
                case 4:
                    consola.ejecutar(this::eliminarCliente);
                    break;
                default:
                    consola.imprimir("Opción no válida.");
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

    private void actualizarCliente() {
        consola.mostrarClientes(clientes.listar());
        int id = consola.leerEntero("Id del cliente a actualizar: ");
        String nombre = consola.leerTexto("Nuevo nombre: ");
        String correo = consola.leerTexto("Nuevo correo: ");
        String telefono = consola.leerTexto("Nuevo teléfono: ");
        clientes.actualizar(id, nombre, correo, telefono);
        consola.imprimir("Cliente actualizado.");
    }

    private void eliminarCliente() {
        consola.mostrarClientes(clientes.listar());
        clientes.eliminar(consola.leerEntero("Id del cliente a eliminar: "));
        consola.imprimir("Cliente eliminado.");
    }
}
