package com.kikopolis;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;
    private static final int UNIT_SIZE = 25;
    private static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    private static final int DELAY = 200;
    private final Timer timer;
    private final Random random;
    private final int[] snakeX;
    private final int[] snakeY;
    private int bodyParts;
    private int applesEaten;
    private int appleX;
    private int appleY;
    private char direction;
    private boolean running;
    
    public GamePanel() {
        snakeX = new int[GAME_UNITS];
        snakeY = new int[GAME_UNITS];
        bodyParts = 6;
        applesEaten = 0;
        appleX = 0;
        appleY = 0;
        direction = 'R';
        running = false;
        timer = new Timer(DELAY, this);
        random = new Random();
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        startGame();
    }
    
    public void startGame() {
        newApple();
        running = true;
        timer.start();
    }
    
    @Override
    public void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }
    
    public void draw(final Graphics graphics) {
        if (running) {
            // Draw grid lines
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                graphics.setColor(Color.DARK_GRAY);
                graphics.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                graphics.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            // Draw the apple
            graphics.setColor(Color.RED);
            graphics.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            // Draw the snake, starting with the head as a different color and then the body
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    graphics.setColor(Color.GREEN);
                } else {
                    graphics.setColor(new Color(45, 180, 0));
                }
                graphics.fillRect(snakeX[i], snakeY[i], UNIT_SIZE, UNIT_SIZE);
            }
            // Draw the score on the top of the screen
            graphics.setColor(Color.RED);
            graphics.setFont(new Font("Ink Free", Font.BOLD, 40));
            var fontMetrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: " + applesEaten, (SCREEN_WIDTH - fontMetrics.stringWidth("Score: " + applesEaten)) / 2, graphics.getFont().getSize());
        } else {
            gameOver(graphics);
        }
    }
    
    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }
        switch (direction) {
            case 'U' -> snakeY[0] = snakeY[0] - UNIT_SIZE;
            case 'D' -> snakeY[0] = snakeY[0] + UNIT_SIZE;
            case 'L' -> snakeX[0] = snakeX[0] - UNIT_SIZE;
            case 'R' -> snakeX[0] = snakeX[0] + UNIT_SIZE;
            default -> { /*do nothing*/ }
        }
    }
    
    public void newApple() {
        appleX = random.nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }
    
    public void checkApple() {
        if (snakeX[0] == appleX && snakeY[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    
    public void checkCollisions() {
        // Check if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                running = false;
                break;
            }
        }
        // Check if head touches left border
        if (snakeX[0] < 0) {
            running = false;
        }
        // Check if head touches right border
        if (snakeX[0] > SCREEN_WIDTH) {
            running = false;
        }
        // Check if head touches top border
        if (snakeY[0] < 0) {
            running = false;
        }
        // Check if head touches bottom border
        if (snakeY[0] > SCREEN_HEIGHT) {
            running = false;
        }
        
        if (!running) {
            timer.stop();
        }
    }
    
    public void gameOver(final Graphics graphics) {
        graphics.setColor(Color.RED);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 40));
        var fontMetrics1 = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + applesEaten, (SCREEN_WIDTH - fontMetrics1.stringWidth("Score: " + applesEaten)) / 2, graphics.getFont().getSize());
        graphics.setColor(Color.RED);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 75));
        var fontMetrics2 = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (SCREEN_WIDTH - fontMetrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(final KeyEvent event) {
            switch (event.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
                default:
                    // do nothing
            }
        }
    }
}
