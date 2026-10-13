
package Util;

import java.math.BigDecimal;
import java.util.Locale;

public class ReporteUtils {
    
    private static final Locale CO = Locale.forLanguageTag("es-CO");
    
    private ReporteUtils() { }
    
    public static String dinero(BigDecimal valor) {
        return String.format(CO, "$%,.2f", valor);
    }
}
