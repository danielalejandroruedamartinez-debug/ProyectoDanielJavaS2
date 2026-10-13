
package Vista;

import Controlador.ReporteControlador;
import Modelo.Persona;
import Util.ReporteUtils;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Map;

public class MenuReportes implements Menu {

    private final Consola consola;
    private final ReporteControlador reportes;

    public MenuReportes(Consola consola, ReporteControlador reportes) {
        this.consola = consola;
        this.reportes = reportes;
    }

    @Override
    public void mostrar(Persona usuario) {
        while (true) {
            int opcion = consola.pedirOpcion("REPORTES", "Volver",
                    "Celulares con stock bajo (menos de 5)",
                    "Top 3 de celulares más vendidos",
                    "Ventas totales por mes",
                    "Generar archivo reporte_ventas.txt");
            if (opcion == 0) {
                return;
            }
            switch (opcion) {
                case 1 -> consola.ejecutar(this::mostrarStockBajo);
                case 2 -> consola.ejecutar(this::mostrarTop3);
                case 3 -> consola.ejecutar(this::mostrarVentasPorMes);
                case 4 -> consola.ejecutar(() -> consola.imprimir("Archivo generado en: " + reportes.generarArchivoVentas()));
                default -> consola.imprimir("Opción no válida.");
            }
        }
    }

    private void mostrarStockBajo() {
        if (reportes.celularesStockBajo().isEmpty()) {
            consola.imprimir("Ningún celular tiene stock bajo.");
            return;
        }
        consola.mostrarCelulares(reportes.celularesStockBajo());
    }

    private void mostrarTop3() {
        Map<String, Integer> top = reportes.top3MasVendidos();
        if (top.isEmpty()) {
            consola.imprimir("Todavía no hay ventas registradas.");
            return;
        }
        int puesto = 1;
        for (Map.Entry<String, Integer> e : top.entrySet()) {
            consola.imprimir(puesto++ + ". " + e.getKey() + " - " + e.getValue() + " unidades");
        }
    }

    private void mostrarVentasPorMes() {
        Map<YearMonth, BigDecimal> porMes = reportes.ventasPorMes();
        if (porMes.isEmpty()) {
            consola.imprimir("Todavía no hay ventas registradas.");
            return;
        }
        porMes.forEach((mes, total) -> consola.imprimir(mes + ": " + ReporteUtils.dinero(total)));
    }
}
