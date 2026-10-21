
package Controlador;

import Modelo.CategoriaGama;
import Modelo.Celular;
import Servicio.GestorCelulares;
import java.math.BigDecimal;
import java.util.List;

public class CelularControlador {
    
    private final GestorCelulares gestor;
    
    public CelularControlador(GestorCelulares gestor) {
        this.gestor = gestor;
    }
    
    public List<Celular> listar() { return gestor.listar(); }
    
    public List<String> listarMarcas() { return gestor.listarMarcas(); }
    
    public List<String> listarSistemasOperativos() { return gestor.listarSistemasOperativos(); }
    
    public void registrar(String marca, String modelo, BigDecimal precio, int stock,
                          String sistemaOperativo, CategoriaGama gama) {
        gestor.registrar(armar(0,marca, modelo, precio, stock, sistemaOperativo, gama));
    }
    
    public void actualizar(int id, String marca, String modelo, BigDecimal precio, int stock,
                           String sistemaOperativo, CategoriaGama gama) {
        gestor.actualizar(armar(id, marca, modelo, precio, stock, sistemaOperativo, gama));
    }
        
    public void eliminar(int id) { gestor.eliminar(id); }
    
    private Celular armar(int id, String marca, String modelo, BigDecimal precio, int Stock,
                          String sistemaOperativo, CategoriaGama gama) {
        
        Celular c = new Celular();
        c.setId(id);
        c.setMarca(marca);
        c.setModelo(modelo);
        c.setPrecio(precio);
        c.setStock(Stock);
        c.setSistemaOperativo(sistemaOperativo);
        c.setGama(gama);
        return c;
    }
}
