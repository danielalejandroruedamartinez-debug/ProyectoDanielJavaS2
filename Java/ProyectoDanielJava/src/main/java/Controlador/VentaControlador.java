
package Controlador;

import Modelo.Cliente;
import Modelo.Vendedor;
import Modelo.Venta;
import Servicio.GestorVentas;
import java.util.List;
import java.util.Map;

public class VentaControlador {

    private final GestorVentas gestor;

    public VentaControlador(GestorVentas gestor) {
        this.gestor = gestor;
    }

    public Venta registrar(Vendedor vendedor, int idCliente, Map<Integer, Integer> pedido) {
        return gestor.registrar(vendedor, idCliente, pedido);
    }

    public List<Venta> listar() { return gestor.listar(); }

    public Venta buscarPorId(int id) { return gestor.buscarPorId(id); }

    public List<Venta> misCompras(Cliente cliente) { return gestor.listarPorCliente(cliente.getIdCliente()); }
}