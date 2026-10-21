
package Servicio;

import Excepciones.DatosInvalidosException;
import Excepciones.StockInsuficienteException;
import Modelo.Celular;
import Modelo.Cliente;
import Modelo.ItemVenta;
import Modelo.Vendedor;
import Modelo.Venta;
import Persistencia.CelularDAO;
import Persistencia.ClienteDAO;
import Persistencia.VentaDAO;
import Util.Validador;
import java.util.List;
import java.util.Map;

public class GestorVentas {

    private final VentaDAO ventaDAO;
    private final CelularDAO celularDAO;
    private final ClienteDAO clienteDAO;

    public GestorVentas(VentaDAO ventaDAO, CelularDAO celularDAO, ClienteDAO clienteDAO) {
        this.ventaDAO = ventaDAO;
        this.celularDAO = celularDAO;
        this.clienteDAO = clienteDAO;
    }


    public Venta registrar(Vendedor vendedor, int idCliente, Map<Integer, Integer> pedido) {
        if (pedido == null || pedido.isEmpty()) {
            throw new DatosInvalidosException("La venta debe tener al menos un celular");
        }
        Cliente cliente = clienteDAO.buscarPorId(idCliente)
                .orElseThrow(() -> new DatosInvalidosException("El cliente no existe"));

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setVendedor(vendedor);

        for (Map.Entry<Integer, Integer> linea : pedido.entrySet()) {
            Validador.positivo(linea.getValue(), "La cantidad");
            Celular celular = celularDAO.buscarPorId(linea.getKey())
                    .orElseThrow(() -> new DatosInvalidosException("El celular " + linea.getKey() + " no existe"));
            if (celular.getStock() < linea.getValue()) {
                throw new StockInsuficienteException("Stock insuficiente para " + celular.getNombreCompleto()
                        + " (disponible: " + celular.getStock() + ")");
            }
            venta.agregarItem(new ItemVenta(celular, linea.getValue(), celular.getPrecio()));
        }
        ventaDAO.guardar(venta);
        return ventaDAO.buscarPorId(venta.getId()).orElse(venta);
    }

    public List<Venta> listar() { return ventaDAO.listar(); }

    public Venta buscarPorId(int id) {
        return ventaDAO.buscarPorId(id).orElseThrow(() -> new DatosInvalidosException("La venta no existe"));
    }

    public List<Venta> listarPorCliente(int idCliente) { return ventaDAO.listarPorCliente(idCliente); }
}
