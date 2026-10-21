
package Controlador;

import Modelo.Administrador;
import Modelo.Persona;
import Modelo.Vendedor;
import Servicio.GestorUsuarios;
import java.time.LocalDate;
import java.util.List;

public class UsuarioControlador {

    private final GestorUsuarios gestor;

    public UsuarioControlador(GestorUsuarios gestor) {
        this.gestor = gestor;
    }

    public Persona login(String correo, String contrasena) {
        return gestor.login(correo, contrasena);
    }

    // ---------- vendedores ----------

    public List<Vendedor> listarVendedores() { return gestor.listarVendedores(); }

    public void registrarVendedor(String nombre, String identificacion, String correo, String telefono,
                                  String contrasena, String cargo, LocalDate fechaIngreso) {
        Vendedor v = new Vendedor();
        v.setNombre(nombre);
        v.setIdentificacion(identificacion);
        v.setCorreo(correo);
        v.setTelefono(telefono);
        v.setContraseña(contrasena);
        v.setCargo(cargo);
        v.setFechaIngreso(fechaIngreso);
        gestor.registrarVendedor(v);
    }

    public void eliminarVendedor(int idVendedor) { gestor.eliminarVendedor(idVendedor); }

    // ---------- administradores ----------

    public List<Administrador> listarAdministradores() { return gestor.listarAdministradores(); }

    public void crearAdministrador(Administrador actual, String nombre, String identificacion,
                                   String correo, String telefono, String contraseña) {
        Administrador a = new Administrador();
        a.setNombre(nombre);
        a.setIdentificacion(identificacion);
        a.setCorreo(correo);
        a.setTelefono(telefono);
        a.setContraseña(contraseña);
        gestor.crearAdministrador(actual, a);
    }

    public void eliminarAdministrador(Administrador actual, int idAdministrador) {
        gestor.eliminarAdministrador(actual, idAdministrador);
    }
}
