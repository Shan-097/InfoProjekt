package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;

public class LoadGamePanel extends JPanel implements Runnable {
    
    /**
     * to be done
     */
    private final int frameRate;

    /**
     * to be done
     */
    private String selectedFilePath;

    /**
     * to be done
     */
    private JList<String> fileList;

    /**
     * to be done
     */
    private DefaultListModel<String> listModel;

    /**
     * to be done
     */
    private JButton loadSelectedGame;

    /**
     * to be done
     */
    private Thread thread;

    /**
     * Constructor of the load game panel.
     * Instantiates the buttons, adds listeners to them and
     * sets important values of the frame.
     * 
     * @param pFR the desired frame rate of the saves menu
     */
    public LoadGamePanel(int pFR) {
        this.frameRate = pFR;

        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(400, 300));
        this.setDoubleBuffered(true);
        this.setOpaque(true);

        listModel = new DefaultListModel<>();
        fileList = new JList<>(listModel);
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(fileList);
        
        loadFileList();

        // add button to go back to menu

        loadSelectedGame = new JButton("Load Selected World");
        loadSelectedGame.setFont(new Font("Arial", Font.BOLD, 15));
        loadSelectedGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!fileList.isSelectionEmpty()) {
                    selectedFilePath = "SourceCode/Infoprojekt/saves/" + fileList.getSelectedValue();
                    System.out.println("Selected file: " + selectedFilePath);
                    App.loadGameScreen(selectedFilePath);
                }
            }
        });

        add(scrollPane, BorderLayout.CENTER);
        add(loadSelectedGame, BorderLayout.SOUTH);
    }

    /**
     * to be done
     */
    private void loadFileList() {
        File saveFolder = new File("SourceCode/Infoprojekt/saves");
        if (saveFolder.exists() && saveFolder.isDirectory()) {
            File[] files = saveFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        listModel.addElement(file.getName());
                    }
                }
            }
        }
    }    

    /**
     * Executed upon starting the thread.
     * Starts the game loop.
     * Controls the maximum frame rate and calls @moveButtons to update their
     * position.
     */
    @Override
    public void run() {
        double frameDisplayTime = 1000000000 / frameRate;
        long lastTime = System.nanoTime();
        long currentTime;
        double delta = 0;

        while (thread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / frameDisplayTime;
            lastTime = currentTime;
            if (delta >= 1) {
                repaint();
                delta--;
            }
        }
    }
}
