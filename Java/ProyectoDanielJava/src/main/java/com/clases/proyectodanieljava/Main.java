
package com.clases.proyectodanieljava;

import Modelo.CategoriaGama;
import Modelo.Celular;
import Modelo.ItemVenta;
import Modelo.Venta;
import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        Celular galaxy = new Celular();
        galaxy.setMarca("Samsung");
        galaxy.setModelo("Galaxy S24");

        Celular moto = new Celular();
        moto.setMarca("Motorola");
        moto.setModelo("Moto G14");

        Venta venta = new Venta();
        venta.agregarItem(new ItemVenta(galaxy, 1, new BigDecimal("3500000")));
        venta.agregarItem(new ItemVenta(moto, 2, new BigDecimal("600000")));

        System.out.println("Subtotal: " + venta.getSubtotal());
        System.out.println("IVA:      " + venta.getIva());
        System.out.println("Total:    " + venta.getTotal());
        System.out.println("Gama:     " + CategoriaGama.desdeNombre("media"));
    }
}