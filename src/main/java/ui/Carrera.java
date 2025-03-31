package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Carrera extends JPanel implements KeyListener {
    private int playerX = 100;
    private int playerY = 500;
    private int speed = 0;
    private Timer gameLoop;

    public Carrera() {
        setPreferredSize(new Dimension(1200, 750));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        
        // Game loop para actualizar la posición
        gameLoop = new Timer(16, new ActionListener() { // ~60 FPS
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                repaint();
            }
        });
        gameLoop.start();
    }

    private void update() {
        playerY -= speed; // Mueve el coche hacia arriba (simula avance)
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Dibujar circuito (simple)
        g2d.setColor(Color.GRAY);
        g2d.fillRect(200, 0, 800, 750); // Pista
        
        // Dibujar coche (rectángulo por ahora)
        g2d.setColor(Color.RED);
        g2d.fillRect(playerX, playerY, 40, 70);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) {
            speed = 5; // Acelerar
        } else if (key == KeyEvent.VK_DOWN) {
            speed = -2; // Frenar
        } else if (key == KeyEvent.VK_LEFT) {
            playerX -= 10; // Girar izquierda
        } else if (key == KeyEvent.VK_RIGHT) {
            playerX += 10; // Girar derecha
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
            speed = 0; // Detener al soltar tecla
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}