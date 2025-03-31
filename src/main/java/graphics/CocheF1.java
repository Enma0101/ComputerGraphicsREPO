package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class CocheF1 {
    // Posición y propiedades físicas
    private double x, y;
    private double velocidad;
    private double angulo; // En radianes
    private double aceleracion;
    private double velocidadMaxima;
    private double friccion;
    private double maniobrabilidad;
    private boolean frenando;
    
    // Tamaño del coche
    private int ancho;
    private int alto;
    
    // Colores
    private Color colorPrincipal;
    private Color colorSecundario;
    
    // Lista de puntos para la detección de colisiones
    private List<Point2D> puntosColision;
    
    // Para detectar si el coche está en la pista
    private boolean enPista;
    
    // Imagen del coche (opcional)
    private BufferedImage imagen;

    public CocheF1(double x, double y) {
        this.x = x;
        this.y = y;
        this.velocidad = 0;
        this.angulo = 0;
        this.aceleracion = 0.2;
        this.velocidadMaxima = 7.0;
        this.friccion = 0.95;
        this.maniobrabilidad = 0.05;
        this.frenando = false;
        this.ancho = 20;
        this.alto = 40;
        this.colorPrincipal = Color.RED;
        this.colorSecundario = Color.BLACK;
        this.puntosColision = new ArrayList<>();
        this.enPista = true;
        
        // Intentar cargar la imagen si existe
        try {
            this.imagen = ImageIO.read(new File("coche_f1.png"));
        } catch (IOException e) {
            this.imagen = null;
            System.out.println("Usando gráficos vectoriales para el coche");
        }
    }
    
    public void actualizar() {
        // Aplicar aceleración o frenado
        if (frenando) {
            velocidad *= 0.9; // Frenado más intenso
        }
        
        // Aplicar fricción natural
        velocidad *= friccion;
        
        // Actualizar posición basada en velocidad y ángulo
        x += Math.sin(angulo) * velocidad;
        y -= Math.cos(angulo) * velocidad;
        
        // Actualizar puntos de colisión
        actualizarPuntosColision();
    }
    
    private void actualizarPuntosColision() {
        puntosColision.clear();
        
        // Crear puntos alrededor del perímetro del coche para detectar colisiones
        double frente = alto / 2.0;
        double atras = -alto / 2.0;
        double izquierda = -ancho / 2.0;
        double derecha = ancho / 2.0;
        
        // 8 puntos alrededor del coche
        agregarPuntoTransformado(0, -frente); // Frente
        agregarPuntoTransformado(derecha, -frente * 0.8); // Frente-derecha
        agregarPuntoTransformado(derecha, 0); // Derecha
        agregarPuntoTransformado(derecha, atras * 0.8); // Atrás-derecha
        agregarPuntoTransformado(0, atras); // Atrás
        agregarPuntoTransformado(izquierda, atras * 0.8); // Atrás-izquierda
        agregarPuntoTransformado(izquierda, 0); // Izquierda
        agregarPuntoTransformado(izquierda, -frente * 0.8); // Frente-izquierda
    }
    
    private void agregarPuntoTransformado(double dx, double dy) {
        double cosa = Math.cos(angulo);
        double sina = Math.sin(angulo);
        
        double nx = x + (dx * cosa - dy * sina);
        double ny = y + (dx * sina + dy * cosa);
        
        puntosColision.add(new Point2D.Double(nx, ny));
    }
    
    public void dibujar(Graphics2D g2d) {
        // Guardar la transformación original
        AffineTransform transformOriginal = g2d.getTransform();
        
        // Configurar la calidad del renderizado
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Mover al punto de origen del coche y rotar
        g2d.translate(x, y);
        g2d.rotate(angulo);
        
        if (imagen != null) {
            // Dibujar usando la imagen
            g2d.drawImage(imagen, -ancho/2, -alto/2, ancho, alto, null);
        } else {
            // Dibujar usando vectores
            
            // Cuerpo principal
            g2d.setColor(colorPrincipal);
            g2d.fillRect(-ancho/2, -alto/2, ancho, alto);
            
            // Cabina
            g2d.setColor(colorSecundario);
            g2d.fillOval(-ancho/3, -alto/4, (int) (ancho/1.5f), alto/3);
            
            // Alerón delantero
            g2d.setColor(colorSecundario);
            g2d.fillRect(-ancho/2, -alto/2 - 5, ancho, 5);
            
            // Alerón trasero
            g2d.setColor(colorSecundario);
            g2d.fillRect(-ancho/2, alto/2, ancho, 5);
            
            // Detalles adicionales
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(-ancho/2 - 2, -alto/3, 2, alto/4);
            g2d.fillRect(ancho/2, -alto/3, 2, alto/4);
        }
        
        // Si estamos fuera de la pista, mostrar un indicador
        if (!enPista) {
            g2d.setColor(new Color(255, 0, 0, 128));
            g2d.fillOval(-ancho/2 - 5, -alto/2 - 5, ancho + 10, alto + 10);
        }
        
        // Restaurar la transformación original
        g2d.setTransform(transformOriginal);
        
        // Para depuración, dibujamos los puntos de colisión
        /*
        g2d.setColor(Color.YELLOW);
        for (Point2D punto : puntosColision) {
            g2d.fillOval((int)punto.getX() - 2, (int)punto.getY() - 2, 4, 4);
        }
        */
    }
    
    // Control del coche
    public void acelerar() {
        velocidad = Math.min(velocidad + aceleracion, velocidadMaxima);
        frenando = false;
    }
    
    public void frenar() {
        frenando = true;
    }
    
    public void girarIzquierda() {
        // Solo permitir giro efectivo cuando hay movimiento
        if (Math.abs(velocidad) > 0.1) {
            angulo -= maniobrabilidad * (velocidad > 0 ? 1 : -1);
        }
    }
    
    public void girarDerecha() {
        // Solo permitir giro efectivo cuando hay movimiento
        if (Math.abs(velocidad) > 0.1) {
            angulo += maniobrabilidad * (velocidad > 0 ? 1 : -1);
        }
    }
    
    public boolean comprobarColision(Path2D pistaBorde, Path2D pistaInterior) {
        // Verificar si alguno de los puntos está fuera de la pista
        boolean dentroExterior = true;
        boolean fueraDentro = true;
        
        for (Point2D punto : puntosColision) {
            // Debe estar dentro del borde exterior
            if (!pistaBorde.contains(punto)) {
                dentroExterior = false;
            }
            
            // Y fuera del borde interior (si existe)
            if (pistaInterior != null && pistaInterior.contains(punto)) {
                fueraDentro = false;
            }
        }
        
        enPista = dentroExterior && fueraDentro;
        
        // Si está fuera de la pista, reducir la velocidad
        if (!enPista) {
            velocidad *= 0.8; // Mayor resistencia fuera de la pista
        }
        
        return !enPista;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void detener() {
        this.velocidad = 0;
    }
}