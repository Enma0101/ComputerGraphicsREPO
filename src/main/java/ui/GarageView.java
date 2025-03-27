package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.*;

import main.Main;

public class GarageView extends JFrame implements MouseListener {

    String ColorPrincipal = "188, 24, 35";
    String Colorsecundario = "255, 242, 0";
    String EquipoSeleccionado = "Ferrari";
    Color ColorMain = stringToColor(ColorPrincipal);
    Color ColorSen = stringToColor(Colorsecundario);
    
    JLabel label, label2, Agarre, VelocidadMAX, Potencia, Peso, stad;
    JButton buttonBack, buttonModificar;

    // Door animation components
    private JLabel panelPuerta;
    private int puertaY = 0; 
    private Timer animationTimer;

    public GarageView() {
        // Panel de contenido personalizado con patrón de fibra de carbono
        JPanel contentPanelWithTexture = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Crear imagen de fondo con patrón de fibra de carbono
                BufferedImage backgroundTexture = createCarbonFiberTexture(getWidth(), getHeight());
                g2d.drawImage(backgroundTexture, 0, 0, this);
            }
        };
        contentPanelWithTexture.setLayout(new BorderLayout());
        
        // Establecer el panel de contenido personalizado
        setContentPane(contentPanelWithTexture);
        
        this.setSize(1200, 750);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        panelPuerta = new JLabel();
        panelPuerta.setBounds(0, puertaY, 1200, 750);
        
        URL doorImageUrl = getClass().getResource("/resources/Imagens/garage-door.jpg");
        if (doorImageUrl != null) {
            ImageIcon doorIcon = new ImageIcon(doorImageUrl);
            Image doorImage = doorIcon.getImage().getScaledInstance(1200, 750, Image.SCALE_SMOOTH);
            panelPuerta.setIcon(new ImageIcon(doorImage));
        } else {
            panelPuerta.setBackground(Color.DARK_GRAY);
            panelPuerta.setOpaque(true);
        }
        
        contentPanelWithTexture.add(panelPuerta, BorderLayout.CENTER);
    
        initGarageComponents(contentPanelWithTexture);

        startDoorAnimation();
    }

    private BufferedImage createCarbonFiberTexture(int width, int height) {
        BufferedImage texture = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = texture.createGraphics();
        
        // Color base ligeramente más oscuro derivado del color principal
        Color baseColor = new Color(
            Math.max(0, ColorMain.getRed() - 50), 
            Math.max(0, ColorMain.getGreen() - 50), 
            Math.max(0, ColorMain.getBlue() - 50)
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
    
    private void initGarageComponents(JPanel parentPanel) {
        // Crear paneles con fondo transparente
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTitulo.setOpaque(false);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(50, 0, 20, 0));

        label = new JLabel("Garage " + EquipoSeleccionado);
        label.setForeground(ColorSen);
        label.setFont(Main.GLOBAL_FONT);
        panelTitulo.add(label);
        parentPanel.add(panelTitulo, BorderLayout.NORTH);

        // Panel de botones
        JPanel panelBotones = new JPanel(new GridBagLayout());
        panelBotones.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 200, 40, 200); 
        
        // Botón Back
        buttonBack = new JButton("Back menu principal");
        buttonBack.setBackground(ColorSen);
        buttonBack.setForeground(ColorMain);
        buttonBack.setFont(Main.GLOBAL_FONT2);
        buttonBack.setFocusPainted(false);
        buttonBack.setContentAreaFilled(false);
        buttonBack.setOpaque(true);
        buttonBack.setFocusable(false);
        buttonBack.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
        buttonBack.setBorder(BorderFactory.createCompoundBorder(
                buttonBack.getBorder(),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        buttonBack.addMouseListener(this);

        // Botón Modificar
        buttonModificar = new JButton("Modificar Auto");
        buttonModificar.setBackground(ColorSen);
        buttonModificar.setForeground(ColorMain);
        buttonModificar.setFont(Main.GLOBAL_FONT2);
        buttonModificar.setFocusPainted(false);
        buttonModificar.setContentAreaFilled(false);
        buttonModificar.setOpaque(true);
        buttonModificar.setFocusable(false);
        buttonModificar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
        buttonModificar.setBorder(BorderFactory.createCompoundBorder(
                buttonModificar.getBorder(),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        buttonModificar.addMouseListener(this);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panelBotones.add(buttonBack, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelBotones.add(buttonModificar, gbc);

        parentPanel.add(panelBotones, BorderLayout.SOUTH);

        // Panel Central
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BorderLayout());
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Reducir espaciado superior

        label2 = new JLabel();
        URL imageUrl = getClass().getResource("/resources/Imagens/" + EquipoSeleccionado + "/Car.png");
        ImageIcon imageIcon = new ImageIcon(imageUrl);
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(650, 500, Image.SCALE_SMOOTH);
        label2.setIcon(new ImageIcon(scaledImage));
        label2.setBorder(BorderFactory.createEmptyBorder(0, 0, 70, 0));
        label2.setHorizontalAlignment(JLabel.CENTER);
  

        // Panel de Estadísticas
        JPanel panelStats = new JPanel();
        panelStats.setLayout(new BoxLayout(panelStats, BoxLayout.Y_AXIS));
        panelStats.setOpaque(false);
        panelStats.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 50)); 
        

        stad = new JLabel("      ESTADISTICAS - VEHICULO ");
        Agarre = new JLabel("      Agarre :       Excelente");
        VelocidadMAX = new JLabel("    Velocidad Max :  350 km/h");
        Potencia = new JLabel("      Potencia :     1000 HP");
        Peso = new JLabel("       Peso : 	       740 kg");

        Font fontStats = Main.GLOBAL_FONT2;
        stad.setFont(fontStats);
        stad.setForeground(Color.white);
        Agarre.setFont(fontStats);
        Agarre.setForeground(Color.white);
        VelocidadMAX.setFont(fontStats);
        VelocidadMAX.setForeground(Color.white);
        Potencia.setFont(fontStats);
        Potencia.setForeground(Color.white);
        Peso.setFont(fontStats);
        Peso.setForeground(Color.white);

        panelStats.add(stad);
        panelStats.add(Box.createVerticalStrut(30));
        panelStats.add(Agarre);
        panelStats.add(Box.createVerticalStrut(20));
        panelStats.add(VelocidadMAX);
        panelStats.add(Box.createVerticalStrut(20));
        panelStats.add(Potencia);
        panelStats.add(Box.createVerticalStrut(20));
        panelStats.add(Peso);

        
     // Ajusta estos parámetros en la sección de inicialización del logo
        JLabel logoLabel = new JLabel();
        URL logoUrl = getClass().getResource("/resources/Imagens/"+ EquipoSeleccionado  + "/logo.png");

        if (logoUrl != null) {
            ImageIcon logoIcon = new ImageIcon(logoUrl);
            Image logoImage = logoIcon.getImage().getScaledInstance(250, 150, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(logoImage));
            
            // Configura márgenes más ajustados
            logoLabel.setBorder(BorderFactory.createEmptyBorder(50, 100, 0, 0));
    
        }
        
    
     
        panelStats.add(logoLabel);

  

        panelCentral.add(panelStats, BorderLayout.WEST);
        panelCentral.add(label2, BorderLayout.CENTER);
        

        parentPanel.add(panelCentral, BorderLayout.CENTER);

        this.setVisible(true);
    }
   
    private void startDoorAnimation() {
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

    private Color stringToColor(String rgb) {
        String[] rgbValues = rgb.split(",");
        int red = Integer.parseInt(rgbValues[0].trim());
        int green = Integer.parseInt(rgbValues[1].trim());
        int blue = Integer.parseInt(rgbValues[2].trim());
        return new Color(red, green, blue);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == buttonBack) {
            buttonBack.setBackground(Color.white);
        } else if (e.getSource() == buttonModificar) {
            buttonModificar.setBackground(Color.white);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource() == buttonBack) {
            buttonBack.setBackground(ColorSen);
            buttonBack.setForeground(ColorMain);
        } else if (e.getSource() == buttonModificar) {
            buttonModificar.setBackground(ColorSen);
            buttonModificar.setForeground(ColorMain);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == buttonBack) {
            buttonBack.setFont(new Font(buttonBack.getFont().getName(), Font.BOLD, 20));
            buttonBack.setPreferredSize(new Dimension(200, 50));
        } else if (e.getSource() == buttonModificar) {
            buttonModificar.setFont(new Font(buttonModificar.getFont().getName(), Font.BOLD, 20));
            buttonModificar.setPreferredSize(new Dimension(200, 50));
        }
        e.getComponent().revalidate();
        e.getComponent().repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == buttonBack) {
            buttonBack.setFont(new Font(buttonBack.getFont().getName(), Font.BOLD, 16));
            buttonBack.setPreferredSize(new Dimension(250, 50));
        } else if (e.getSource() == buttonModificar) {
            buttonModificar.setFont(new Font(buttonModificar.getFont().getName(), Font.BOLD, 16));
            buttonModificar.setPreferredSize(new Dimension(250, 50));
        }
        e.getComponent().revalidate();
        e.getComponent().repaint();
    }
}