package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class LoadGamePanel extends JPanel implements Runnable {
    private final int buttonWidth = 230;
    private final int buttonHeight = 35;
    private String selectedFilePath;
    private JList<String> fileList;
    private DefaultListModel<String> listModel;
    private JButton loadSelectedGame;

    public LoadGamePanel() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(400, 300));
        this.setDoubleBuffered(true);
        this.setOpaque(true);

        listModel = new DefaultListModel<>();
        fileList = new JList<>(listModel);
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(fileList);
        
        loadFileList();

        loadSelectedGame = new JButton("Load Selected World");
        loadSelectedGame.setFont(new Font("Arial", Font.BOLD, 15));
        loadSelectedGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!fileList.isSelectionEmpty()) {
                    selectedFilePath = "saves/" + fileList.getSelectedValue();
                    System.out.println("Selected file: " + selectedFilePath);
                }
            }
        });

        add(scrollPane, BorderLayout.CENTER);
        add(loadSelectedGame, BorderLayout.SOUTH);
    }

    private void loadFileList() {
        File saveFolder = new File("saves");
        if (saveFolder.exists() && saveFolder.isDirectory()) {
            File[] files = saveFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        listModel.addElement(file.getName());
                    }
                }
            }
        }
    }
}
