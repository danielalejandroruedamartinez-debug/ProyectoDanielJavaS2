
package Persistencia;

import Excepciones.ErrorSistemaException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexionDB {

    private static final String URL = "jdbc:mariadb://localhost:3306/tecnostoreDaniel_db";
    private static final String USUARIO = "root";
    private static final String CLAVE = "123";

    private static ConexionDB instancia;
    private Connection conexion;

    private ConexionDB() { }

    public static synchronized ConexionDB getInstancia() {
        
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    public synchronized Connection getConexion() {
        
        try {
            
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
            }
            return conexion;
        } catch (SQLException e) {
            throw new ErrorSistemaException(
                    "No se pudo conectar a la base de datos.", e);
        }
    }

    public synchronized void cerrar() {
        
        try {
            
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException ignorada) {
            
        }
    }
}
