
package Vista;

import Controlador.CelularControlador;
import Controlador.ClienteControlador;
import Controlador.VentaControlador;
import Modelo.Persona;
import Modelo.Vendedor;
import java.util.LinkedHashMap;
import java.util.Map;

public class MenuVendedor implements Menu {

    private final Consola consola;
    private final CelularControlador celulares;
    private final ClienteControlador clientes;
    private final VentaControlador ventas;

    public MenuVendedor(Consola consola, CelularControlador celulares, ClienteControlador clientes,
                        VentaControlador ventas) {
        this.consola = consola;
        this.celulares = celulares;
        this.clientes = clientes;
        this.ventas = ventas;
    }

    @Override
    public void mostrar(Persona usuario) {
        Vendedor vendedor = (Vendedor) usuario;
        
        while (true) {
            
            int opcion = consola.pedirOpcion("MENÚ VENDEDOR", "Cerrar sesión",
                    "Listar celulares", "Registrar cliente", "Listar clientes",
                    "Registrar venta", "Listar ventas", "Ver detalle de una venta");
            
            if (opcion == 0) {
                return;
            }
            switch (opcion) {
                case 1 -> consola.ejecutar(() -> consola.mostrarCelulares(celulares.listar()));
                case 2 -> consola.ejecutar(this::registrarCliente);
                case 3 -> consola.ejecutar(() -> consola.mostrarClientes(clientes.listar()));
                case 4 -> consola.ejecutar(() -> registrarVenta(vendedor));
                case 5 -> consola.ejecutar(() -> consola.mostrarVentas(ventas.listar()));
                case 6 -> consola.ejecutar(this::verDetalle);
                default -> consola.imprimir("Opción no válida.");
            }
        }
    }

    private void registrarCliente() {
        String nombre = consola.leerTexto("Nombre: ");
        String identificacion = consola.leerTexto("Identificación: ");
        String correo = consola.leerTexto("Correo: ");
        String telefono = consola.leerTexto("Teléfono: ");
        String contrasena = consola.leerTexto("Contraseña: ");
        clientes.registrar(nombre, identificacion, correo, telefono, contrasena);
        consola.imprimir("Cliente registrado.");
    }

    private void registrarVenta(Vendedor vendedor) {
        consola.mostrarClientes(clientes.listar());
        int idCliente = consola.leerEntero("Id del cliente: ");
        consola.mostrarCelulares(celulares.listar());

        Map<Integer, Integer> pedido = new LinkedHashMap<>();
        while (true) {
            int idCelular = consola.leerEntero("Id del celular (0 para terminar): ");
            if (idCelular == 0) {
                break;
            }
            int cantidad = consola.leerEntero("Cantidad: ");
            pedido.merge(idCelular, cantidad, Integer::sum);
        }
        consola.mostrarDetalleVenta(ventas.registrar(vendedor, idCliente, pedido));
        consola.imprimir("Venta registrada y stock actualizado.");
    }

    private void verDetalle() {
        consola.mostrarVentas(ventas.listar());
        consola.mostrarDetalleVenta(ventas.buscarPorId(consola.leerEntero("Id de la venta: ")));
    }
}
