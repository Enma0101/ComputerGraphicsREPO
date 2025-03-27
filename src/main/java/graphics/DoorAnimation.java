package graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;

public class DoorAnimation {
    private JLabel panelPuerta;
    private int puertaY = 0; 
    private Timer animationTimer;
    private Color colorMain;

    public DoorAnimation(Color colorMain) {
        this.colorMain = colorMain;
    }

    public JLabel createDoorPanel(int width, int height) {
        panelPuerta = new JLabel();
        panelPuerta.setBounds(0, puertaY, width, height);
        
        URL doorImageUrl = getClass().getResource("/resources/Imagens/garage-door.jpg");
        if (doorImageUrl != null) {
            ImageIcon doorIcon = new ImageIcon(doorImageUrl);
            Image doorImage = doorIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            panelPuerta.setIcon(new ImageIcon(doorImage));
        } else {
            panelPuerta.setBackground(Color.DARK_GRAY);
            panelPuerta.setOpaque(true);
        }
        
        return panelPuerta;
    }

    public void startDoorAnimation() {
        animationTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (puertaY > -750) { 
                    puertaY -= 5; 
                    panelPuerta.setBounds(0, puertaY, 1200, 750);
                    panelPuerta.repaint();
                } else {
                    animationTimer.stop(); 
                }
            }
        });
        animationTimer.start();
    }

    public BufferedImage createCarbonFiberTexture(int width, int height) {
        BufferedImage texture = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = texture.createGraphics();
        
        // Color base ligeramente más oscuro derivado del color principal
        Color baseColor = new Color(
            Math.max(0, colorMain.getRed() - 50), 
            Math.max(0, colorMain.getGreen() - 50), 
            Math.max(0, colorMain.getBlue() - 50)
        );
        
        // Color de las líneas
        Color lineColor = new Color(
            Math.max(0, baseColor.getRed() - 30), 
            Math.max(0, baseColor.getGreen() - 30), 
            Math.max(0, baseColor.getBlue() - 30)
        );
        
        // Fondo base
        g2d.setColor(baseColor);
        g2d.fillRect(0, 0, width, height);
        
        // Parámetros del patrón de fibra de carbono
        int lineSpacing = 5;
        int lineWidth = 2;
        
        g2d.setColor(lineColor);
        
        // Dibujar líneas diagonales para efecto de fibra de carbono
        for (int y = 0; y < height; y += lineSpacing) {
            // Líneas en un ángulo
            g2d.drawLine(0, y, width, y - height);
            g2d.drawLine(0, y + lineWidth, width, y - height + lineWidth);
            
            // Líneas en el ángulo opuesto
            g2d.drawLine(0, y, width, y + height);
            g2d.drawLine(0, y + lineWidth, width, y + height + lineWidth);
        }
        
        // Añadir un ligero efecto de transparencia
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f);
        g2d.setComposite(alphaComposite);
        
        g2d.dispose();
        return texture;
    }
}