package ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.*;

import graphics.DoorAnimation;
import main.Main;


public class GarageView extends JPanel {
	
    private String ColorPrincipal = "188, 24, 35";
    private String Colorsecundario = "255, 242, 0";
    private String EquipoSeleccionado = "Ferrari";
    private Color ColorMain = stringToColor(ColorPrincipal);
    private Color ColorSen = stringToColor(Colorsecundario);



    
    private JLabel label, label2, Agarre, VelocidadMAX, Potencia, Peso, stad;
    private JButton buttonBack, buttonModificar;
    private DoorAnimation doorAnimation;

    public GarageView() {
        setLayout(new BorderLayout());
        doorAnimation = new DoorAnimation(ColorMain);
     
        JPanel contentPanelWithTexture = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                BufferedImage backgroundTexture = doorAnimation.createCarbonFiberTexture(getWidth(), getHeight());
                g2d.drawImage(backgroundTexture, 0, 0, this);
            }
        };
        contentPanelWithTexture.setLayout(new BorderLayout());
        
        add(contentPanelWithTexture, BorderLayout.CENTER);

        JLabel panelPuerta = doorAnimation.createDoorPanel(1200, 750);
        contentPanelWithTexture.add(panelPuerta, BorderLayout.CENTER);

        initGarageComponents(contentPanelWithTexture);
        doorAnimation.startDoorAnimation();
    }
    
    private void initGarageComponents(JPanel parentPanel) {
      
        JPanel panelTitulo = createTitlePanel();
        parentPanel.add(panelTitulo, BorderLayout.NORTH);

  
        JPanel panelBotones = createButtonPanel();
        parentPanel.add(panelBotones, BorderLayout.SOUTH);


        JPanel panelCentral = createCentralPanel();
        parentPanel.add(panelCentral, BorderLayout.CENTER);
    }

    private JPanel createTitlePanel() {
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTitulo.setOpaque(false);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(50, 0, 20, 0));

        label = new JLabel("Garage " + EquipoSeleccionado);
        label.setForeground(ColorSen);
        label.setFont(Main.GLOBAL_FONT);
        panelTitulo.add(label);
        
        return panelTitulo;
    }

    private JPanel createButtonPanel() {
        JPanel panelBotones = new JPanel(new GridBagLayout());
        panelBotones.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 200, 40, 200); 
        
        // Botón Back
        buttonBack = createStyledButton("Back menu principal");
        buttonBack.addMouseListener(createButtonMouseListener(buttonBack));

        // Botón Modificar
        buttonModificar = createStyledButton("Modificar Auto");
        buttonModificar.addMouseListener(createButtonMouseListener(buttonModificar));

        gbc.gridx = 1;
        gbc.gridy = 0;
        panelBotones.add(buttonBack, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelBotones.add(buttonModificar, gbc);
        
        return panelBotones;
    }

    public JButton getBackButton() {
        return buttonBack;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(ColorSen);
        button.setForeground(ColorMain);
        button.setFont(Main.GLOBAL_FONT2);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setFocusable(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
        button.setBorder(BorderFactory.createCompoundBorder(
                button.getBorder(),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        return button;
    }

    private MouseAdapter createButtonMouseListener(JButton button) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (button == buttonModificar) {
                    openPitStopView();
                }else if(button == buttonBack){

                    Window window = SwingUtilities.getWindowAncestor(GarageView.this);
                    if (window != null) {
                        window.dispose();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(Color.WHITE);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(ColorSen);
                button.setForeground(ColorMain);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setFont(new Font(button.getFont().getName(), Font.BOLD, 20));
                button.setPreferredSize(new Dimension(200, 50));
                button.revalidate();
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setFont(new Font(button.getFont().getName(), Font.BOLD, 16));
                button.setPreferredSize(new Dimension(250, 50));
                button.revalidate();
                button.repaint();
            }
        };
    }

    private void openPitStopView() {
        PitStopView pitStopView = new PitStopView(ColorPrincipal, EquipoSeleccionado, Colorsecundario);
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentWindow, "Pit Stop", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setContentPane(pitStopView);
        dialog.setSize(1200, 750);
        dialog.setLocationRelativeTo(parentWindow);
        dialog.setUndecorated(true);
        dialog.setVisible(true);
    }

    private JPanel createCentralPanel() {
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BorderLayout());
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Imagen del coche
        label2 = createCarLabel();

        // Panel de Estadísticas
        JPanel panelStats = createStatsPanel();

        panelCentral.add(panelStats, BorderLayout.WEST);
        panelCentral.add(label2, BorderLayout.CENTER);
        
        return panelCentral;
    }

    private JLabel createCarLabel() {
        JLabel label2 = new JLabel();
        URL imageUrl = getClass().getResource("/resources/Imagens/" + EquipoSeleccionado + "/Car.png");
        ImageIcon imageIcon = new ImageIcon(imageUrl);
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(650, 500, Image.SCALE_SMOOTH);
        label2.setIcon(new ImageIcon(scaledImage));
        label2.setBorder(BorderFactory.createEmptyBorder(0, 0, 70, 0));
        label2.setHorizontalAlignment(JLabel.CENTER);
        return label2;
    }

    private JPanel createStatsPanel() {
        JPanel panelStats = new JPanel();
        panelStats.setLayout(new BoxLayout(panelStats, BoxLayout.Y_AXIS));
        panelStats.setOpaque(false);
        panelStats.setBorder(BorderFactory.createEmptyBorder(80, 100, 0, 50)); 

        // Crear etiquetas de estadísticas
        Font fontStats = Main.GLOBAL_FONT2;
        
        stad = createStatLabel("ESTADÍSTICAS ACTUALES", fontStats, ColorSen);
        Agarre = createStatLabel("Agarre: Excelente", fontStats, Color.WHITE);
        VelocidadMAX = createStatLabel("Velocidad Max: 350 km/h", fontStats, Color.WHITE);
        Potencia = createStatLabel("Potencia: 1000 HP", fontStats, Color.WHITE);
        Peso = createStatLabel("Peso: 740 kg", fontStats, Color.WHITE);

        // Añadir etiquetas con espaciado
        panelStats.add(stad);
        panelStats.add(Box.createVerticalStrut(30));
        panelStats.add(Agarre);
        panelStats.add(Box.createVerticalStrut(20));
        panelStats.add(VelocidadMAX);
        panelStats.add(Box.createVerticalStrut(20));
        panelStats.add(Potencia);
        panelStats.add(Box.createVerticalStrut(20));
        panelStats.add(Peso);

        // Añadir logo
        JLabel logoLabel = createLogoLabel();
        panelStats.add(logoLabel);

        return panelStats;
    }

    private JLabel createStatLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    private JLabel createLogoLabel() {
        JLabel logoLabel = new JLabel();
        URL logoUrl = getClass().getResource("/resources/Imagens/"+ EquipoSeleccionado  + "/logo.png");

        if (logoUrl != null) {
            ImageIcon logoIcon = new ImageIcon(logoUrl);
            Image logoImage = logoIcon.getImage().getScaledInstance(250, 150, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(logoImage));
            
            // Configurar márgenes
            logoLabel.setBorder(BorderFactory.createEmptyBorder(50, 100, 0, 0));
        }
        
        return logoLabel;
    }

    private Color stringToColor(String rgb) {
        String[] rgbValues = rgb.split(",");
        int red = Integer.parseInt(rgbValues[0].trim());
        int green = Integer.parseInt(rgbValues[1].trim());
        int blue = Integer.parseInt(rgbValues[2].trim());
        return new Color(red, green, blue);
    }
}