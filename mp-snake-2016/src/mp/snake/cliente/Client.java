/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp.snake.cliente;

/**
 *
 * @author pablo
 */
public class Client {

    /**
     * Carga la vista de la serpiente para el cliente
     */
    public static void main(String[] args) {
        ViewHandler gestor = new ViewHandler();
        gestor.iniciar();
        
    }
    
}