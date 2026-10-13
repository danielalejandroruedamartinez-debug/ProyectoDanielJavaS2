
package Controlador;
 
import Modelo.Persona;
import Servicio.GestorUsuarios;


public class UsuarioControlador {
    
    private final GestorUsuarios gestor;
    
    public UsuarioControlador(GestorUsuarios gestor) {
        this.gestor = gestor;
    }
    
    public Persona login(String correo, String contraseña) {
        return gestor.login(correo, contraseña);
    }
}
