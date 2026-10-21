
package Modelo;

import java.math.BigDecimal;

public class ItemVenta {
    
    private final Celular celular;
    private final int cantidad;
    private final BigDecimal precioUnitario;

    public ItemVenta(Celular celular, int cantidad, BigDecimal precioUnitario) {
        this.celular = celular;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public Celular getCelular() {
        return celular;
    }

    public int getCantidad() {
        return cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    
    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
