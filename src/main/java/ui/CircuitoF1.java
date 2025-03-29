
package ui;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import javax.swing.*;

public class CircuitoF1 extends JPanel {

    private Random random = new Random(123); // Semilla fija para consistencia

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Configuración de gráficos
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar fondo verde (césped)
        dibujarAreasVerdes(g2d);
        
        // Dibujar la cuadrícula con transparencia
        g2d.setColor(new Color(200, 200, 200, 100));
        for(int i = 50 ; i < 700; i+=50) {
            for(int j = 50 ; j< 1200; j+=50) {
                g2d.drawLine(0, i, 1200, i);
                g2d.drawLine(j, 0, j, 700);
            }
        }

        // DIBUJAR EL CIRCUITO CON CURVAS
        Path2D path = new Path2D.Double();

        // Punto inicial
        path.moveTo(350, 650);

        // Tramo inferior
        path.lineTo(850, 650);

        // Curva inferior derecha
        path.curveTo(920, 650, 980, 620, 1000, 550);

        // Tramo derecho vertical
        path.curveTo(1020, 500, 1020, 450, 1020, 400);
        path.lineTo(1020, 220);

        // Curva superior derecha
        path.curveTo(1020, 150, 980, 120, 920, 120);

        // Tramo superior
        path.lineTo(750, 120);

        // Curva superior izquierda central
        path.curveTo(700, 120, 650, 130, 620, 150);
        path.curveTo(590, 170, 580, 180, 550, 180);
        path.lineTo(350, 180);

        // SECCIÓN MODIFICADA - Siguiendo el patrón azul
        // Curva suave desde la sección superior hacia el lado izquierdo
        path.curveTo(300, 180, 250, 170, 210, 140); // Inicio de la curva hacia arriba-izquierda
        path.curveTo(170, 110, 150, 80, 130, 80); // Continúa la curva hacia la izquierda-arriba

        // Tramo vertical izquierdo superior
        path.curveTo(110, 80, 100, 100, 100, 130); // Curva hacia abajo
        path.lineTo(100, 550); // Tramo vertical largo

        // Curva inferior izquierda
        path.curveTo(100, 610, 150, 650, 200, 650);

        // Cierre al punto inicial
        path.lineTo(350, 650);

        // Dibujar el borde blanco del circuito
        g2d.setStroke(new BasicStroke(84));
        g2d.setColor(Color.WHITE);
        g2d.draw(path);
        
        // Dibujar el asfalto del circuito
        g2d.setStroke(new BasicStroke(80));
        g2d.setColor(Color.DARK_GRAY);
        g2d.draw(path);
        
        // Dibujar la línea central del circuito
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, new float[] {10, 15}, 0));
        g2d.setColor(Color.WHITE);
        g2d.draw(path);
    }
    
    private void dibujarAreasVerdes(Graphics2D g2d) {
        // Fondo verde base (césped)
        Rectangle2D fondo = new Rectangle2D.Double(0, 0, 1200, 700);
        GradientPaint gradient = new GradientPaint(0, 0, new Color(34, 139, 34), // Verde bosque
                                                  0, 700, new Color(50, 205, 50)); // Verde lima
        g2d.setPaint(gradient);
        g2d.fill(fondo);
        
        // Dibujar variaciones de tonos verdes para simular césped
        g2d.setColor(new Color(0, 100, 0, 30)); // Verde oscuro semitransparente
        
        for (int i = 0; i < 1000; i++) {
            int x = random.nextInt(1200);
            int y = random.nextInt(700);
            int size = random.nextInt(40) + 10;
            
            // Dibujar manchas de césped con diferentes tonalidades
            if (random.nextBoolean()) {
                g2d.setColor(new Color(34, 139, 34, 40)); // Verde bosque
            } else {
                g2d.setColor(new Color(0, 100, 0, 30)); // Verde oscuro
            }
            
            g2d.fillOval(x, y, size, size);
        }
        
        // Agregar algunas áreas de césped más claras (como si estuvieran cortadas recientemente)
        g2d.setColor(new Color(144, 238, 144, 40)); // Verde claro semitransparente
        for (int i = 0; i < 200; i++) {
            int x = random.nextInt(1200);
            int y = random.nextInt(700);
            int size = random.nextInt(100) + 50;
            
            g2d.fillOval(x, y, size, size);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Circuito Curvo de F1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 750);
        frame.setLocationRelativeTo(null);
        frame.add(new CircuitoF1());
        frame.setVisible(true);
    }
}