package main;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class StartingPanel extends JPanel {    
    public StartingPanel(){
        this.setPreferredSize(null);
        this.setDoubleBuffered(true);
    }
    
    public void update(){

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        //draw
        g2d.dispose();
    }

}