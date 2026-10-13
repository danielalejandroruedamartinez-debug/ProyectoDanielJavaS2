
package com.clases.proyectodanieljava;

import Controlador.CelularControlador;
import Controlador.ClienteControlador;
import Controlador.ReporteControlador;
import Controlador.UsuarioControlador;
import Controlador.VentaControlador;
import Excepciones.ErrorSistemaException;
import Modelo.TipoPersona;
import Persistencia.CelularDAO;
import Persistencia.ClienteDAO;
import Persistencia.ConexionDB;
import Persistencia.UsuarioDAO;
import Persistencia.VentaDAO;
import Servicio.GestorCelulares;
import Servicio.GestorClientes;
import Servicio.GestorUsuarios;
import Servicio.GestorVentas;
import Vista.Consola;
import Vista.Menu;
import Vista.MenuAdministrador;
import Vista.MenuCliente;
import Vista.MenuPrincipal;
import Vista.MenuReportes;
import Vista.MenuVendedor;
import java.sql.Connection;
import java.util.EnumMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        try {
            Connection con = ConexionDB.getInstancia().getConexion();

            // Persistencia
            CelularDAO celularDAO = new CelularDAO(con);
            ClienteDAO clienteDAO = new ClienteDAO(con);
            UsuarioDAO usuarioDAO = new UsuarioDAO(con);
            VentaDAO ventaDAO = new VentaDAO(con);

            // Servicio
            GestorCelulares gestorCelulares = new GestorCelulares(celularDAO);
            GestorClientes gestorClientes = new GestorClientes(clienteDAO, usuarioDAO);
            GestorUsuarios gestorUsuarios = new GestorUsuarios(usuarioDAO);
            GestorVentas gestorVentas = new GestorVentas(ventaDAO, celularDAO, clienteDAO);

            // Controlador
            CelularControlador celularControlador = new CelularControlador(gestorCelulares);
            ClienteControlador clienteControlador = new ClienteControlador(gestorClientes);
            UsuarioControlador usuarioControlador = new UsuarioControlador(gestorUsuarios);
            VentaControlador ventaControlador = new VentaControlador(gestorVentas);
            ReporteControlador reporteControlador = new ReporteControlador(gestorCelulares, gestorVentas);

            // Vista
            Consola consola = new Consola();
            Menu menuReportes = new MenuReportes(consola, reporteControlador);

            Map<TipoPersona, Menu> menus = new EnumMap<>(TipoPersona.class);
            menus.put(TipoPersona.ADMINISTRADOR, new MenuAdministrador(consola, celularControlador,
                    clienteControlador, ventaControlador, menuReportes));
            menus.put(TipoPersona.VENDEDOR, new MenuVendedor(consola, celularControlador,
                    clienteControlador, ventaControlador, menuReportes));
            menus.put(TipoPersona.CLIENTE, new MenuCliente(consola, celularControlador, ventaControlador));

            new MenuPrincipal(consola, usuarioControlador, clienteControlador, menus).iniciar();
        } catch (ErrorSistemaException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            ConexionDB.getInstancia().cerrar();
        }
    }
}