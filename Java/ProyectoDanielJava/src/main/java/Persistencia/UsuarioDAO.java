
package Persistencia;

import Modelo.Administrador;
import Modelo.Cliente;
import Modelo.Persona;
import Modelo.TipoPersona;
import Modelo.Vendedor;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Optional;
import Excepciones.ErrorSistemaException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    
    private final Connection con;
    
    public UsuarioDAO(Connection con) {
        this.con = con;
    }
    
    public boolean existeIdentificacion(String identificacion) {
        return existe("SELECT 1 FROM persona WHERE identificacion = ? LIMIT 1", identificacion);
    }
    
    public boolean existeCorreo(String correo) {
        return existe("SELECT 1 FROM persona WHERE correo = ? LIMIT 1", correo);
    }
    
    public boolean existeCorreoDeOtraPersona(String correo, int idPersona) {
        return existe("SELECT 1 FROM persona WHERE correo = ? AND id <> ? LIMIT 1", correo, idPersona);
    }
    
    private boolean existe(String sql, Object... parametros) {
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            
            for (int i = 0; i < parametros.length; i++) {
                ps.setObject(i + 1, parametros[i]);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new ErrorSistemaException("Error al validar los datos", e);
        }
    }
    
    private static final String SELECT_LOGIN =
            "SELECT p.id, p.id_tipo_persona, p.nombre, p.identificacion, p.correo, p.telefono, p.contraseña,"
            + " c.id AS id_cliente, c.fecha_registro AS fecha_cliente,"
            + " v.id AS id_vendedor, v.cargo, v.fecha_ingreso,"
            + " a.id AS id_admin, a.es_principal, a.id_admin_creador, a.fecha_registro AS fecha_admin"
            + " FROM persona p"
            + " LEFT JOIN cliente c ON c.id_persona = p.id"
            + " LEFT JOIN vendedor v ON v.id_persona = p.id"
            + " LEFT JOIN administrador a ON a.id_persona = p.id"
            + " WHERE p.correo = ?";
    
 public Optional<Persona> buscarPorCorreo(String correo) {
        try (PreparedStatement ps = con.prepareStatement(SELECT_LOGIN)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapearLogin(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new ErrorSistemaException("Error al buscar el usuario", e);
        }
    }
    
    private static Persona mapearLogin(ResultSet rs) throws SQLException {
        
        TipoPersona tipo = TipoPersona.desdeId(rs.getInt("id_tipo_persona"));
        
        Persona p;
        
        switch (tipo) {
            case CLIENTE ->  {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("id_cliente"));
                Timestamp t = rs.getTimestamp("fecha_cliente");
                c.setFechaRegistro(t == null ? null : t.toLocalDateTime());
                p = c;
            }
            case VENDEDOR ->  {
                Vendedor v = new Vendedor();
                v.setIdVendedor(rs.getInt("id_vendedor"));
                v.setCargo(rs.getString("cargo"));
                Date d = rs.getDate("fecha_ingreso");
                v.setFechaIngreso(d == null ? null : d.toLocalDate());
                p = v;
            }
            default -> {
                Administrador a = new Administrador();
                a.setIdAdministrador(rs.getInt("id_admin"));
                a.setPrincipal(rs.getBoolean("es_principal"));
                int creador = rs.getInt("id_admin_creador");
                a.setIdAdminCreator(rs.wasNull() ? null : creador);
                Timestamp t = rs.getTimestamp("fecha_admin");
                a.setFechaRegistro(t == null ? null : t.toLocalDateTime());
                p = a;
            }
        }
        p.setId(rs.getInt("id"));
        p.setTipo(tipo);
        p.setNombre(rs.getString("nombre"));
        p.setIdentificacion(rs.getString("identificacion"));
        p.setCorreo(rs.getString("correo"));
        p.setTelefono(rs.getString("telefono"));
        p.setContraseña(rs.getString("contraseña"));
        return p;
    }
    
        // ---------- vendedores ----------

    private static final String SELECT_VENDEDOR =
            "SELECT v.id AS id_vendedor, v.cargo, v.fecha_ingreso, p.id AS id_persona,"
            + " p.nombre, p.identificacion, p.correo, p.telefono"
            + " FROM vendedor v"
            + " JOIN persona p ON p.id = v.id_persona";

    private static Vendedor mapearVendedor(ResultSet rs) throws SQLException {
        Vendedor v = new Vendedor();
        v.setIdVendedor(rs.getInt("id_vendedor"));
        v.setId(rs.getInt("id_persona"));
        v.setTipo(TipoPersona.VENDEDOR);
        v.setNombre(rs.getString("nombre"));
        v.setIdentificacion(rs.getString("identificacion"));
        v.setCorreo(rs.getString("correo"));
        v.setTelefono(rs.getString("telefono"));
        v.setCargo(rs.getString("cargo"));
        v.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
        return v;
    }

    public List<Vendedor> listarVendedores() {
        try (PreparedStatement ps = con.prepareStatement(SELECT_VENDEDOR + " ORDER BY v.id");
             ResultSet rs = ps.executeQuery()) {
            List<Vendedor> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapearVendedor(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new ErrorSistemaException("Error al listar los vendedores", e);
        }
    }

    public void guardarVendedor(Vendedor vendedor) {
        SqlHelper.transaccion(con, "No se pudo guardar el vendedor", () -> {
            int idPersona = SqlHelper.insertarPersona(con, vendedor);
            vendedor.setId(idPersona);
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO vendedor (id_persona, cargo, fecha_ingreso) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, idPersona);
                ps.setString(2, vendedor.getCargo());
                ps.setDate(3, Date.valueOf(vendedor.getFechaIngreso()));
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    vendedor.setIdVendedor(rs.getInt(1));
                }
            }
        });
    }

    public void eliminarVendedor(int idVendedor) {
        SqlHelper.eliminar(con,
                "DELETE FROM persona WHERE id = (SELECT id_persona FROM vendedor WHERE id = ?)", idVendedor);
    }

    // ---------- administradores ----------

    private static final String SELECT_ADMIN =
            "SELECT a.id AS id_admin, a.es_principal, a.id_admin_creador, a.fecha_registro,"
            + " p.id AS id_persona, p.nombre, p.identificacion, p.correo, p.telefono"
            + " FROM administrador a"
            + " JOIN persona p ON p.id = a.id_persona";

    private static Administrador mapearAdmin(ResultSet rs) throws SQLException {
        Administrador a = new Administrador();
        a.setIdAdministrador(rs.getInt("id_admin"));
        a.setId(rs.getInt("id_persona"));
        a.setTipo(TipoPersona.ADMINISTRADOR);
        a.setNombre(rs.getString("nombre"));
        a.setIdentificacion(rs.getString("identificacion"));
        a.setCorreo(rs.getString("correo"));
        a.setTelefono(rs.getString("telefono"));
        a.setPrincipal(rs.getBoolean("es_principal"));
        int creador = rs.getInt("id_admin_creador");
        a.setIdAdminCreator(rs.wasNull() ? null : creador);
        a.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
        return a;
    }

    public List<Administrador> listarAdministradores() {
        try (PreparedStatement ps = con.prepareStatement(SELECT_ADMIN + " ORDER BY a.id");
             ResultSet rs = ps.executeQuery()) {
            List<Administrador> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapearAdmin(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new ErrorSistemaException("Error al listar los administradores", e);
        }
    }

    public Optional<Administrador> buscarAdministrador(int idAdmin) {
        try (PreparedStatement ps = con.prepareStatement(SELECT_ADMIN + " WHERE a.id = ?")) {
            ps.setInt(1, idAdmin);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapearAdmin(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new ErrorSistemaException("Error al buscar el administrador", e);
        }
    }

    public void guardarAdministrador(Administrador admin) {
        SqlHelper.transaccion(con, "No se pudo guardar el administrador", () -> {
            int idPersona = SqlHelper.insertarPersona(con, admin);
            admin.setId(idPersona);
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO administrador (id_persona, es_principal, id_admin_creador) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, idPersona);
                ps.setInt(2, admin.isPrincipal() ? 1 : 0);
                ps.setObject(3, admin.getIdAdminCreator());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    admin.setIdAdministrador(rs.getInt(1));
                }
            }
        });
    }

    public void eliminarAdministrador(int idAdmin) {
        SqlHelper.eliminar(con,
                "DELETE FROM persona WHERE id = (SELECT id_persona FROM administrador WHERE id = ?)", idAdmin);
    }
}
    

