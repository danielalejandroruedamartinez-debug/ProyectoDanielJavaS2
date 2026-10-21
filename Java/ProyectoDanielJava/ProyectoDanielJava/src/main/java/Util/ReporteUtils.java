
package Util;

import Modelo.Celular;
import Modelo.ItemVenta;
import Modelo.Venta;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public final class ReporteUtils {

    private static final Locale CO = Locale.forLanguageTag("es-CO");

    private ReporteUtils() { }

    public static List<Celular> stockBajo(List<Celular> celulares, int limite) {
        return celulares.stream()
                .filter(c -> c.getStock() < limite)
                .sorted(Comparator.comparingInt(Celular::getStock))
                .collect(Collectors.toList());
    }

    public static Map<String, Integer> masVendidos(List<Venta> ventas, int cuantos) {
        Map<String, Integer> unidadesPorCelular = ventas.stream()
                .flatMap(v -> v.getItems().stream())
                .collect(Collectors.groupingBy(
                        i -> i.getCelular().getNombreCompleto(),
                        Collectors.summingInt(ItemVenta::getCantidad)));

        return unidadesPorCelular.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(cuantos)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> a, LinkedHashMap::new));
    }

    public static Map<YearMonth, BigDecimal> ventasPorMes(List<Venta> ventas) {
        return ventas.stream()
                .collect(Collectors.groupingBy(
                        v -> YearMonth.from(v.getFecha()),
                        TreeMap::new,
                        Collectors.reducing(BigDecimal.ZERO, Venta::getTotal, BigDecimal::add)));
    }

    public static String dinero(BigDecimal valor) {
        return String.format(CO, "$%,.2f", valor);
    }
}
