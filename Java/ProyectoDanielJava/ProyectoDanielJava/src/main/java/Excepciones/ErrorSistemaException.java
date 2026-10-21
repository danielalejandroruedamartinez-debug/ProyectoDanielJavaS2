
package Excepciones;


public class ErrorSistemaException extends RuntimeException {
    
    public ErrorSistemaException(String mensaje, Throwable causa){
        
        super(mensaje, causa);
    }
}
