
package Persistencia;

import Excepciones.ErrorSistemaException;
import Modelo.Persona;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

final class SqlHelper {

    interface Operacion {
        void ejecutar() throws SQLException;
    }

    private SqlHelper() { }

    static void transaccion(Connection con, String mensajeError, Operacion operacion) {
        try {
            con.setAutoCommit(false);
            operacion.ejecutar();
            con.commit();
        } catch (SQLException e) {
            deshacer(con);
            throw new ErrorSistemaException(mensajeError, e);
        } catch (RuntimeException e) {
            deshacer(con);
            throw e;
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException ignorada) {
            }
        }
    }

    private static void deshacer(Connection con) {
        try {
            con.rollback();
        } catch (SQLException ignorada) {
        }
    }

    static int insertarPersona(Connection con, Persona p) throws SQLException {
        String sql = "INSERT INTO persona (id_tipo_persona, nombre, identificacion, correo, telefono, contraseña) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getTipo().getId());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getIdentificacion());
            ps.setString(4, p.getCorreo());
            ps.setString(5, p.getTelefono());
            ps.setString(6, p.getContraseña());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    static void actualizarPersona(Connection con, String tablaRol, int idRol, Persona p) throws SQLException {
        String sql = "UPDATE persona SET nombre = ?, correo = ?, telefono = ? "
                + "WHERE id = (SELECT id_persona FROM " + tablaRol + " WHERE id = ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCorreo());
            ps.setString(3, p.getTelefono());
            ps.setInt(4, idRol);
            ps.executeUpdate();
        }
    }

    static void eliminar(Connection con, String sql, int id) {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new ErrorSistemaException(
                    "No se puede eliminar porque tiene registros relacionados (por ejemplo, ventas)", e);
        } catch (SQLException e) {
            throw new ErrorSistemaException("Error al eliminar el registro", e);
        }
    }
}
