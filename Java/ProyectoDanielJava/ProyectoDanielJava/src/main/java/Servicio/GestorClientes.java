
package Servicio;

import Excepciones.DatosInvalidosException;
import Modelo.Cliente;
import Modelo.TipoPersona;
import Persistencia.ClienteDAO;
import Persistencia.UsuarioDAO;
import Util.Validador;
import java.util.List;

public class GestorClientes {
    
    private final ClienteDAO clienteDAO;
    private final UsuarioDAO usuarioDAO;
    
    public GestorClientes(ClienteDAO clienteDAO, UsuarioDAO usuarioDAO) {
        this.clienteDAO = clienteDAO;
        this.usuarioDAO = usuarioDAO;
    }
    
    public List<Cliente> listar() { return clienteDAO.listar(); }
    
    public Cliente buscarPorId(int idCliente) {
        return clienteDAO.buscarPorId(idCliente)
                .orElseThrow(() -> new DatosInvalidosException("El cliente no existe"));
    }
    
    public void registrar(Cliente c) {
        c.setNombre(Validador.noVacio(c.getNombre(), "El nombre"));
        c.setIdentificacion(Validador.soloDigitos(c.getIdentificacion(), "La identificación", 5, 20));
        c.setCorreo(Validador.correo(c.getCorreo()));
        c.setTelefono(Validador.soloDigitos(c.getTelefono(), "El teléfono", 7, 15));
        c.setContraseña(Validador.noVacio(c.getContraseña(), "La contraseña"));
        
        if (usuarioDAO.existeIdentificacion(c.getIdentificacion())) {
            throw new DatosInvalidosException("Ya existe una persona con esa identificacion");
        }
        
        if (usuarioDAO.existeCorreo(c.getCorreo())) {
            throw new DatosInvalidosException("Ya existe una persona con ese correo");
        }
        c.setTipo(TipoPersona.CLIENTE);
        clienteDAO.guardar(c);
    }
    
    public void actualizar(Cliente c) {
        
        buscarPorId(c.getIdCliente());
        c.setNombre(Validador.noVacio(c.getNombre(), "El nombre"));
        c.setCorreo(Validador.correo(c.getCorreo()));
        c.setTelefono(Validador.soloDigitos(c.getTelefono(), "El telefono", 7, 15));
        
        if (usuarioDAO.existeCorreoDeOtraPersona(c.getCorreo(), c.getId())) {
            throw new DatosInvalidosException("Ya existe una persona con ese correo");            
        }
        clienteDAO.actualizar(c);
    }
    
    public void eliminar(int idCliente) {
        buscarPorId(idCliente);
        clienteDAO.eliminar(idCliente);
    }
}
