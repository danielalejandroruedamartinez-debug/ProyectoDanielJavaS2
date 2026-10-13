
package Vista;

import Excepciones.DatosInvalidosException;
import Excepciones.ErrorSistemaException;
import Excepciones.StockInsuficienteException;
import Modelo.Administrador;
import Modelo.Celular;
import Modelo.Cliente;
import Modelo.ItemVenta;
import Modelo.Vendedor;
import Modelo.Venta;
import Util.ReporteUtils;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Consola {
    
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    private final Scanner scanner = new Scanner(System.in);
    
    
    public void imprimir(String texto) { System.out.println(texto); }
    
    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }
    
    public int leerEntero(String mensaje) {
        
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                imprimir("Ingresa un numero entero valido");
            }
        }
    }
    
    public BigDecimal leerDecimal(String mensaje) {
        
        while (true) {
            try {
                return new BigDecimal(leerTexto(mensaje).replace(',', '.'));
            } catch (NumberFormatException e) {
                imprimir("Ingresa un numero valido.");
            }
        }
    }
    
    public LocalDate leerFecha(String mensaje) {
        
        while (true) {
            try {
                return LocalDate.parse(leerTexto(mensaje + "(aaaa-mm-dd): "));
            } catch (DateTimeParseException e) {
                imprimir("Fecha invalida. Usa el formato aaa-mm-dd.");
            }
        }
    }
    
    public int pedirOpcion(String titulo, String textoCero, String... opciones) {
        
        imprimir("\n===== " + titulo + " =====");
        for (int i = 0; i < opciones.length; i++) {
            imprimir((i + 1) + ". " + opciones[i]);
        }
        imprimir("0. " + textoCero);
        return leerEntero("Elige una opcion: ");
    }
    
    public <T> T elejir(String titulo, List<T> opciones) {
        imprimir(titulo + ":");
        
        for (int i = 0; i < opciones.size(); i++) {
            imprimir(" " + (i + 1) + ". " + opciones.get(i));
        }
        
        while (true) {
            int n = leerEntero("Elije (1-" + opciones.size() + "): ");
            
            if (n >= 1 && n <= opciones.size()) {
                return opciones.get(n - 1);
            }
            imprimir("Opcion no valida.");
        }
    }

    public void ejecutar(Runnable accion) {
        try {
            accion.run();
        } catch (DatosInvalidosException | StockInsuficienteException | ErrorSistemaException e) {
            imprimir("Error: " + e.getMessage());
        }
    }
    
//    public void ejecutar(Runnable accion) {
//        try {
//            accion.run();
//        } catch (DatosInvalidosException | StockInsuficienteException | ErrorSistemaException e) {
//            imprimir("Error: " + e.getMessage());
//            if (e.getCause() != null) {
//                imprimir("   Causa técnica: " + e.getCause().getMessage());
//            }
//        }
//    }
    
    public void mostrarCelulares(List<Celular> lista) {
        
        if (lista.isEmpty()) {
            imprimir("No hay celulares para mostrar.");
            return;
        }
        imprimir(String.format("%-4s %-10s %-18s %18s %6s %-9s %-6s",
                "ID", "Marca", "Modelo", "Precio", "Stock", "S.O", "Gama"));
        
        for (Celular c : lista) {
            
            imprimir(String.format("%-4d %-10s %-18s %18s %6d %-9s %-6s",
                    c.getId(), c.getMarca(), c.getModelo(), ReporteUtils.dinero(c.getPrecio()),
                    c.getStock(), c.getSistemaOperativo(), c.getGama()));
        }
    }
    
    public void mostrarClientes(List<Cliente> lista) {
        
        if (lista.isEmpty()) {
            imprimir("No hay Clientes registrados.");
            return;
        }
        imprimir(String.format("%-4s %-22s %-14s %-26s %-12s", "ID", "Nombre", "Identificación", "Correo", "Teléfono"));
        
        for (Cliente c : lista) {
            imprimir(String.format("%-4d %-22s %-14s %-26s %-12s",
                     c.getIdCliente(), c.getNombre(), c.getIdentificacion(), c.getCorreo(), c.getTelefono()));
        }
    }
    
    public void mostrarVendedores(List<Vendedor> lista) {
        
        if (lista.isEmpty()) {
            imprimir("No hay vendedores registrados.");
            return;
        }
        imprimir(String.format("%-4s %-22s %-14s %-26s %-18s %-10s",
                "ID", "Nombre", "Identificacion", "Correo", "Cargo", "Ingreso"));
        
        for (Vendedor v : lista) {
            imprimir(String.format("%-4d %-22s %-14s %-26s %-18s %-10s",
                    v.getIdVendedor(), v.getNombre(), v.getIdentificacion(), v.getCorreo(),
                    v.getCargo(), v.getFechaIngreso()));
        }
    }
    
    public void mostrarAdministradores(List<Administrador> lista) {
        
        imprimir(String.format("%-4s %-24s %-14s %-26s %-10s", "ID", "Nombre", "Identificación", "Correo", "Principal"));
        
        for (Administrador a : lista) {
            imprimir(String.format("%-4d %-24s %-14s %-26s %-10s",
                    a.getIdAdministrador(), a.getNombre(), a.getIdentificacion(), a.getCorreo(),
                    a.isPrincipal() ? "Si" : "No"));
        }
    }
    
    public void mostrarVentas(List<Venta> lista) {
        if (lista.isEmpty()) {
            imprimir("No hay ventas para mostrar.");
            return;
        }
        imprimir(String.format("%-4s %-17s %-20s %-20s %18s", "ID", "Fecha", "Cliente", "Vendedor", "Total"));
        
        for (Venta v : lista) {
            imprimir(String.format("%-4d %-17s %-20s %-20s %18s",
                    v.getId(), v.getFecha().format(FECHA_HORA), v.getCliente().getNombre(),
                    v.getVendedor().getNombre(), ReporteUtils.dinero(v.getTotal())));
        }
    }
    
    public void mostrarDetalleVenta(Venta v) {
        
        imprimir("\n--- Venta #" + v.getId() + " (" + v.getFecha().format(FECHA_HORA) + ") ---");
        imprimir("Cliente: " + v.getCliente().getNombre() + " | Vendedor: " + v.getVendedor().getNombre());
        for (ItemVenta i : v.getItems()) {
            imprimir(String.format("  %-24s x%-3d %18s %18s",
                    i.getCelular().getNombreCompleto(), i.getCantidad(),
                    ReporteUtils.dinero(i.getPrecioUnitario()), ReporteUtils.dinero(i.getSubtotal())));
        }
        imprimir("Subtotal: " + ReporteUtils.dinero(v.getSubtotal()));
        imprimir("IVA (" + v.getPorcentajeIva().stripTrailingZeros().toPlainString() + "%): "
                + ReporteUtils.dinero(v.getIva()));
        imprimir("TOTAL: " + ReporteUtils.dinero(v.getTotal()));
    }
}
