
package Persistencia;

import Excepciones.ErrorSistemaException;
import Modelo.CategoriaGama;
import Modelo.Celular;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CelularDAO {
    
    private static final String SELECT =
            "SELECT id, marca, modelo, precio, stock, sistema_operativo, gama FROM vista_celular";
    
    private final Connection con;

    public CelularDAO(Connection con) {
        
        this.con =con;
    }
    
    private static Celular mapear(ResultSet rs) throws SQLException {
        
        Celular c = new Celular();
        
        c.setId(rs.getInt("id"));
        c.setMarca(rs.getString("marca"));
        c.setModelo(rs.getString("modelo"));
        c.setPrecio(rs.getBigDecimal("precio"));
        c.setStock(rs.getInt("stock"));
        c.setSistemaOperativo(rs.getString("sistema_operativo"));
        c.setGama(CategoriaGama.desdeNombre(rs.getString("gama")));
        return c;
    }
    
    public List<Celular> listar() {
        
        try (PreparedStatement ps = con.prepareStatement(SELECT + " ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            List<Celular> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new ErrorSistemaException("Error al listar los celulares", e);
        }
    }
    
    public Optional<Celular> buscarPorId(int id) {
        String sql = SELECT + " WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new ErrorSistemaException("Error al buscar el celular", e);
        }
    }

    public List<String> listarMarcas() {
        return listarNombres("SELECT nombre FROM marca ORDER BY id");
    }

    public List<String> listarSistemasOperativos() {
        return listarNombres("SELECT nombre FROM sistema_operativo ORDER BY id");
    }

    private List<String> listarNombres(String sql) {
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<String> nombres = new ArrayList<>();
            while (rs.next()) {
                nombres.add(rs.getString(1));
            }
            return nombres;
        } catch (SQLException e) {
            throw new ErrorSistemaException("Error al consultar el catálogo", e);
        }
    }
    
    public void guardar(Celular c) {
        
        String sql = "INSERT INTO celular (id_marca, modelo, precio, stock, id_sistema_operativo, id_gama)"
                + "VALUES ((SELECT id FROM marca WHERE nombre = ?), ?, ?, ?, "
                + "(SELECT id FROM sistema_operativo WHERE nombre = ?), ?)";
        
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getMarca());
            ps.setString(2, c.getModelo());
            ps.setBigDecimal(3, c.getPrecio());
            ps.setInt(4, c.getStock());
            ps.setString(5, c.getSistemaOperativo());
            ps.setInt(6, c.getGama().getId());
            ps.executeUpdate();
            
            try(ResultSet rs = ps.getGeneratedKeys()) {
                
                if (rs.next()) {
                    c.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new ErrorSistemaException("Error al guardar el celular", e);
        }
    }
    
    public void actualizar(Celular c) {
        String sql = "UPDATE celular SET id_marca = (SELECT id FROM marca WHERE nombre = ?), modelo = ?, "
                + "precio = ?, stock = ?, id_sistema_operativo = (SELECT id FROM sistema_operativo WHERE nombre = ?), "
                + "id_gama = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getMarca());
            ps.setString(2, c.getModelo());
            ps.setBigDecimal(3, c.getPrecio());
            ps.setInt(4, c.getStock());
            ps.setString(5, c.getSistemaOperativo());
            ps.setInt(6, c.getGama().getId());
            ps.setInt(7, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new ErrorSistemaException("Error al actualizar el celular", e);
        }
    }

    public void eliminar(int id) {
        SqlHelper.eliminar(con, "DELETE FROM celular WHERE id = ?", id);
    }
}
