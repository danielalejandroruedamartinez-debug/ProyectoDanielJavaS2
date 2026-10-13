
package Util;

import Excepciones.DatosInvalidosException;
import java.math.BigDecimal;
import java.util.regex.Pattern;


public class Validador {
    
    private static final Pattern CORREO = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    private static final Pattern DIGITOS = Pattern.compile("^\\d+$");
    
    private Validador() {}
    
    public static String noVacio(String valor, String campo) {
        
        if (valor == null || valor.trim().isEmpty()) {
            throw new DatosInvalidosException(campo + " No puede estar vacío ");
        }
        return valor.trim();
    }
    
    public static String correo(String valor) {
        
        String v = noVacio(valor, "El correo");
        
        if (!CORREO.matcher(v).matches()) {
            throw new DatosInvalidosException(" El formato del correo no es valido ");            
        }
        return v;
    }
    
    public static void soloDigitos(String valor, String campo, int min, int max) {
        
        String v = noVacio(valor, campo);
        
        if (!DIGITOS.matcher(v).matches() || v.length() < min || v.length() > max){
            throw new DatosInvalidosException(campo + " Debe tener solo digitos ");
            
        }
    }
        
    public static void positivoP(BigDecimal valor, String campo) {
        
        if (valor ==null || valor.signum() <= 0) {
            throw new DatosInvalidosException(campo + " Debe ser mayor a cero ");
        }
    }
    
    public static void positivo(int valor, String campo) {
        
        if (valor <= 0) {
            throw new DatosInvalidosException(campo + " No puede ser negativo ");
        }
    }
    
    public static void noNegativo(int valor, String campo) {
        
        if (valor < 0) {
            throw new DatosInvalidosException(campo + " No puede ser negativo ");
        }
    }
    
}
