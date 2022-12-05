package com.kikopolis;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class GameFrame extends JFrame {
    public GameFrame() {
        add(new GamePanel());
        setTitle("Snake");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }
}
