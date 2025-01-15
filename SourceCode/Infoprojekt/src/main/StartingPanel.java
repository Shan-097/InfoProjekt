package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JPanel;

public class StartingPanel extends JPanel {    
    public StartingPanel(){
        this.setPreferredSize(null);
        this.setDoubleBuffered(true);
        this.repaint();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        JButton startNewGame = new JButton("Create new World");
        startNewGame.setSize(new Dimension(50, 20));//random values, TO DO: choose better
        startNewGame.setLocation((this.WIDTH - startNewGame.getWidth()) / 2, (this.HEIGHT - startNewGame.getHeight()) / 2);//random values, TO DO: choose better
        this.add(startNewGame);
        g2d.dispose();
    }

}