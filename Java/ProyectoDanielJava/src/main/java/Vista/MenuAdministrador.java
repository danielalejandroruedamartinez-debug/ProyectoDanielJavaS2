
package Vista;

import Controlador.CelularControlador;
import Controlador.ClienteControlador;
import Controlador.VentaControlador;
import Modelo.CategoriaGama;
import Modelo.Persona;
import java.math.BigDecimal;
import java.util.Arrays;

public class MenuAdministrador implements Menu {

    private final Consola consola;
    private final CelularControlador celulares;
    private final ClienteControlador clientes;
    private final VentaControlador ventas;
    private final Menu menuReportes;

    public MenuAdministrador(Consola consola, CelularControlador celulares, ClienteControlador clientes,
                             VentaControlador ventas, Menu menuReportes) {
        this.consola = consola;
        this.celulares = celulares;
        this.clientes = clientes;
        this.ventas = ventas;
        this.menuReportes = menuReportes;
    }

    @Override
    public void mostrar(Persona usuario) {
        while (true) {
            int opcion = consola.pedirOpcion("MENÚ ADMINISTRADOR", "Cerrar sesión",
                    "Listar celulares", "Registrar celular", "Actualizar celular", "Eliminar celular",
                    "Listar clientes", "Actualizar cliente", "Eliminar cliente",
                    "Listar ventas", "Ver detalle de una venta", "Reportes");
            if (opcion == 0) {
                return;
            }
            switch (opcion) {
                case 1 -> consola.ejecutar(() -> consola.mostrarCelulares(celulares.listar()));
                case 2 -> consola.ejecutar(() -> pedirCelular(null));
                case 3 -> consola.ejecutar(this::actualizarCelular);
                case 4 -> consola.ejecutar(this::eliminarCelular);
                case 5 -> consola.ejecutar(() -> consola.mostrarClientes(clientes.listar()));
                case 6 -> consola.ejecutar(this::actualizarCliente);
                case 7 -> consola.ejecutar(this::eliminarCliente);
                case 8 -> consola.ejecutar(() -> consola.mostrarVentas(ventas.listar()));
                case 9 -> consola.ejecutar(this::verDetalleVenta);
                case 10 -> menuReportes.mostrar(usuario);
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


    private void verDetalleVenta() {
        consola.mostrarVentas(ventas.listar());
        consola.mostrarDetalleVenta(ventas.buscarPorId(consola.leerEntero("Id de la venta: ")));
    }
}
