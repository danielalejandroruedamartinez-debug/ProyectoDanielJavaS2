
package Persistencia;

import Excepciones.ErrorSistemaException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    
}
