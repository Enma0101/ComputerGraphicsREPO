package ui;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Random;

import javax.swing.*;

import graphics.CocheF1;

public class CircuitoF1 extends JPanel {
    private JLabel labelAnuncios;
    
    // Atributos para el coche F1
    private CocheF1 cocheF1;
    private Path2D pistaBorde;
    private Path2D pistaInterior;
    private Timer timer;
    private boolean teclaArribaPresionada;
    private boolean teclaAbajoPresionada;
    private boolean teclaIzquierdaPresionada;
    private boolean teclaDerechaPresionada;
    
    public CircuitoF1() {
        // Inicializar coche en un punto de partida adecuado
        cocheF1 = new CocheF1(350, 600);
        
        // Crear la pista (se rellenará en paintComponent)
        pistaBorde = new Path2D.Double();
        pistaInterior = new Path2D.Double();
        
        // Inicializar el timer para actualizar la animación
        timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarJuego();
                repaint();
            }
        });
        timer.start();
        
        // Configurar detección de teclas
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        teclaArribaPresionada = true;
                        break;
                    case KeyEvent.VK_DOWN:
                        teclaAbajoPresionada = true;
                        break;
                    case KeyEvent.VK_LEFT:
                        teclaIzquierdaPresionada = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        teclaDerechaPresionada = true;
                        break;
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        teclaArribaPresionada = false;
                        break;
                    case KeyEvent.VK_DOWN:
                        teclaAbajoPresionada = false;
                        break;
                    case KeyEvent.VK_LEFT:
                        teclaIzquierdaPresionada = false;
                        break;
                    case KeyEvent.VK_RIGHT:
                        teclaDerechaPresionada = false;
                        break;
                    case KeyEvent.VK_SPACE:
                        // Reiniciar posición del coche
                        cocheF1.setPosition(350, 600);
                        cocheF1.detener();
                        break;
                }
            }
        });
    }
    
    private void actualizarJuego() {
        // Aplicar controles
        if (teclaArribaPresionada) {
            cocheF1.acelerar();
        }
        if (teclaAbajoPresionada) {
            cocheF1.frenar();
        }
        if (teclaIzquierdaPresionada) {
            cocheF1.girarIzquierda();
        }
        if (teclaDerechaPresionada) {
            cocheF1.girarDerecha();
        }
        
        // Actualizar física del coche
        cocheF1.actualizar();
        
        // Comprobar colisiones con los bordes de la pista
        cocheF1.comprobarColision(pistaBorde, pistaInterior);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Configuración de gráficos
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar fondo verde (césped)
        dibujarAreasVerdes(g2d);
        
        // Dibujar la cuadrícula con transparencia
        g2d.setColor(new Color(200, 200, 200, 100));
        for(int i = 50 ; i < 750; i+=50) {
            for(int j = 50 ; j< 1200; j+=50) {
                g2d.drawLine(0, i, 1200, i);
                g2d.drawLine(j, 0, j, 750);
            }
        }
    
        g2d.setStroke(new BasicStroke(8)); 
        g2d.setColor(new Color(200, 204, 206)); 
        g2d.drawLine(850,610,1000,500);
        g2d.drawLine(600,215,800,150);
        
        
        g2d.setStroke(new BasicStroke(2)); 
        g2d.setColor(new Color(0,0,0)); 
        g2d.drawLine(852,612,1002,502);g2d.drawLine(848,608,998,498);
        g2d.drawLine(602,217,802,152);g2d.drawLine(598,213,798,148);
        
        // 3. Zona de boxes
        dibujarZonaBoxesCompleta(g2d, 280, 60, 300, 80);
        
        
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
        
       

        // 4. Bordes rojos y blancos
        g2d.setStroke(new BasicStroke(90));
        g2d.setColor(Color.WHITE);
        g2d.draw(path);

        float[] dashPattern = {20, 20};
        g2d.setStroke(new BasicStroke(90, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, dashPattern, 0));
        g2d.setColor(Color.RED);
        g2d.draw(path);
        
     // 2. Dibujar asfalto del circuito
        g2d.setStroke(new BasicStroke(80));
        g2d.setColor(Color.DARK_GRAY);
        g2d.draw(path);

        
        // Dibujar la línea central del circuito
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, new float[] {10, 15}, 0));
        g2d.setColor(Color.WHITE);
        g2d.draw(path);
        
        

        g2d.setStroke(new BasicStroke(5)); // Grosor del borde
        g2d.setColor(Color.RED); // Color de los círculos
        dibujarCirculosZona(g2d, 945, 190, 30, 100, 5, 2);
        g2d.setStroke(new BasicStroke(5)); // Grosor del borde
        g2d.setColor(Color.white); // Color de los círculos
        dibujarCirculosZona(g2d, 945, 410, 30, 90, 5, 2);
        
        // Banner en el lado 
        dibujarBannerVertical(g2d, 170, 350, 150);
        
        dibujarBannerVertical(g2d, 950, 345, 100);
        
        dibujarTribuna(g2d, 1100, 0, 70, 750, 25);
        
    
        dibujarZonaBoxesCompleta(g2d, 285, 62, 280, 80);
        
        // Crear los paths de la pista para la detección de colisiones
        pistaBorde = new Path2D.Double();
        pistaInterior = new Path2D.Double();
        
        // Usar el mismo path que ya definiste para la pista, pero con diferentes tamaños
        // Para el borde exterior (más grande que la pista visual)
        Path2D pathExterior = new Path2D.Double();
        pathExterior.moveTo(350, 650);
        pathExterior.lineTo(850, 650);
        pathExterior.curveTo(920, 650, 980, 620, 1000, 550);
        pathExterior.curveTo(1020, 500, 1020, 450, 1020, 400);
        pathExterior.lineTo(1020, 220);
        pathExterior.curveTo(1020, 150, 980, 120, 920, 120);
        pathExterior.lineTo(750, 120);
        pathExterior.curveTo(700, 120, 650, 130, 620, 150);
        pathExterior.curveTo(590, 170, 580, 180, 550, 180);
        pathExterior.lineTo(350, 180);
        pathExterior.curveTo(300, 180, 250, 170, 210, 140);
        pathExterior.curveTo(170, 110, 150, 80, 130, 80);
        pathExterior.curveTo(110, 80, 100, 100, 100, 130);
        pathExterior.lineTo(100, 550);
        pathExterior.curveTo(100, 610, 150, 650, 200, 650);
        pathExterior.lineTo(350, 650);
        
        // Para el borde interior (más pequeño que la pista visual)
        Path2D pathInterior = new Path2D.Double();
        pathInterior.moveTo(350, 610);
        pathInterior.lineTo(820, 610);
        pathInterior.curveTo(870, 610, 920, 590, 940, 550);
        pathInterior.curveTo(960, 500, 960, 450, 960, 400);
        pathInterior.lineTo(960, 240);
        pathInterior.curveTo(960, 190, 920, 160, 870, 160);
        pathInterior.lineTo(750, 160);
        pathInterior.curveTo(710, 160, 670, 170, 640, 190);
        pathInterior.curveTo(610, 210, 600, 220, 550, 220);
        pathInterior.lineTo(370, 220);
        pathInterior.curveTo(330, 220, 290, 210, 250, 180);
        pathInterior.curveTo(210, 150, 190, 120, 170, 120);
        pathInterior.curveTo(150, 120, 140, 140, 140, 170);
        pathInterior.lineTo(140, 530);
        pathInterior.curveTo(140, 570, 180, 610, 220, 610);
        pathInterior.lineTo(350, 610);
        
        // Crear áreas para los bordes
        Area areaExterior = new Area(new BasicStroke(85).createStrokedShape(pathExterior));
        Area areaInterior = new Area(new BasicStroke(35).createStrokedShape(pathInterior));
        
        // Convertir las áreas a paths
        pistaBorde = new Path2D.Double();
        pistaBorde.append(areaExterior.getPathIterator(null), false);
        
        pistaInterior = new Path2D.Double();
        pistaInterior.append(areaInterior.getPathIterator(null), false);
        
        // Para depuración, podemos dibujar los bordes de colisión
        /*
        g2d.setColor(new Color(255, 0, 0, 50));
        g2d.fill(pistaBorde);
        g2d.setColor(new Color(0, 255, 0, 50));
        g2d.fill(pistaInterior);
        */
        
        // Dibujar el coche
        cocheF1.dibujar(g2d);
    }
		    
		    private void dibujarCirculosZona(Graphics2D g2d, int xInicio, int yInicio, int anchoArea, int altoArea, int radio, int espacio) {
		     

		        // Calcular cuántos círculos caben en el área
		        int filas = altoArea / (radio*2 + espacio);
		        int columnas = anchoArea / (radio*2 + espacio);

		        // Dibujar en grid
		        for (int fila = 0; fila < filas; fila++) {
		        	
		            for (int col = 0; col < columnas; col++) {
		          
		                int x = xInicio + col * (radio*2 + espacio);
		                int y = yInicio + fila * (radio*2 + espacio);
		                g2d.drawOval(x, y, radio*2, radio*2);
		            }
		        }	 
	 }
    
    private void dibujarAreasVerdes(Graphics2D g2d) {
        // Fondo verde base (césped)
        Rectangle2D fondo = new Rectangle2D.Double(0, 0, 1200, 750);
        g2d.setColor(new Color(124,252,0)); 
        g2d.fill(fondo);
        
      
    }
    

    private void dibujarBannerVertical(Graphics2D g2d, int centroX, int centroY, int altura) {
        // Guardar transformación original
        AffineTransform originalTransform = g2d.getTransform();
        
        // Dimensiones del banner
        int ancho = 40;
        int radioEsquina = 10;
        
        // 1. Fondo con gradiente amarillo Pirelli
        GradientPaint gradiente = new GradientPaint(
            centroX, centroY - altura/2, new Color(255, 215, 0),
            centroX, centroY + altura/2, new Color(255, 165, 0));
        g2d.setPaint(gradiente);
        g2d.fillRoundRect(centroX - ancho/2, centroY - altura/2, ancho, altura, radioEsquina, radioEsquina);
        
        // 2. Borde negro
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(centroX - ancho/2, centroY - altura/2, ancho, altura, radioEsquina, radioEsquina);
        
        // 3. Configurar texto
        Font font = new Font("Arial", Font.BOLD, ajustarTamañoFuente(altura));
        g2d.setFont(font);
        String texto = "PIRELLI";
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(texto);
        
        // 4. Rotar y posicionar texto correctamente
        g2d.rotate(Math.toRadians(-90), centroX, centroY); // Rotar 90° antihorario
        
        // Calcular posición para que el texto quede centrado y dentro del banner
        int textX = centroX - altura/2 + (altura - textWidth)/2;
        int textY = centroY + fm.getAscent()/2;
        
        // Sombra del texto
        g2d.setColor(new Color(0, 0, 0, 80));
        g2d.drawString(texto, textX + 2, textY + 2);
        
        // Texto principal
        g2d.setColor(Color.BLACK);
        g2d.drawString(texto, textX, textY);
        
        // Restaurar transformación
        g2d.setTransform(originalTransform);
  
    }

    private int ajustarTamañoFuente(int altura) {
        // Ajuste más conservador para asegurar que el texto quepa
        int tamañoBase = altura / 5;
        return Math.max(12, Math.min(18, tamañoBase)); // Limitar entre 12 y 18
    }



    public static void main(String[] args) {
        JFrame frame = new JFrame("Circuito Curvo de F1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 750);
        frame.setLocationRelativeTo(null);
        frame.add(new CircuitoF1());
        frame.setVisible(true);
        
    }
    
    
    private void dibujarTribuna(Graphics2D g2d, int x, int y, int ancho, int alto, int niveles) {
        // 1. Ajustar posición para margen superior
        int yAjustado = y ;
        
        // 2. Dibujar estructura principal de las gradas
        g2d.setColor(new Color(220, 220, 220));
        g2d.fillRect(x, yAjustado, ancho, alto);

        // 3. TORRE VERTICAL DEL TECHO (estructura metálica)
        int anchoTorre = 50;
        int alturaTorre = alto + 150; // Más alta que las gradas
        
        // Torre principal
        GradientPaint gradienteTorre = new GradientPaint(
            x + ancho, yAjustado, new Color(100, 100, 100),
            x + ancho + anchoTorre, yAjustado, new Color(60, 60, 60));
        g2d.setPaint(gradienteTorre);
        g2d.fillRect(1150, yAjustado - 50, anchoTorre, alturaTorre);
        
        // Refuerzos diagonales (vigas en X)
        g2d.setColor(new Color(80, 80, 80));
        g2d.setStroke(new BasicStroke(4));
        for(int i = 0; i < 4; i++) {
            int yRefuerzo = yAjustado + i * (alturaTorre/3);
            g2d.drawLine(x + ancho, yRefuerzo, x + ancho + anchoTorre, yRefuerzo + 50);
            g2d.drawLine(x + ancho, yRefuerzo + 50, x + ancho + anchoTorre, yRefuerzo);
        }
        
        // 4. TECHO VERTICAL (marquesina)
        int anchoMarquesina = 120;
        int altoMarquesina = 40;
        
        // Sombra de la marquesina
        g2d.setColor(new Color(30, 30, 30, 150));
        g2d.fillRect(x + ancho + anchoTorre, yAjustado - 30, anchoMarquesina, altoMarquesina);
        
     
        g2d.setColor(new Color(150, 150, 150));
        g2d.fillRect(x + ancho + anchoTorre, yAjustado - 30, anchoMarquesina, 5);
        
        g2d.fillRect(x + ancho + anchoTorre + anchoMarquesina, yAjustado - 30, 10, altoMarquesina + 30);
        
        
        g2d.setColor(new Color(200, 230, 255, 120));
        g2d.fillRect(x + ancho + anchoTorre, yAjustado - 30, anchoMarquesina, altoMarquesina);
        
        // 5. Niveles de asientos con espectadores
        int alturaNivel = alto / niveles;
        for(int i = 0; i < niveles; i++) {
            int yNivel = yAjustado + i * alturaNivel;
            
            // Barandilla
            g2d.setColor(new Color(50, 50, 50));
            g2d.fillRect(x, yNivel, ancho, 5);
            
            // Asientos
            g2d.setColor(new Color(190, 190, 190));
            g2d.fillRect(x + 5, yNivel + 3, ancho - 10, alturaNivel - 3);
            
            // Espectadores (simplificado)
            dibujarEspectadores(g2d, x + 5, yNivel + 3, ancho - 10, alturaNivel - 3, 35);
        }
        
        // 6. Iluminación (opcional)
        g2d.setColor(new Color(255, 255, 150, 200));
        g2d.fillOval(x + ancho + anchoTorre + anchoMarquesina - 15, yAjustado - 40, 20, 20);
    }
    

    private void dibujarEspectadores(Graphics2D g2d, int x, int y, int ancho, int alto, int densidad) {
        Random rand = new Random();
        int tamPersona = Math.max(4, alto/8);
        
        for (int i = 0; i < densidad; i++) {
            int px = x + rand.nextInt(ancho - tamPersona);
            int py = y + rand.nextInt(alto - tamPersona);
            
            // Cabeza
            g2d.setColor(new Color(rand.nextInt(40)+190, rand.nextInt(40)+160, rand.nextInt(40)+120));
            g2d.fillOval(px, py, tamPersona, tamPersona);
            
            // Cuerpo (usando degradado para camisetas)
            GradientPaint camiseta = new GradientPaint(
                px, py, new Color(rand.nextInt(200)+55, rand.nextInt(200)+55, rand.nextInt(200)+55),
                px, py+tamPersona, new Color(rand.nextInt(200)+55, rand.nextInt(200)+55, rand.nextInt(200)+55));
            g2d.setPaint(camiseta);
            g2d.fillRect(px-tamPersona/4, py+tamPersona, tamPersona/2, tamPersona);
        }
    }
    private void dibujarZonaBoxesCompleta(Graphics2D g2d, int x, int y, int ancho, int alto) {
        // Estructura principal
        g2d.setColor(new Color(60, 60, 60));
        g2d.fillRect(x, y, ancho, alto);
        
        // Techumbre moderna
        g2d.setColor(new Color(30, 30, 30, 200));
        g2d.fillRect(x-15, y-25, ancho+30, 25);
        
        // Columnas futuristas
        g2d.setColor(new Color(150, 150, 150));
        for(int i = 0; i < 6; i++) {
            int colX = x + i*(ancho/5);
            g2d.fillRoundRect(colX-3, y-25, 6, 25, 5, 5);
        }
        
        // Puertas de garaje
        g2d.setStroke(new BasicStroke(2));
        int numBoxes = 10;
        for(int i = 0; i < numBoxes; i++) {
            int boxX = x + i*(ancho/numBoxes);
            
            // Puerta metálica
            g2d.setColor(new Color(180, 180, 180));
            g2d.fillRect(boxX+2, y+5, (ancho/numBoxes)-4, alto-10);
            
            // Detalles de puerta
            g2d.setColor(new Color(100, 100, 100));
            g2d.drawRect(boxX+2, y+5, (ancho/numBoxes)-4, alto-10);
            g2d.drawLine(boxX+(ancho/numBoxes)/2, y+5, boxX+(ancho/numBoxes)/2, y+alto-5);
        }
        
        // Logos y señalización
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("PITS", x+ancho/2-15, y+alto/2+5);
    }
    
    
    
    }