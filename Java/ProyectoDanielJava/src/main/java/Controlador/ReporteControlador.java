
package Controlador;

import Modelo.Celular;
import Servicio.GestorCelulares;
import Servicio.GestorVentas;
import Util.ArchivoUtils;
import Util.ReporteUtils;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public class ReporteControlador {

    private static final int STOCK_BAJO = 5;
    private static final int TOP = 3;
    private static final String ARCHIVO = "reporte_ventas.txt";

    private final GestorCelulares gestorCelulares;
    private final GestorVentas gestorVentas;

    public ReporteControlador(GestorCelulares gestorCelulares, GestorVentas gestorVentas) {
        this.gestorCelulares = gestorCelulares;
        this.gestorVentas = gestorVentas;
    }

    public List<Celular> celularesStockBajo() {
        return ReporteUtils.stockBajo(gestorCelulares.listar(), STOCK_BAJO);
    }

    public Map<String, Integer> top3MasVendidos() {
        return ReporteUtils.masVendidos(gestorVentas.listar(), TOP);
    }

    public Map<YearMonth, BigDecimal> ventasPorMes() {
        return ReporteUtils.ventasPorMes(gestorVentas.listar());
    }

    public Path generarArchivoVentas() {
        return ArchivoUtils.escribirReporteVentas(gestorVentas.listar(), ARCHIVO);
    }
}
