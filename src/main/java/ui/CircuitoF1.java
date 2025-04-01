package ui;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.util.Random;

import javax.swing.*;

import graphics.CocheF1;
import main.Main;

public class CircuitoF1 extends JPanel {
   
    
    // Atributos para el coche F1
    private CocheF1 cocheF1;
    private Path2D pistaBorde;
    private Path2D pistaInterior;
    private Path2D zonaPits;
    private Timer timer;
    private boolean teclaArribaPresionada;
    private boolean teclaAbajoPresionada;
    private boolean teclaIzquierdaPresionada;
    private boolean teclaDerechaPresionada;
    private Area areaPista;
    private int penalizaciones = 0;
    private int segundosPenalizacion = 0;
    private boolean mostrandoPenalizacion = false;
    private Timer timerPenalizacion;
    private boolean carreraFinalizada = false;
    private long ultimoChoque = 0;
    private static final int VUELTAS_PARA_FINALIZAR = 5;
    private static final int PENALIZACION_SEGUNDOS = 5;
    private boolean haCruzado = false;
    private Color ColorPricipal = new Color(188, 24, 35);
    private int vueltas = 0; 
    private int segundos = 0; 
    private int milisegundos = 0;
    private int minutos = 0; 
    private Timer temporizador;
    private Timer timerTiempo;
    

    private Path2D lineaMeta;
    private Area areaLineaMeta;
  
