
package Modelo;

import java.time.LocalDateTime;


public class Cliente extends Persona {
    
    private int idCliente;
    private LocalDateTime fechaRegistro;

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    
    }
    
   
            
    
    

