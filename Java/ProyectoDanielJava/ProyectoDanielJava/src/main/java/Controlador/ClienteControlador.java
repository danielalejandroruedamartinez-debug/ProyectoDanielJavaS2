
package Controlador;

import Modelo.Cliente;
import Servicio.GestorClientes;
import java.util.List;

public class ClienteControlador {

    private final GestorClientes gestor;

    public ClienteControlador(GestorClientes gestor) {
        this.gestor = gestor;
    }

    public List<Cliente> listar() { return gestor.listar(); }

    public void registrar(String nombre, String identificacion, String correo, String telefono, String contraseña) {
        Cliente c = new Cliente();
        c.setNombre(nombre);
        c.setIdentificacion(identificacion);
        c.setCorreo(correo);
        c.setTelefono(telefono);
        c.setContraseña(contraseña);
        gestor.registrar(c);
    }

    public void actualizar(int idCliente, String nombre, String correo, String telefono) {
        Cliente c = gestor.buscarPorId(idCliente);
        c.setNombre(nombre);
        c.setCorreo(correo);
        c.setTelefono(telefono);
        gestor.actualizar(c);
    }

    public void eliminar(int idCliente) { gestor.eliminar(idCliente); }
}
