/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package Persistencia;

/**
 *
 * @author daniel
 */
public class ConexionDB {
    
    private ConexionDB() {
    }
    
    public static ConexionDB getInstance() {
        return ConexionDBHolder.INSTANCE;
    }
    
    private static class ConexionDBHolder {

        private static final ConexionDB INSTANCE = new ConexionDB();
    }
}
