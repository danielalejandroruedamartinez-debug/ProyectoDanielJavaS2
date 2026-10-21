
package Persistencia;

import Excepciones.ErrorSistemaException;
import Excepciones.StockInsuficienteException;
import Modelo.Celular;
import Modelo.Cliente;
import Modelo.ItemVenta;
import Modelo.Vendedor;
import Modelo.Venta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VentaDAO {

    private static final String SELECT_VENTA =
            "SELECT v.id, v.fecha, v.porcentaje_iva,"
            + " c.id AS id_cliente, pc.nombre AS nombre_cliente,"
            + " ve.id AS id_vendedor, pv.nombre AS nombre_vendedor"
            + " FROM venta v"
            + " JOIN cliente c ON c.id = v.id_cliente"
            + " JOIN persona pc ON pc.id = c.id_persona"
            + " JOIN vendedor ve ON ve.id = v.id_vendedor"
            + " JOIN persona pv ON pv.id = ve.id_persona";

    private static final String SELECT_ITEMS =
            "SELECT d.cantidad, d.precio_unitario, ce.id AS id_celular, ma.nombre AS marca, ce.modelo"
            + " FROM detalle_venta d"
            + " JOIN celular ce ON ce.id = d.id_celular"
            + " JOIN marca ma ON ma.id = ce.id_marca"
            + " WHERE d.id_venta = ?"
            + " ORDER BY d.id";

    private final Connection con;

    public VentaDAO(Connection con) {
        this.con = con;
    }

    public void guardar(Venta venta) {
        SqlHelper.transaccion(con, "No se pudo guardar la venta", () -> {
            
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO venta (id_cliente, id_vendedor, porcentaje_iva) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, venta.getCliente().getIdCliente());
                ps.setInt(2, venta.getVendedor().getIdVendedor());
                ps.setBigDecimal(3, venta.getPorcentajeIva());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    venta.setId(rs.getInt(1));
                }
            }
            for (ItemVenta item : venta.getItems()) {
                
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO detalle_venta (id_venta, id_celular, cantidad, precio_unitario) VALUES (?, ?, ?, ?)")) {
                    ps.setInt(1, venta.getId());
                    ps.setInt(2, item.getCelular().getId());
                    ps.setInt(3, item.getCantidad());
                    ps.setBigDecimal(4, item.getPrecioUnitario());
                    ps.executeUpdate();
                }
                
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE celular SET stock = stock - ? WHERE id = ? AND stock >= ?")) {
                    ps.setInt(1, item.getCantidad());
                    ps.setInt(2, item.getCelular().getId());
                    ps.setInt(3, item.getCantidad());
                    if (ps.executeUpdate() == 0) {
                        throw new StockInsuficienteException(
                                "Stock insuficiente para " + item.getCelular().getNombreCompleto());
                    }
                }
            }
        });
    }

    public List<Venta> listar() {
        return consultar("");
    }

    public List<Venta> listarPorCliente(int idCliente) {
        return consultar(" WHERE v.id_cliente = ?", idCliente);
    }

    public Optional<Venta> buscarPorId(int id) {
        return consultar(" WHERE v.id = ?", id).stream().findFirst();
    }

    private List<Venta> consultar(String condicion, int... parametros) {
        List<Venta> ventas = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(SELECT_VENTA + condicion + " ORDER BY v.id")) {
            for (int i = 0; i < parametros.length; i++) {
                ps.setInt(i + 1, parametros[i]);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ventas.add(mapearVenta(rs));
                }
            }
        } catch (SQLException e) {
            throw new ErrorSistemaException("Error al consultar las ventas", e);
        }
        
        for (Venta v : ventas) {
            cargarItems(v);
        }
        return ventas;
    }

    private static Venta mapearVenta(ResultSet rs) throws SQLException {
        Venta v = new Venta();
        v.setId(rs.getInt("id"));
        v.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
        v.setPorcentajeIva(rs.getBigDecimal("porcentaje_iva"));

        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("id_cliente"));
        cliente.setNombre(rs.getString("nombre_cliente"));
        v.setCliente(cliente);

        Vendedor vendedor = new Vendedor();
        vendedor.setIdVendedor(rs.getInt("id_vendedor"));
        vendedor.setNombre(rs.getString("nombre_vendedor"));
        v.setVendedor(vendedor);
        return v;
    }

    private void cargarItems(Venta venta) {
        
        try (PreparedStatement ps = con.prepareStatement(SELECT_ITEMS)) {
            ps.setInt(1, venta.getId());
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Celular c = new Celular();
                    c.setId(rs.getInt("id_celular"));
                    c.setMarca(rs.getString("marca"));
                    c.setModelo(rs.getString("modelo"));
                    venta.agregarItem(new ItemVenta(c, rs.getInt("cantidad"), rs.getBigDecimal("precio_unitario")));
                }
            }
        } catch (SQLException e) {
            throw new ErrorSistemaException("Error al cargar el detalle de la venta", e);
        }
    }
}