    public CircuitoF1() {
    	
        JButton botonCerrar = new JButton("Back");
        setLayout(null);
        botonCerrar.setBounds(0, 0, 95, 40);
        botonCerrar.setFont(Main.GLOBAL_FONT.deriveFont(15f));
        botonCerrar.setForeground(Color.WHITE);
        botonCerrar.setBackground(Color.RED);
        botonCerrar.setFocusable(false);
        add(botonCerrar);
        

        botonCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	mostrarConfirmacion();


            }
        });

    	
    
    	this.areaPista = calcularArea();
    	
        // Inicializar coche en un punto de partida adecuado (línea de meta)
        cocheF1 = new CocheF1(1000, 450, this);
        
        
        lineaMeta = crearLineaMeta();
        areaLineaMeta = new Area(lineaMeta);
        
        // Crear la pista (se rellenará en paintComponent)
        pistaBorde = new Path2D.Double();
        pistaInterior = new Path2D.Double();
        zonaPits = new Path2D.Double();
     

        // Inicializar el timer para actualizar la animación
        timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!carreraFinalizada) {
                    cocheF1.actualizar();
                    
                    // Verificar si ha cruzado la meta
                    Point2D posicionCoche = new Point2D.Double(cocheF1.getX(), cocheF1.getY());
                    if (haCruzadoMeta(posicionCoche)) {
                        if (!haCruzado) {
                            vueltas++;
                            haCruzado = true;
                            
                            // Verificar si ha completado las vueltas necesarias
                            if (vueltas >= VUELTAS_PARA_FINALIZAR) {
                                finalizarCarrera();
                            }
                        }
                    } else {
                        haCruzado = false;
                    }
                    
                    // Verificar colisiones con el borde de la pista
                    verificarColisiones(posicionCoche);
                }
                repaint();
            }
        });
        timer.start();
        
        temporizador = new Timer(10, new ActionListener() { // Cada 10 ms
            @Override
            public void actionPerformed(ActionEvent e) {
                milisegundos += 10; // Incrementar milisegundos

                // Convertir milisegundos a segundos
                if (milisegundos >= 1000) {
                    milisegundos = 0;
                    segundos++;
                }

                // Convertir segundos a minutos
                if (segundos >= 60) {
                    segundos = 0;
                    minutos++;
                }

                repaint(); // Redibujar la pantalla
            }
        });
        temporizador.start();

        
     
        // Configurar detección de teclas
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:    // Flecha arriba
                    case KeyEvent.VK_W:     // Tecla W
                        teclaArribaPresionada = true;
                        cocheF1.acelerar();
                        break;
                        
                    case KeyEvent.VK_SPACE:  // Flecha abajo
                    case KeyEvent.VK_S:     // Tecla S
                        teclaAbajoPresionada = true;
                        cocheF1.frenar();
                        break;
                        
                    case KeyEvent.VK_LEFT:  // Flecha izquierda
                    case KeyEvent.VK_A:     // Tecla A
                        teclaIzquierdaPresionada = true;
                        cocheF1.girarIzquierda();
                        break;
                        
                    case KeyEvent.VK_RIGHT: // Flecha derecha
                    case KeyEvent.VK_D:     // Tecla D
                        teclaDerechaPresionada = true;
                        cocheF1.girarDerecha();
                        break;
                        
                   
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_W:
                        teclaArribaPresionada = false;
                        cocheF1.soltarAcelerador();
                        break;
                        
                    case KeyEvent.VK_SPACE:
                    case KeyEvent.VK_S:
                        teclaAbajoPresionada = false;
                      
                        break;
                        
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_A:
                        teclaIzquierdaPresionada = false;
                        break;
                        
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_D:
                        teclaDerechaPresionada = false;
                        break;
                }
            }
        });
        
        
 
        
        
        
        
        
        
        
    }
    
    
 

	private Path2D crearLineaMeta() {

        Path2D meta = new Path2D.Double();
        // Colocar la meta delante de la posición inicial del coche (1000, 500)
        // Ajusta estas coordenadas según la posición exacta donde quieres la meta
        meta.moveTo(981, 400);
        meta.lineTo(1060, 400);
        meta.lineTo(1060, 350);
        meta.lineTo(981, 350);
       
        meta.closePath();
        return meta;
    }
    
    public boolean haCruzadoMeta(Point2D punto) {
        Area areaPunto = crearAreaPunto(punto);
        areaPunto.intersect(areaLineaMeta);
        return !areaPunto.isEmpty();
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
        
        cocheF1.actualizar();
        
        Point2D posicionCoche = new Point2D.Double(cocheF1.getX(), cocheF1.getY());
        if (haCruzadoMeta(posicionCoche)) {
            if (!haCruzado) { // Solo cuenta si aún no ha cruzado
            	vueltas++;
            	if(vueltas > 1 ) {
            		temporizador.stop();
            	}
                haCruzado = true; // Marca como cruzado
            }
        } else {
            haCruzado = false; // Resetea el estado si ya no está sobre la meta
        }

        
       
        
    
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
        for(int i = 50; i < 750; i += 50) {
            for(int j = 50; j < 1200; j += 50) {
                g2d.drawLine(0, i, 1200, i);
                g2d.drawLine(j, 0, j, 750);
            }
        }

        // Dibujar líneas de referencia
        g2d.setStroke(new BasicStroke(8)); 
        g2d.setColor(new Color(200, 204, 206)); 
        g2d.drawLine(850, 610, 1000, 500);
        g2d.drawLine(600, 215, 800, 150);
        
        g2d.setStroke(new BasicStroke(2)); 
        g2d.setColor(new Color(0, 0, 0)); 
        g2d.drawLine(852, 612, 1002, 502);
        g2d.drawLine(848, 608, 998, 498);
        g2d.drawLine(602, 217, 802, 152);
        g2d.drawLine(598, 213, 798, 148);
        
 
        
        // Dibujar zona de boxes
        dibujarZonaBoxesCompleta(g2d, 280, 60, 300, 80);
        
        // Crear el path principal de la pista
        Path2D path = crearPathPistaPrincipal();
        
       
        
        // Dibujar bordes de la pista
        g2d.setStroke(new BasicStroke(90));
        g2d.setColor(Color.WHITE);
        g2d.draw(path);
        

        float[] dashPattern = {20, 20};
        g2d.setStroke(new BasicStroke(90, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, dashPattern, 0));
        g2d.setColor(Color.RED);
        g2d.draw(path);
        
        // Dibujar asfalto del circuito
        g2d.setStroke(new BasicStroke(80));
        g2d.setColor(Color.DARK_GRAY);
        g2d.draw(path);
        
       
        // Dibujar la línea central del circuito
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, new float[] {10, 15}, 0));
        g2d.setColor(Color.WHITE);
        g2d.draw(path);
        
    
        
      
        
        // Dibujar elementos decorativos
        g2d.setStroke(new BasicStroke(5));
        g2d.setColor(Color.RED);
        dibujarCirculosZona(g2d, 945, 190, 30, 100, 5, 2);
        
        g2d.setStroke(new BasicStroke(5));
        g2d.setColor(Color.white);
        dibujarCirculosZona(g2d, 945, 410, 30, 90, 5, 2);
        
        // Dibujar banners y tribunas
        dibujarBannerVertical(g2d, 170, 350, 150);
        dibujarBannerVertical(g2d, 950, 345, 100);
        dibujarTribuna(g2d, 1100, 0, 70, 750, 25);
        
        
        // Dibujar la línea de meta
        g2d.setStroke(new BasicStroke(5));
        g2d.setColor(new Color(0,0,0));
        g2d.fill(lineaMeta);
        
        int x = 981; // Coordenada inicial X
        int y = 350; // Coordenada inicial Y
        int anchoCuadro = 10; // Tamaño del cuadro
        int altoCuadro = 10;

        for (int i = 0; i < 5; i++) { // Filas
            for (int j = 0; j < 8; j++) { // Columnas
                if ((i + j) % 2 == 0) { // Alternar entre blanco y negro
                    g2d.setColor(new Color(255, 255, 255)); // Blanco
                } else {
                    g2d.setColor(Color.DARK_GRAY); // Negro
                }
                g2d.fillRect(x + j * anchoCuadro, y + i * altoCuadro, anchoCuadro, altoCuadro);
            }
        }


        //lineas de arranque
        g2d.setColor(Color.white);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(984, 420, 984 , 440);
        g2d.drawLine(984, 420, 1013 , 420);
        g2d.drawLine(1013, 420, 1013 , 440);
        
        
        g2d.drawLine(1023, 450, 1023 , 470);
        g2d.drawLine(1023, 450, 1052 , 450);
        g2d.drawLine(1052, 450, 1052 , 470);
        
      
        
        dibujarArbusto(g2d, 150, 220, 70, 50); 
        dibujarArbusto(g2d, 150, 430, 70, 50);
     
  
     
        cocheF1.dibujar(g2d);
        
        
        dibujarTableroDatos(g2d);
    
    }
    
    
    
    private Path2D crearPathPistaPrincipal() {
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

        // Curva suave hacia el lado izquierdo
        path.curveTo(300, 180, 250, 170, 210, 140);
        path.curveTo(170, 110, 150, 80, 130, 80);

        // Tramo vertical izquierdo superior
        path.curveTo(110, 80, 100, 100, 100, 130);
        path.lineTo(100, 550);

        // Curva inferior izquierda
        path.curveTo(100, 610, 150, 650, 200, 650);

        // Cierre al punto inicial
        path.lineTo(350, 650);
        
        return path;
    }
    
    public void dibujarArbusto(Graphics2D g2d, int x, int y, int ancho, int alto) {
        // Crear un degradado radial para el arbusto
        RadialGradientPaint gradiente = new RadialGradientPaint(
            new Point2D.Double(x + ancho / 2, y + alto / 2), // Centro
            ancho / 2, // Radio del degradado
            new float[]{0f, 1f}, // Distribución del color
            new Color[]{new Color(34, 139, 34), new Color(0, 100, 0)} // Colores (verde claro a oscuro)
        );

        g2d.setPaint(gradiente);
        g2d.fillOval(x, y, ancho, alto); // Dibujar el arbusto con forma ovalada
    }

    
    private Area calcularArea() {
    	
    	Path2D path = crearPathPistaPrincipal();
    	
    	BasicStroke strokePistaCompleta = new BasicStroke(90, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
    	
    	Shape formaAreaPista = strokePistaCompleta.createStrokedShape(path);
    	
    	return new Area(formaAreaPista);
    	 
    }
    
   

    public boolean estaEnPista(Point2D punto) {
        Area areaPunto = crearAreaPunto(punto);
        areaPunto.intersect(calcularArea());
        return !((areaPunto).isEmpty());
    }
    

    private Area crearAreaPunto(Point2D punto) {
        Path2D.Double puntoPath = new Path2D.Double();
        puntoPath.moveTo(punto.getX(), punto.getY());
        puntoPath.lineTo(punto.getX()+1, punto.getY());
        puntoPath.lineTo(punto.getX()+1, punto.getY()+1);
        puntoPath.lineTo(punto.getX(), punto.getY()+1);
        puntoPath.closePath();
        return new Area(puntoPath);
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
        g2d.setColor(new Color(124, 252, 0)); 
        g2d.fill(fondo);
    }
    

    private void dibujarBannerVertical(Graphics2D g2d, int centroX, int centroY, int altura) {
        AffineTransform originalTransform = g2d.getTransform();
        
        int ancho = 40;
        int radioEsquina = 10;
        
        GradientPaint gradiente = new GradientPaint(
            centroX, centroY - altura/2, new Color(255, 215, 0),
            centroX, centroY + altura/2, new Color(255, 165, 0));
        g2d.setPaint(gradiente);
        g2d.fillRoundRect(centroX - ancho/2, centroY - altura/2, ancho, altura, radioEsquina, radioEsquina);
        
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(centroX - ancho/2, centroY - altura/2, ancho, altura, radioEsquina, radioEsquina);
        
        Font font = new Font("Arial", Font.BOLD, ajustarTamañoFuente(altura));
        g2d.setFont(font);
        String texto = "PIRELLI";
        FontMetrics fm = g2d.getFontMetrics();
        
        g2d.rotate(Math.toRadians(-90), centroX, centroY);
        int textX = centroX - altura/2 + (altura - fm.stringWidth(texto))/2;
        int textY = centroY + fm.getAscent()/2;
        
        g2d.setColor(new Color(0, 0, 0, 80));
        g2d.drawString(texto, textX + 2, textY + 2);
        
        g2d.setColor(Color.BLACK);
        g2d.drawString(texto, textX, textY);
        
        g2d.setTransform(originalTransform);
    }

    private int ajustarTamañoFuente(int altura) {
        int tamañoBase = altura / 5;
        return Math.max(12, Math.min(18, tamañoBase));
    }



    
    
    
    private void dibujarTribuna(Graphics2D g2d, int x, int y, int ancho, int alto, int niveles) {
        int yAjustado = y;
        
        g2d.setColor(new Color(220, 220, 220));
        g2d.fillRect(x, yAjustado, ancho, alto);

        int anchoTorre = 50;
        int alturaTorre = alto + 150;
        
        GradientPaint gradienteTorre = new GradientPaint(
            x + ancho, yAjustado, new Color(100, 100, 100),
            x + ancho + anchoTorre, yAjustado, new Color(60, 60, 60));
        g2d.setPaint(gradienteTorre);
        g2d.fillRect(1150, yAjustado - 50, anchoTorre, alturaTorre);
        
        g2d.setColor(new Color(80, 80, 80));
        g2d.setStroke(new BasicStroke(4));
        for(int i = 0; i < 4; i++) {
            int yRefuerzo = yAjustado + i * (alturaTorre/3);
            g2d.drawLine(x + ancho, yRefuerzo, x + ancho + anchoTorre, yRefuerzo + 50);
            g2d.drawLine(x + ancho, yRefuerzo + 50, x + ancho + anchoTorre, yRefuerzo);
        }
        
        int anchoMarquesina = 120;
        int altoMarquesina = 40;
        
        g2d.setColor(new Color(30, 30, 30, 150));
        g2d.fillRect(x + ancho + anchoTorre, yAjustado - 30, anchoMarquesina, altoMarquesina);
        
        g2d.setColor(new Color(150, 150, 150));
        g2d.fillRect(x + ancho + anchoTorre, yAjustado - 30, anchoMarquesina, 5);
        g2d.fillRect(x + ancho + anchoTorre + anchoMarquesina, yAjustado - 30, 10, altoMarquesina + 30);
        
        g2d.setColor(new Color(200, 230, 255, 120));
        g2d.fillRect(x + ancho + anchoTorre, yAjustado - 30, anchoMarquesina, altoMarquesina);
        
        int alturaNivel = alto / niveles;
        for(int i = 0; i < niveles; i++) {
            int yNivel = yAjustado + i * alturaNivel;
            
            g2d.setColor(new Color(50, 50, 50));
            g2d.fillRect(x, yNivel, ancho, 5);
            
            g2d.setColor(new Color(190, 190, 190));
            g2d.fillRect(x + 5, yNivel + 3, ancho - 10, alturaNivel - 3);
            
            dibujarEspectadores(g2d, x + 5, yNivel + 3, ancho - 10, alturaNivel - 3, 35);
        }
        
        g2d.setColor(new Color(255, 255, 150, 200));
        g2d.fillOval(x + ancho + anchoTorre + anchoMarquesina - 15, yAjustado - 40, 20, 20);
    }
    
    private void dibujarEspectadores(Graphics2D g2d, int x, int y, int ancho, int alto, int densidad) {
    	 Random rand = new Random(20000); 
        int tamPersona = Math.max(4, alto/8);
        
        for (int i = 0; i < densidad; i++) {
            int px = x + rand.nextInt(ancho - tamPersona);
            int py = y + rand.nextInt(alto - tamPersona);
            
            g2d.setColor(new Color(rand.nextInt(40)+190, rand.nextInt(40)+160, rand.nextInt(40)+120));
            g2d.fillOval(px, py, tamPersona, tamPersona);
            
            GradientPaint camiseta = new GradientPaint(
                px, py, new Color(rand.nextInt(200)+55, rand.nextInt(200)+55, rand.nextInt(200)+55),
                px, py+tamPersona, new Color(rand.nextInt(200)+55, rand.nextInt(200)+55, rand.nextInt(200)+55));
            g2d.setPaint(camiseta);
            g2d.fillRect(px-tamPersona/4, py+tamPersona, tamPersona/2, tamPersona);
        }
    }
    
    private void dibujarZonaBoxesCompleta(Graphics2D g2d, int x, int y, int ancho, int alto) {
        g2d.setColor(new Color(60, 60, 60));
        g2d.fillRect(x, y, ancho, alto);
        
        g2d.setColor(new Color(30, 30, 30, 200));
        g2d.fillRect(x-15, y-25, ancho+30, 25);
        
        g2d.setColor(new Color(150, 150, 150));
        for(int i = 0; i < 6; i++) {
            int colX = x + i*(ancho/5);
            g2d.fillRoundRect(colX-3, y-25, 6, 25, 5, 5);
        }
        
        g2d.setStroke(new BasicStroke(2));
        int numBoxes = 10;
        for(int i = 0; i < numBoxes; i++) {
            int boxX = x + i*(ancho/numBoxes);
            
            g2d.setColor(new Color(180, 180, 180));
            g2d.fillRect(boxX+2, y+5, (ancho/numBoxes)-4, alto-10);
            
            g2d.setColor(new Color(100, 100, 100));
            g2d.drawRect(boxX+2, y+5, (ancho/numBoxes)-4, alto-10);
            g2d.drawLine(boxX+(ancho/numBoxes)/2, y+5, boxX+(ancho/numBoxes)/2, y+alto-5);
        }
        
        g2d.setColor(ColorPricipal);
        g2d.setFont(Main.GLOBAL_FONT2.deriveFont(15f));
        g2d.drawString("PITS", x+ancho/2-15, y+alto/2+5);
    }


    private void mostrarConfirmacion() {
        // Crear un panel personalizado con diseño moderno
        JPanel panelMensaje = new JPanel(new BorderLayout(40, 40)); 
        panelMensaje.setBackground(new Color(45, 45, 45)); 
        panelMensaje.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20)); // Espaciado interno

        // Crear mensaje con formato
        JLabel texto = new JLabel("¿Estás seguro de que deseas regresar al menú principal?");
        texto.setFont(Main.GLOBAL_FONT2.deriveFont(14f)); // Aumentar tamaño de fuente
        texto.setForeground(new Color(255, 215, 0)); // Color dorado
        texto.setHorizontalAlignment(SwingConstants.CENTER);

        // Agregar componentes al panel
        panelMensaje.add(texto, BorderLayout.CENTER); // Texto en el centro

        // Configuración de UIManager para colores
        UIManager.put("OptionPane.background", new Color(45, 45, 45));
        UIManager.put("Panel.background", new Color(45, 45, 45));
        UIManager.put("OptionPane.messageForeground", Color.WHITE); // Color del mensaje

        // Crear botones personalizados sin foco
        JButton btnSi = new JButton("Sí");
        JButton btnNo = new JButton("No");

        btnSi.setBackground(new Color(100, 100, 100));
        btnSi.setForeground(Color.WHITE);
        btnSi.setFont(Main.GLOBAL_FONT2.deriveFont(14f)); // Misma fuente que el mensaje
        btnSi.setFocusPainted(false); // Eliminar borde de enfoque
        btnSi.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Botón más estilizado

        btnNo.setBackground(new Color(100, 100, 100));
        btnNo.setForeground(Color.WHITE);
        btnNo.setFont(Main.GLOBAL_FONT2.deriveFont(14f));
        btnNo.setFocusPainted(false);
        btnNo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Crear panel para botones
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(45, 45, 45));
        panelBotones.add(btnSi);
        panelBotones.add(btnNo);

        // Mostrar diálogo con diseño personalizado
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Confirmación");
        dialog.setLayout(new BorderLayout());
        dialog.add(panelMensaje, BorderLayout.CENTER);
        dialog.add(panelBotones, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);

        // Eventos de los botones
        btnSi.addActionListener(e -> {
            if (timer != null) timer.stop();
            if (temporizador != null) temporizador.stop();
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) window.dispose();
            dialog.dispose();
        });

        btnNo.addActionListener(e -> dialog.dispose());

        // Mostrar el diálogo
        dialog.setVisible(true);
    }



    private void dibujarTableroDatos(Graphics2D g2d) {
        int x = 1000;
        int y = 0;
        int ancho = 200;
        int alto = 80;
        
        // Fondo del tablero
        GradientPaint fondoTablero = new GradientPaint(
            x, y, new Color(30, 30, 30),
            x, y + alto, new Color(60, 60, 60)
        );
        g2d.setPaint(fondoTablero);
        g2d.fillRoundRect(x, y, ancho, alto, 15, 15);
        
        // Borde del tablero
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(new Color(100, 100, 100));
        g2d.drawRoundRect(x, y, ancho, alto, 15, 15);
        // Detalles estéticos (tornillos en las esquinas)
        g2d.setColor(new Color(150, 150, 150));
        int radioTornillo = 5;
        g2d.fillOval(x + 8, y + 8, radioTornillo*2, radioTornillo*2);
        g2d.fillOval(x + ancho - 8 - radioTornillo*2, y + 8, radioTornillo*2, radioTornillo*2);
        g2d.fillOval(x + 8, y + alto - 8 - radioTornillo*2, radioTornillo*2, radioTornillo*2);
        g2d.fillOval(x + ancho - 8 - radioTornillo*2, y + alto - 8 - radioTornillo*2, radioTornillo*2, radioTornillo*2);
        
        // Línea divisoria
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(new Color(100, 100, 100));
        g2d.drawLine(x + 10, y + alto/2, x + ancho - 10, y + alto/2);
        
        // Sección Vueltas
        g2d.setFont(Main.GLOBAL_FONT.deriveFont(13f));
        g2d.setColor(new Color(253, 217, 0));
        g2d.drawString("VUELTAS:", x + 15, y + 28);
        
        // Número de vueltas (estilo LED digital)
        g2d.setFont(Main.Digital);
        g2d.setColor(new Color(255,255,255)); 
        g2d.drawString(String.format("%01d/%01d", vueltas, VUELTAS_PARA_FINALIZAR), 1120, y + 28);
        
        // Sección Tiempo
        g2d.setFont(Main.GLOBAL_FONT.deriveFont(13f));
        g2d.setColor(new Color(253, 217, 0));
        g2d.drawString("TIEMPO:", x + 15, y + 28 + alto/2);
        
        // Tiempo (estilo LED digital)
        g2d.setFont(Main.Digital.deriveFont(22f));
        g2d.setColor(new Color(255,255,255));
        String tiempoStr = String.format("%02d:%02d:%02d", minutos, segundos, milisegundos / 10);
        g2d.drawString(tiempoStr, 1108, y + 28 + alto/2);
        
        // Mostrar penalización si es necesario
        if (mostrandoPenalizacion) {
            g2d.setColor(new Color(255, 0, 0, 200));
            g2d.setFont(Main.GLOBAL_FONT.deriveFont(Font.BOLD, 24f));
            g2d.drawString("+" + PENALIZACION_SEGUNDOS + "s", x + ancho - 60, y + 40);
        }
        
        // Mostrar total de penalizaciones
        if (penalizaciones > 0) {
            g2d.setColor(new Color(255, 80, 80));
            g2d.setFont(Main.GLOBAL_FONT.deriveFont(12f));
            g2d.drawString("Penalizaciones: " + penalizaciones + " (" + segundosPenalizacion + "s)", x + ancho - 130, y + alto - 5);
        }
    }
        
    private void verificarColisiones(Point2D posicionCoche) {
        System.out.println("Verificando colisiones. En pista: " + estaEnPista(posicionCoche));
        if (!estaEnPista(posicionCoche)) {
            long tiempoActual = System.currentTimeMillis();
            System.out.println("Fuera de pista. Tiempo actual: " + tiempoActual + ", último choque: " + ultimoChoque);
            if (tiempoActual - ultimoChoque > 1000) {
                System.out.println("Aplicando penalización. Penalizaciones antes: " + penalizaciones);
                penalizaciones++;
                segundosPenalizacion += PENALIZACION_SEGUNDOS;
                System.out.println("Penalizaciones después: " + penalizaciones + ", segundos: " + segundosPenalizacion);
                mostrandoPenalizacion = true;
                
                // Resto del código para el timer...
                
                ultimoChoque = tiempoActual;
            }
        }
    }

