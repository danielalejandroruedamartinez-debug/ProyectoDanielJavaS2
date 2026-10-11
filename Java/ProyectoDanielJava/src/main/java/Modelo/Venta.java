
package Modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Venta {
    
    public static final BigDecimal IVA_POR_DEFECTO = new BigDecimal("19.00");
    
    private int id;
    private Cliente cliente;
    private Vendedor vendedor;
    private LocalDateTime fecha;
    private BigDecimal porcentajeIva = IVA_POR_DEFECTO;
    private final List<ItemVenta> items = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getPorcentajeIva() {
        return porcentajeIva;
    }

    public void setPorcentajeIva(BigDecimal porcentajeIva) {
        this.porcentajeIva = porcentajeIva;
    }
    
    public List<ItemVenta> getItems() { 
        return items; 
    }
    public void agregarItem(ItemVenta item) { items.add(item); }
    
    public BigDecimal getSubtotal() {
        return items.stream().map(ItemVenta :: getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        
    }
    
     public BigDecimal getIva() {
        return getSubtotal().multiply(porcentajeIva).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotal() {
        return getSubtotal().add(getIva());
    }
    
    
    
    
    
}
