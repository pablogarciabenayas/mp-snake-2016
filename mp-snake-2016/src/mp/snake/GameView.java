/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp.snake;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;

/**
 *
 * @author luisca
 */
public class GameView extends javax.swing.JPanel  implements Observer{
    
        GridLayout layout;
        JPanel[][] referencia;
        Color[] colores={Color.WHITE,Color.BLACK,Color.ORANGE,Color.BLUE,Color.YELLOW};
        
 
    public GameView(int filas, int columnas) {
        initComponents();
        layout =new GridLayout(filas, columnas);
        referencia = new JPanel[filas][columnas];
        gamePanel.setLayout(layout);
        for (int i = 0; i < layout.getRows(); i++) {
            for (int j = 0; j <layout.getColumns(); j++) {
                JPanel p= new JPanel();
                p.setBackground(Color.WHITE);
                referencia[i][j]=p;
                gamePanel.add(p);
                 
            }
            
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gamePanel = new javax.swing.JPanel();

        gamePanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout gamePanelLayout = new javax.swing.GroupLayout(gamePanel);
        gamePanel.setLayout(gamePanelLayout);
        gamePanelLayout.setHorizontalGroup(
            gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 340, Short.MAX_VALUE)
        );
        gamePanelLayout.setVerticalGroup(
            gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 266, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(gamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel gamePanel;
    // End of variables declaration//GEN-END:variables
    void arrancar(){
    setVisible(true);
    }
    void setControlador(Controlador controlador){
        addKeyListener(controlador);
    }

    @Override
    public void update(Observable o, Object arg) {
        //Aqui se pinta la serpiente
        String msgR=(String)arg;
        String[]args= msgR.split(",");
        int x= Integer.parseInt(args[0]);
        int y= Integer.parseInt(args[1]);
        Color color= colores[Integer.parseInt(args[2])];
        referencia[x][y].setBackground(color);
        /*boolean choque =Boolean.parseBoolean(args[3]);
        if(!choque)
        {
            
        }*/
  
    }
}