// Agrega este método para finalizar la carrera
private void finalizarCarrera() {
    carreraFinalizada = true;
    timer.stop();
    temporizador.stop();
    if (timerPenalizacion != null) {
        timerPenalizacion.stop();
    }
    
    // Mostrar ventana de podio
    mostrarVentanaPodio();
}




// Método para mostrar la ventana de podio

private void mostrarVentanaPodio() {
    JDialog podioDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "¡Carrera Finalizada!");
    podioDialog.setSize(1200, 750);
    podioDialog.setUndecorated(true);
    podioDialog.setLocationRelativeTo(this);
    podioDialog.setResizable(false);
    
    JPanel panelPodio = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Fondo degradado
            GradientPaint fondoGradiente = new GradientPaint(
                0, 0, new Color(30, 30, 60),
                0, getHeight(), new Color(15, 15, 30)
            );
            g2d.setPaint(fondoGradiente);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            // Dibujar podio
            g2d.setColor(new Color(212, 175, 55)); // Dorado
            g2d.fillRect(getWidth()/2 - 75, getHeight() - 100, 150, 80);
            
            // Dibujar confetti
            Random rand = new Random();
            for (int i = 0; i < 100; i++) {
                g2d.setColor(new Color(
                    rand.nextInt(255),
                    rand.nextInt(255),
                    rand.nextInt(255)
                ));
                g2d.fillOval(
                    rand.nextInt(getWidth()),
                    rand.nextInt(getHeight()/2),
                    5, 5
                );
            }
            
            // Dibujar coche
            AffineTransform originalTransform = g2d.getTransform();
            g2d.translate(getWidth()/2, getHeight() - 130);
            g2d.scale(1.5, 1.5);
            cocheF1.dibujarStatico(g2d);
            g2d.setTransform(originalTransform);
            
            // Dibujar texto
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            g2d.drawString("¡CARRERA FINALIZADA!", getWidth()/2 - 150, 40);
            
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.drawString("Estadísticas", getWidth()/2 - 50, 80);
            
            g2d.setFont(new Font("Arial", Font.PLAIN, 16));
            g2d.drawString("Vueltas completadas: " + vueltas + "/" + VUELTAS_PARA_FINALIZAR, 50, 120);
            
            // Calcular tiempo total con penalizaciones
            int tiempoTotalSegundos = minutos * 60 + segundos + segundosPenalizacion;
            int minutosTotales = tiempoTotalSegundos / 60;
            int segundosTotales = tiempoTotalSegundos % 60;
            int penalizacionActual = CocheF1.getPenalizacion();
            g2d.drawString("Tiempo: " + String.format("%02d:%02d.%02d", minutos, segundos, milisegundos / 10), 50, 150);
            g2d.drawString("Penalizaciones: " + penalizacionActual + " (" + segundosPenalizacion + " segundos)", 50, 180);
            g2d.drawString("Tiempo total: " + String.format("%02d:%02d.%02d", minutosTotales, segundosTotales, milisegundos / 10), 50, 210);
        }
    };
    
    podioDialog.setLayout(new BorderLayout());
    podioDialog.add(panelPodio, BorderLayout.CENTER);
    
    JPanel panelBotones = new JPanel(new FlowLayout()); // Set explicit layout
    panelBotones.setBackground(new Color(30, 30, 60));
    panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 0)); // Add some padding
    
    JButton btnVolverCorrer = new JButton("Volver a Correr");
    btnVolverCorrer.setFont(Main.GLOBAL_FONT.deriveFont(14f));
    btnVolverCorrer.setBackground(Color.DARK_GRAY);
    btnVolverCorrer.setForeground(Color.WHITE);
    btnVolverCorrer.setFocusPainted(false);
    btnVolverCorrer.setPreferredSize(new Dimension(300, 40)); // Set a specific size
    
    JButton btnVolverMenu = new JButton("Menú Principal");
    btnVolverMenu.setFont(Main.GLOBAL_FONT.deriveFont(14f));
    btnVolverMenu.setBackground(Color.DARK_GRAY);
    btnVolverMenu.setForeground(Color.WHITE);
    btnVolverMenu.setFocusPainted(false);
    btnVolverMenu.setPreferredSize(new Dimension(300, 40)); // Set a specific size
    
    btnVolverCorrer.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            podioDialog.dispose();
            reiniciarCarrera();
        }
    });
    
    btnVolverMenu.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            podioDialog.dispose();
            Window window = SwingUtilities.getWindowAncestor(CircuitoF1.this);
            if (window != null) window.dispose();
        }
    });
    
    panelBotones.add(btnVolverCorrer);
    panelBotones.add(btnVolverMenu);
    
    // Make sure the dialog's layout is properly set
    podioDialog.setLayout(new BorderLayout());
    podioDialog.add(panelPodio, BorderLayout.CENTER);
    podioDialog.add(panelBotones, BorderLayout.SOUTH);
    
    podioDialog.setVisible(true);
}

// Método para reiniciar la carrera
private void reiniciarCarrera() {
    // Reiniciar variables
    vueltas = 0;
    segundos = 0;
    milisegundos = 0;
    minutos = 0;
    penalizaciones = 0;
    segundosPenalizacion = 0;
    carreraFinalizada = false;
    
    // Reiniciar coche
    cocheF1 = new CocheF1(1000, 450, this);
    
    // Reiniciar temporizadores
    if (temporizador != null) {
        temporizador.start();
    }
    if (timer != null) {
        timer.start();
    }
    
    // Solicitar enfoque
    requestFocus();
}













}









