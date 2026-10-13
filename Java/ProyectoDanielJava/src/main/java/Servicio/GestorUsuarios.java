
package Servicio;

import Excepciones.DatosInvalidosException;
import Modelo.Persona;
import Persistencia.UsuarioDAO;
import Util.Validador;


public class GestorUsuarios {
    
    private final UsuarioDAO usuarioDAO; 
    
    public GestorUsuarios(UsuarioDAO usuarioDao) {
        this.usuarioDAO = usuarioDao;
    }
    
    public Persona login(String correo, String contraseña) {
        
        Validador.noVacio(correo, "El correo");
        Validador.noVacio(contraseña, "La contraseña");
        
        Persona persona = usuarioDAO.buscarPorCorreo(correo.trim())
                .orElseThrow(() -> new DatosInvalidosException("Correo o contraseña incorrectos"));
        
        if (!persona.getContraseña().equals(contraseña)) {
            throw new DatosInvalidosException("Correo o contraseña incorrecrtos");
        }
        return persona;
    }
}
