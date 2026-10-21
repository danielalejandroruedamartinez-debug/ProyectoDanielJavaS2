
package Modelo;

import Excepciones.DatosInvalidosException;


public enum TipoPersona {
    
    CLIENTE(1), VENDEDOR(2), ADMINISTRADOR(3);
    
    private final int id;
    
    TipoPersona(int id){ this.id =id; }
    
    public int getId(){ return id; }
    
    public static TipoPersona desdeId(int id){
        
        for (TipoPersona t : values()) {
            if (t.id == id) return t;
        }
        
        throw new DatosInvalidosException("Tipo de persona desconocido: " + id);
        
    }
    
}
