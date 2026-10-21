
package Vista;

import Controlador.ClienteControlador;
import Controlador.UsuarioControlador;
import Modelo.Persona;
import Modelo.TipoPersona;
import java.util.Map;

public class MenuPrincipal {
    
    private final Consola consola;
    private final UsuarioControlador usuarios;
    private final ClienteControlador clientes;
    private final Map<TipoPersona, Menu> menus;
    
    public MenuPrincipal(Consola consola, UsuarioControlador usuarios, ClienteControlador clientes,
                        Map<TipoPersona, Menu> menus) {
        
        this.consola = consola;
        this.usuarios = usuarios;
        this.clientes = clientes;
        this.menus = menus;
    }
    
    public void iniciar() {
        
        while (true) {
            
            int opcion = consola.pedirOpcion("TECNOSTORE", "Salir", "Iniciar sesion", "Registrarme como cliente");
            
            switch (opcion) {
                case 0 -> {
                    consola.imprimir("Hasta pronto");
                    return;
                }
                case 1 -> consola.ejecutar(this :: iniciarSesion);
                case 2 -> consola.ejecutar(this::registrarCliente);
                default -> consola.imprimir("Opcion no valida");
            }
        }
    }
    
    private void iniciarSesion() {
        
        String correo = consola.leerTexto("Correo: ");
        String contraseña = consola.leerTexto("Contraseña: ");
        Persona usuario = usuarios.login(correo, contraseña);
        consola.imprimir("Bienvenido, " + usuario.getNombre() + " (" + usuario.getTipo() + ")");
        menus.get(usuario.getTipo()).mostrar(usuario);
    }
    
    private void registrarCliente() {
        
        String nombre = consola.leerTexto("Nombre: ");
        String identificacion = consola.leerTexto("Identificación: ");
        String correo = consola.leerTexto("Correo: ");
        String telefono = consola.leerTexto("Teléfono: ");
        String contraseña = consola.leerTexto("Contraseña: ");
        clientes.registrar(nombre, identificacion, correo, telefono, contraseña);
        consola.imprimir("Cuenta creada. Ya puedes iniciar sesión.");
    }
}
