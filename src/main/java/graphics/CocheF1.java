package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import java.util.ArrayList;
import java.util.List;

import ui.CircuitoF1;

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
    private boolean acelerando;
    private int contadorAceleracion;
    private double aceleracionInicial;
    private double ultimaXValida, ultimaYValida;
    private double ultimoAnguloValido;
    private double inicioX, inicioY;
    private Color colorPrincipal = new Color(200, 204, 206);
    private Color colorSecundario;
    private Color colorDetalles;
   private CircuitoF1 pista;
   private int  Vueltas;
   private static int Penalizacion = 0;
  
  
    // Tamaño del coche
    private int ancho;
    private int alto;
    
    // Colores
  
    
    // Lista de puntos para la detección de colisiones
    private List<Point2D> puntosColision;
    
    // Para detectar si el coche está en la pista
    private boolean enPista;
    private boolean enPits;
    

    public CocheF1(double x, double y, CircuitoF1 pista ) {
        this.x = x;
        this.y = y;
        this.inicioX = x;
        this.inicioY = y;
        this.velocidad = 0;
        this.angulo = 0;
        this.aceleracionInicial = 0.2; // 
        this.aceleracion = aceleracionInicial;
        this.velocidadMaxima = 8; 
        this.friccion = 0.975; // 
        this.maniobrabilidad = 0.095; // 
        this.frenando = false;
        this.acelerando = false;
        this.contadorAceleracion = 0;
        this.ancho = 24;
        this.alto = 45;
        this.colorPrincipal = colorPrincipal;
        this.colorSecundario = Color.BLACK;
        this.colorDetalles = Color.WHITE;
        this.puntosColision = new ArrayList<>();
        this.enPista = true;
        this.enPits = false;
        this.x = x;
        this.y = y;
        this.pista = pista;
        this.ultimaXValida = x;
        this.ultimaYValida = y;
        this.ultimoAnguloValido = 0;
      
    }
    
    public void actualizar() {
        // Guardar la posición válida actual antes de mover
        if (enPista) {
            ultimaXValida = x;
            ultimaYValida = y;
            ultimoAnguloValido = angulo;
        }
        
       
        
        if (acelerando) {
            contadorAceleracion++;
            // Mejora de la aceleración inicial y progresiva
            double factorAceleracion = Math.min(1.5 + (contadorAceleracion / 60.0), 8); // Aumentado de 1.0 a 1.5 y reducido de 60 a 50
            velocidad = Math.min(velocidad + (aceleracion * factorAceleracion), velocidadMaxima);
        } else {
            // Mantener parte de la aceleración ganada
            contadorAceleracion = Math.max(0, contadorAceleracion - 5); // Desacelera gradualmente
        }
        
        // Aplicar frenado
        if (frenando) {
            velocidad *= 0.85; // Frenado más intenso (de 0.9 a 0.85)
        }
        // Verificar si está en pits
        Point2D posicionActual = new Point2D.Double(x, y);
        
        
        // Aplicar fricción natural o reducción en los pits
        if (enPits) {
            // En pits, limitar la velocidad a un máximo
            double velocidadMaximaPits = 4.5; // Un poco mayor que antes (4.0)
            if (velocidad > velocidadMaximaPits) {
                velocidad = Math.max(velocidadMaximaPits, velocidad * 0.95);
            }
        } else {
            // Fricción normal
            velocidad *= friccion;
        }
        
        // Calcular la nueva posición basada en velocidad y ángulo
        double nuevaX = x + Math.sin(angulo) * velocidad;
        double nuevaY = y - Math.cos(angulo) * velocidad;
        
        // Crear un punto para la nueva posición
        Point2D nuevaPosicion = new Point2D.Double(nuevaX, nuevaY);
        
        // Verificar si la nueva posición está en la pista
        boolean enNuevaPista = pista.estaEnPista(nuevaPosicion);
        
        // Solo actualizar posición si está en pista o en pits
        if (enNuevaPista) {
            x = nuevaX;
            y = nuevaY;
            enPista = enNuevaPista;
        } else {
        	Penalizacion ++;
        	
            respawn();
        }
        
      
        actualizarPuntosColision();
    }
    
    private void respawn() {
        // Respawn en la última posición válida conocida
        this.x = ultimaXValida;
        this.y = ultimaYValida;
        this.angulo = ultimoAnguloValido+180;
        this.velocidad = 0;
        this.aceleracion = aceleracionInicial;
        this.contadorAceleracion = 0;
    }
    
    private void actualizarPuntosColision() {
        puntosColision.clear();
        
        // Crear puntos alrededor del perímetro del coche para detectar colisiones
        double frente = alto / 2.0;
        double atras = -alto / 2.0;
        double izquierda = -ancho / 2.0;
        double derecha = ancho / 2.0;
        
        // 10 puntos alrededor del coche para mejor detección
        agregarPuntoTransformado(0, -frente); // Frente
        agregarPuntoTransformado(derecha * 0.5, -frente * 0.9); // Frente-derecha
        agregarPuntoTransformado(derecha, -frente * 0.6); // Derecha-frente
        agregarPuntoTransformado(derecha, 0); // Derecha
        agregarPuntoTransformado(derecha, atras * 0.6); // Derecha-atrás
        agregarPuntoTransformado(derecha * 0.5, atras * 0.9); // Atrás-derecha
        agregarPuntoTransformado(0, atras); // Atrás
        agregarPuntoTransformado(izquierda * 0.5, atras * 0.9); // Atrás-izquierda
        agregarPuntoTransformado(izquierda, atras * 0.6); // Izquierda-atrás
        agregarPuntoTransformado(izquierda, 0); // Izquierda
        agregarPuntoTransformado(izquierda, -frente * 0.6); // Izquierda-frente
        agregarPuntoTransformado(izquierda * 0.5, -frente * 0.9); // Frente-izquierda
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
        
        // Dibujar usando vectores - Diseño mejorado
        
        // Cuerpo principal con forma más aerodinámica
        Path2D cuerpo = new Path2D.Double();
        cuerpo.moveTo(-ancho/2, alto/3);
        cuerpo.lineTo(-ancho/4, -alto/2); // Punta frontal
        cuerpo.lineTo(ancho/4, -alto/2);
        cuerpo.lineTo(ancho/2, alto/3);
        cuerpo.lineTo(ancho/2, alto/2);
        cuerpo.lineTo(-ancho/2, alto/2);
        cuerpo.closePath();
        
        g2d.setColor(colorPrincipal);
        g2d.fill(cuerpo);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.draw(cuerpo);
        
        // Cabina del piloto (cockpit)
        Path2D cabina = new Path2D.Double();
        cabina.moveTo(-ancho/6, -alto/6);
        cabina.lineTo(0, -alto/3);
        cabina.lineTo(ancho/6, -alto/6);
        cabina.lineTo(ancho/6, alto/6);
        cabina.lineTo(-ancho/6, alto/6);
        cabina.closePath();
        
        g2d.setColor(colorSecundario);
        g2d.fill(cabina);
        
        // Casco del piloto
        g2d.setColor(colorDetalles);
        g2d.fillOval(-ancho/10, -alto/8, ancho/5, alto/5);
        
        // Alerones y detalles
        // Alerón delantero
        g2d.setColor(colorSecundario);
        g2d.fillRect(-ancho/2-3, -alto/2-3, ancho+6, 5);
        
        // Detalles del alerón delantero
        g2d.setColor(colorDetalles);
        g2d.fillRect(-ancho/2-3, -alto/2-3, 3, 5);
        g2d.fillRect(ancho/2, -alto/2-3, 3, 5);
        
        // Alerón trasero
        g2d.setColor(colorSecundario);
        g2d.fillRect(-ancho/2-2, alto/2, ancho+4, 6);
        
        // Soporte del alerón trasero
        g2d.setColor(colorPrincipal);
        g2d.fillRect(-ancho/6, alto/3, ancho/3, alto/6);
        
        // Ruedas
        g2d.setColor(Color.BLACK);
        // Ruedas delanteras
        g2d.fillRoundRect(-ancho/2-4, -alto/3, 8, 12, 3, 3);
        g2d.fillRoundRect(ancho/2-4, -alto/3, 8, 12, 3, 3);
        // Ruedas traseras
        g2d.fillRoundRect(-ancho/2-5, alto/6, 10, 15, 3, 3);
        g2d.fillRoundRect(ancho/2-5, alto/6, 10, 15, 3, 3);
        
        // Llantas
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillOval(-ancho/2-2, -alto/3+3, 4, 6);
        g2d.fillOval(ancho/2-2, -alto/3+3, 4, 6);
        g2d.fillOval(-ancho/2-2, alto/6+5, 4, 6);
        g2d.fillOval(ancho/2-2, alto/6+5, 4, 6);
        
        // Número del coche
        g2d.setColor(Color.WHITE);
        g2d.drawString("1", -3, 0);
        
        // Si estamos fuera de la pista, mostrar un indicador
        if (!enPista && !enPits) {
            g2d.setColor(new Color(255, 0, 0, 128));
            g2d.fillOval(-ancho/2 - 5, -alto/2 - 5, ancho + 10, alto + 10);
        }
        
        // Si estamos en pits, mostrar un indicador
        if (enPits) {
            g2d.setColor(new Color(255, 255, 0, 128));
            g2d.fillOval(-ancho/2 - 5, -alto/2 - 5, ancho + 10, alto + 10);
        }
        
        // Restaurar la transformación original
        g2d.setTransform(transformOriginal);
    }
    
    // Control del coche
    public void acelerar() {
        acelerando = true;
        frenando = false;
    }
    
    public void soltarAcelerador() {
        acelerando = false;
    }
    
    public void frenar() {
        frenando = true;
        acelerando = false;
    }
    
    public void girarIzquierda() {
        // Mejorada la capacidad de giro a baja velocidad
        if (Math.abs(velocidad) > 0.05) { // Reducido de 0.1 a 0.05 para permitir giros a menor velocidad
            double factorGiro = Math.min(1.0, Math.abs(velocidad) / 2.0); // Factor de giro proporcional a la velocidad
            angulo -= maniobrabilidad * (velocidad > 0 ? 1 : -1) * factorGiro;
        }
    }
    
    public void girarDerecha() {
        // Mejorada la capacidad de giro a baja velocidad
        if (Math.abs(velocidad) > 0.05) { // Reducido de 0.1 a 0.05 para permitir giros a menor velocidad
            double factorGiro = Math.min(1.0, Math.abs(velocidad) / 2.0); // Factor de giro proporcional a la velocidad
            angulo += maniobrabilidad * (velocidad > 0 ? 1 : -1) * factorGiro;
        }
    }
    
    public void dibujarStatico(Graphics2D g2d) {
        // Este método dibujará el coche sin aplicar rotaciones ni transformaciones
        // para la pantalla de podio
        // Copiar el código básico de dibujo del coche desde el método dibujar
        // pero eliminar las transformaciones según posición en la pista
        
        // Dibujar carrocería
        g2d.setColor(new Color(255, 0, 0)); // Rojo
        g2d.fillRoundRect(-20, -10, 40, 20, 8, 8);
        
        // Dibujar alerón delantero
        g2d.setColor(new Color(50, 50, 50));
        g2d.fillRect(-25, -8, 5, 16);
        
        // Dibujar alerón trasero
        g2d.setColor(new Color(50, 50, 50));
        g2d.fillRect(15, -8, 10, 16);
        
        // Dibujar cabina
        g2d.setColor(new Color(100, 100, 100));
        g2d.fillOval(-5, -7, 14, 14);
        
        // Dibujar ruedas
        g2d.setColor(Color.BLACK);
        g2d.fillOval(-15, -12, 10, 6); // Rueda delantera izquierda
        g2d.fillOval(-15, 6, 10, 6);  // Rueda delantera derecha
        g2d.fillOval(5, -12, 10, 6);   // Rueda trasera izquierda
        g2d.fillOval(5, 6, 10, 6);    // Rueda trasera derecha
    }
    
    public static int getPenalizacion() {
        return Penalizacion;
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
    
    public void setInicioPosicion(double x, double y) {
        this.inicioX = x;
        this.inicioY = y;
    }
    
    public void detener() {
        this.velocidad = 0;
        this.contadorAceleracion = 0;
    }
    
    public boolean estaEnPista() {
        return enPista;
    }
    
    public double getVelocidad() {
        return velocidad;
    }
    
    public double getAngulo() {
        return angulo;
    }
    
    public void setAngulo(double angulo) {
        this.angulo = angulo;
    }
}