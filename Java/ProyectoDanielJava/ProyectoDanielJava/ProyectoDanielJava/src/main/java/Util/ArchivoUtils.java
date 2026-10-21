
package Util;

import Excepciones.ErrorSistemaException;
import Modelo.ItemVenta;
import Modelo.Venta;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class ArchivoUtils {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private ArchivoUtils() { }

    public static Path escribirReporteVentas(List<Venta> ventas, String nombreArchivo) {
        Path ruta = Paths.get(nombreArchivo);
        try (BufferedWriter out = Files.newBufferedWriter(ruta, StandardCharsets.UTF_8)) {
            out.write("REPORTE DE VENTAS - TECNOSTORE");
            out.newLine();
            out.write("Generado: " + LocalDateTime.now().format(FORMATO_FECHA));
            out.newLine();
            out.write("==================================================");
            out.newLine();

            BigDecimal totalGeneral = BigDecimal.ZERO;
            for (Venta v : ventas) {
                out.write("Venta #" + v.getId() + " | " + v.getFecha().format(FORMATO_FECHA));
                out.newLine();
                out.write("  Cliente:  " + v.getCliente().getNombre());
                out.newLine();
                out.write("  Vendedor: " + v.getVendedor().getNombre());
                out.newLine();
                for (ItemVenta i : v.getItems()) {
                    out.write("    - " + i.getCelular().getNombreCompleto() + " x" + i.getCantidad()
                            + " a " + ReporteUtils.dinero(i.getPrecioUnitario()));
                    out.newLine();
                }
                out.write("  Subtotal: " + ReporteUtils.dinero(v.getSubtotal()));
                out.newLine();
                out.write("  IVA (" + v.getPorcentajeIva().stripTrailingZeros().toPlainString() + "%): "
                        + ReporteUtils.dinero(v.getIva()));
                out.newLine();
                out.write("  TOTAL:    " + ReporteUtils.dinero(v.getTotal()));
                out.newLine();
                out.write("--------------------------------------------------");
                out.newLine();
                totalGeneral = totalGeneral.add(v.getTotal());
            }
            out.write("Ventas registradas: " + ventas.size());
            out.newLine();
            out.write("TOTAL GENERAL: " + ReporteUtils.dinero(totalGeneral));
            out.newLine();
        } catch (IOException e) {
            throw new ErrorSistemaException("No se pudo escribir el archivo " + nombreArchivo, e);
        }
        return ruta.toAbsolutePath();
    }
}
