package com.kikopolis;

import com.kikopolis.entity.Snake;
import org.slf4j.Logger;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import static com.kikopolis.config.PanelConfig.SCREEN_HEIGHT;
import static com.kikopolis.config.PanelConfig.SCREEN_WIDTH;
import static com.kikopolis.config.PanelConfig.UNIT_SIZE;
import static com.kikopolis.movement.Direction.LEFT;
import static com.kikopolis.movement.Direction.RIGHT;
import static com.kikopolis.movement.Direction.UP;
import static com.kikopolis.movement.Direction.DOWN;
import static org.slf4j.LoggerFactory.getLogger;

public final class GamePanel extends JPanel implements ActionListener {
    private static final Logger LOGGER = getLogger(GamePanel.class);
    private static final int DELAY = 200;
    private final int unitSize;
    private final int panelWidth;
    private final int panelHeight;
    private Snake snake;
    private Timer timer;
    private final Random random;
    private int appleX;
    private int appleY;
    private boolean running;
    
    public GamePanel() {
        super();
        panelWidth = SCREEN_WIDTH.getValue();
        panelHeight = SCREEN_HEIGHT.getValue();
        unitSize = UNIT_SIZE.getValue();
        snake = new Snake();
        appleX = 0;
        appleY = 0;
        running = false;
        random = new Random();
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        startGame();
    }
    
    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        draw(g);
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
    
    private void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    private void draw(final Graphics g) {
        if (running) {
            // Draw grid lines
            for (int i = 0; i < (panelHeight / unitSize); i++) {
                g.setColor(Color.DARK_GRAY);
                g.drawLine(i * unitSize, 0, i * unitSize, panelHeight);
                g.drawLine(0, i * unitSize, panelWidth, i * unitSize);
            }
            // Draw the apple
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, unitSize, unitSize);
            // Draw the snake, starting with the head as a different color and then the body
            final int[] x = snake.getX();
            final int[] y = snake.getY();
            final int bodyPartCount = snake.getBodyPartCount();
            for (int i = 0; i < bodyPartCount; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(new Color(45, 180, 0));
                }
                g.fillRect(x[i], y[i], unitSize, unitSize);
            }
            // Draw the score on the top of the screen
            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            var fontMetrics = getFontMetrics(g.getFont());
            final int applesEaten = snake.getApplesEaten();
            g.drawString(
                    "Score: " + applesEaten,
                    (panelWidth - fontMetrics.stringWidth("Score: " + applesEaten)) / 2,
                    g.getFont().getSize()
                        );
        } else {
            gameOver(g);
        }
    }
    
    private void move() {
        final int[] x = snake.getX();
        final int[] y = snake.getY();
        final int bodyPartCount = snake.getBodyPartCount();
        for (int i = bodyPartCount; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (snake.getDirection()) {
            case UP -> y[0] = y[0] - unitSize;
            case DOWN -> y[0] = y[0] + unitSize;
            case LEFT -> x[0] = x[0] - unitSize;
            case RIGHT -> x[0] = x[0] + unitSize;
            default -> { /*do nothing*/ }
        }
        // Update the snake with its new position
        snake.setX(x);
        snake.setY(y);
    }
    
    private void newApple() {
        appleX = random.nextInt((panelWidth / unitSize)) * unitSize;
        appleY = random.nextInt(panelHeight / unitSize) * unitSize;
    }
    
    private void checkApple() {
        final int[] x = snake.getX();
        final int[] y = snake.getY();
        
        if (x[0] == appleX && y[0] == appleY) {
            final int newBodyPartCount = snake.getBodyPartCount() + 1;
            final int newApplesEaten = snake.getApplesEaten() + 1;
            snake.setBodyPartCount(newBodyPartCount);
            snake.setApplesEaten(newApplesEaten);
            newApple();
        }
    }
    
    private void checkCollisions() {
        final int[] x = snake.getX();
        final int[] y = snake.getY();
        // Check if head collides with body
        final int bodyParts = snake.getBodyPartCount();
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }
        // Check if head touches left border
        if (x[0] < 0) {
            running = false;
        }
        // Check if head touches right border
        if (x[0] > panelWidth) {
            running = false;
        }
        // Check if head touches top border
        if (y[0] < 0) {
            running = false;
        }
        // Check if head touches bottom border
        if (y[0] > panelHeight) {
            running = false;
        }
        
        if (!running) {
            timer.stop();
        }
    }
    
    private void gameOver(final Graphics graphics) {
        graphics.setColor(Color.RED);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 40));
        var fontMetrics1 = getFontMetrics(graphics.getFont());
        final int applesEaten = snake.getApplesEaten();
        graphics.drawString(
                "Score: " + applesEaten,
                (panelWidth - fontMetrics1.stringWidth("Score: " + applesEaten)) / 2,
                graphics.getFont().getSize()
                           );
        graphics.setColor(Color.RED);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 75));
        var fontMetrics2 = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (panelWidth - fontMetrics2.stringWidth("Game Over")) / 2, panelHeight / 2);
    }
    
    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(final KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> {
                    if (snake.getDirection() != RIGHT) {
                        snake.setDirection(LEFT);
                    }
                }
                case KeyEvent.VK_RIGHT -> {
                    if (snake.getDirection() != LEFT) {
                        snake.setDirection(RIGHT);
                    }
                }
                case KeyEvent.VK_UP -> {
                    if (snake.getDirection() != DOWN) {
                        snake.setDirection(UP);
                    }
                }
                case KeyEvent.VK_DOWN -> {
                    if (snake.getDirection() != UP) {
                        snake.setDirection(DOWN);
                    }
                }
                default -> { /*do nothing*/ }
            }
        }
    }
}
