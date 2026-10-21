
package Modelo;

import Excepciones.DatosInvalidosException;

public enum CategoriaGama {
    ALTA (1,"Alta"), MEDIA (2,"Media"), BAJA (3,"Baja");
    
    private final int id;
    private final String nombre;
    
    CategoriaGama(int id, String nombre){
        
        this.id =id;
        this.nombre = nombre;
    }
    
    public int getId(){ return id;}
    
    public static CategoriaGama desdeNombre(String nombre){
        
        for (CategoriaGama g : values()){
            if (g.nombre.equalsIgnoreCase(nombre)) return g;
        }
        
        throw new DatosInvalidosException("Gama desconocida: " + nombre);
        
    }
    
    @Override
    public String toString(){return nombre;}
    
}
