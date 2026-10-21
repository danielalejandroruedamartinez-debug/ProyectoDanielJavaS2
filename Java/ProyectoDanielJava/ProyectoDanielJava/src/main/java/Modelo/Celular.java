
package Modelo;

import java.math.BigDecimal;


public class Celular {
    
    private int id;
    private String marca;
    private String modelo;
    private BigDecimal precio;
    private int stock;
    private String sistemaOperativo;
    private CategoriaGama gama;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getSistemaOperativo() {
        return sistemaOperativo;
    }

    public void setSistemaOperativo(String sistemaOperativo) {
        this.sistemaOperativo = sistemaOperativo;
    }

    public CategoriaGama getGama() {
        return gama;
    }

    public void setGama(CategoriaGama gama) {
        this.gama = gama;
    }
    
    public String getNombreCompleto() { return marca + " " + modelo; }


    
    
   
    
    
    
    
    
}
