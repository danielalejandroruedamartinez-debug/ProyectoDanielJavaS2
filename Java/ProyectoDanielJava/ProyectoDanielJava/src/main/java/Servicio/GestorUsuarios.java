
package Servicio;

import Excepciones.DatosInvalidosException;
import Modelo.Administrador;
import Modelo.Persona;
import Modelo.TipoPersona;
import Modelo.Vendedor;
import Persistencia.UsuarioDAO;
import Util.Validador;
import java.util.List;

public class GestorUsuarios {

    private final UsuarioDAO usuarioDAO;

    public GestorUsuarios(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public Persona login(String correo, String contraseña) {
        Validador.noVacio(correo, "El correo");
        Validador.noVacio(contraseña, "La contraseña");
        Persona persona = usuarioDAO.buscarPorCorreo(correo.trim())
                .orElseThrow(() -> new DatosInvalidosException("Correo o contraseña incorrectos"));
        if (!persona.getContraseña().equals(contraseña)) {
            throw new DatosInvalidosException("Correo o contraseña incorrectos");
        }
        return persona;
    }

    // ---------- vendedores ----------

    public List<Vendedor> listarVendedores() { return usuarioDAO.listarVendedores(); }

    public void registrarVendedor(Vendedor v) {
        validarPersona(v);
        v.setCargo(Validador.noVacio(v.getCargo(), "El cargo"));
        if (v.getFechaIngreso() == null) {
            throw new DatosInvalidosException("La fecha de ingreso es obligatoria");
        }
        v.setTipo(TipoPersona.VENDEDOR);
        usuarioDAO.guardarVendedor(v);
    }

    public void eliminarVendedor(int idVendedor) {
        boolean existe = usuarioDAO.listarVendedores().stream()
                .anyMatch(v -> v.getIdVendedor() == idVendedor);
        if (!existe) {
            throw new DatosInvalidosException("El vendedor no existe");
        }
        usuarioDAO.eliminarVendedor(idVendedor);
    }

    // ---------- administradores ----------

    public List<Administrador> listarAdministradores() { return usuarioDAO.listarAdministradores(); }

    public void crearAdministrador(Administrador actual, Administrador nuevo) {
        exigirPrincipal(actual);
        validarPersona(nuevo);
        nuevo.setTipo(TipoPersona.ADMINISTRADOR);
        nuevo.setPrincipal(false);
        nuevo.setIdAdminCreator(actual.getIdAdministrador());
        usuarioDAO.guardarAdministrador(nuevo);
    }

    public void eliminarAdministrador(Administrador actual, int idAdministrador) {
        exigirPrincipal(actual);
        Administrador objetivo = usuarioDAO.buscarAdministrador(idAdministrador)
                .orElseThrow(() -> new DatosInvalidosException("El administrador no existe"));
        if (objetivo.isPrincipal()) {
            throw new DatosInvalidosException("El administrador principal no se puede eliminar");
        }
        usuarioDAO.eliminarAdministrador(idAdministrador);
    }

    private void exigirPrincipal(Administrador actual) {
        if (actual == null || !actual.isPrincipal()) {
            throw new DatosInvalidosException("Solo el administrador principal puede hacer esto");
        }
    }

    private void validarPersona(Persona p) {
        p.setNombre(Validador.noVacio(p.getNombre(), "El nombre"));
        p.setIdentificacion(Validador.soloDigitos(p.getIdentificacion(), "La identificación", 5, 20));
        p.setCorreo(Validador.correo(p.getCorreo()));
        p.setTelefono(Validador.soloDigitos(p.getTelefono(), "El teléfono", 7, 15));
        p.setContraseña(Validador.noVacio(p.getContraseña(), "La contraseña"));
        if (usuarioDAO.existeIdentificacion(p.getIdentificacion())) {
            throw new DatosInvalidosException("Ya existe una persona con esa identificación");
        }
        if (usuarioDAO.existeCorreo(p.getCorreo())) {
            throw new DatosInvalidosException("Ya existe una persona con ese correo");
        }
    }
}
