
package Modelo;

import java.time.LocalDateTime;


public class Administrador extends Persona {
    
    private int idAdministrador;
    private boolean principal;
    private Integer idAdminCreator;
    private LocalDateTime fechaRegistro;

    public int getIdAdministrador() {
        return idAdministrador;
    }

    public void setIdAdministrador(int idAdministrador) {
        this.idAdministrador = idAdministrador;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public Integer getIdAdminCreator() {
        return idAdminCreator;
    }

    public void setIdAdminCreator(Integer idAdminCreator) {
        this.idAdminCreator = idAdminCreator;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    
    
    
    
    
    
}
