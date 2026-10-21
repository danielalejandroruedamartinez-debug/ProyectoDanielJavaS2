
package Servicio;

import Excepciones.DatosInvalidosException;
import Modelo.Celular;
import Persistencia.CelularDAO;
import Util.Validador;
import java.util.List;

public class GestorCelulares {

    private final CelularDAO celularDAO;

    public GestorCelulares(CelularDAO celularDAO) {
        this.celularDAO = celularDAO;
    }

    public List<Celular> listar() { return celularDAO.listar(); }

    public List<String> listarMarcas() { return celularDAO.listarMarcas(); }

    public List<String> listarSistemasOperativos() { return celularDAO.listarSistemasOperativos(); }

    public Celular buscarPorId(int id) {
        return celularDAO.buscarPorId(id).orElseThrow(() -> new DatosInvalidosException("El celular no existe"));
    }

    public void registrar(Celular c) {
        
        validar(c);
        celularDAO.guardar(c);
    }

    public void actualizar(Celular c) {
        
        buscarPorId(c.getId());
        validar(c);
        celularDAO.actualizar(c);
    }

    public void eliminar(int id) {
        
        buscarPorId(id);
        celularDAO.eliminar(id);
    }

    private void validar(Celular c) {
        
        c.setModelo(Validador.noVacio(c.getModelo(), "El modelo"));
        Validador.positivoP(c.getPrecio(), "El precio");
        Validador.noNegativo(c.getStock(), "El stock");
        
        if (c.getGama() == null) {
            throw new DatosInvalidosException("La gama es obligatoria");
        }
        
        if (!celularDAO.listarMarcas().contains(c.getMarca())) {
            throw new DatosInvalidosException("La marca no existe");
        }
        
        if (!celularDAO.listarSistemasOperativos().contains(c.getSistemaOperativo())) {
            throw new DatosInvalidosException("El sistema operativo no existe");
        }
    }
}


