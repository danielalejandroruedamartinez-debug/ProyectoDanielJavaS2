
package Persistencia;

import Excepciones.ErrorSistemaException;
import Modelo.Cliente;
import Modelo.TipoPersona;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteDAO {
    
        private static final String SELECT =
            "SELECT c.id AS id_cliente, c.fecha_registro, p.id AS id_persona,"
            + " p.nombre, p.identificacion, p.correo, p.telefono"
            + " FROM cliente c"
            + " JOIN persona p ON p.id = c.id_persona";
    
    private final Connection con;
    
    public ClienteDAO(Connection con) {
        this.con = con;
    }
    
    private static Cliente mapear(ResultSet rs) throws SQLException {
        
        Cliente c = new Cliente();
        c.setIdCliente(rs.getInt("id_cliente"));
        c.setId(rs.getInt("id_persona"));
        c.setTipo(TipoPersona.CLIENTE);
        c.setNombre(rs.getString("nombre"));
        c.setIdentificacion(rs.getString("identificacion"));
        c.setCorreo(rs.getString("correo"));
        c.setTelefono(rs.getString("telefono"));
        c.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
        return c;
    }
    
    public List<Cliente> listar() {
        
        try (PreparedStatement ps = con.prepareStatement(SELECT + " ORDER BY c.id");
            ResultSet rs = ps.executeQuery()) {
            List<Cliente> lista = new ArrayList<>();
            
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new ErrorSistemaException("Error al listar los clientes", e);
        }
    }
    
    public Optional<Cliente> buscarPorId(int idCliente) {
        
        try (PreparedStatement ps = con.prepareStatement(SELECT + " WHERE c.id = ?")) {
            ps.setInt(1, idCliente);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new ErrorSistemaException("Error al buscar el cliente", e);
        }
    }
    
    public void guardar(Cliente cliente) {
        
        SqlHelper.transaccion(con, "No se puso guardar el cliente", () -> {
            int idPersona = SqlHelper.insertarPersona(con, cliente);
            cliente.setId(idPersona);
            
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO cliente (id_persona) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
                 ps.setInt(1, idPersona);
                 ps.executeUpdate();
                 
                 try (ResultSet rs = ps.getGeneratedKeys()) {
                     rs.next();
                     cliente.setIdCliente(rs.getInt(1));
                 }
            }
        });
    }
    
    public void actualizar(Cliente cliente) {
        
        SqlHelper.transaccion(con, "No se pudo actualizar el cliente",
                () -> SqlHelper.actualizarPersona(con, "cliente", cliente.getIdCliente(), cliente));
    }
    
    public void eliminar(int idCliente) {
        
        SqlHelper.eliminar(con,
                "DELETE FROM persona WHERE id = (SELECT id_persona FROM cliente WHERE id = ?)", idCliente);
    }
    
}
