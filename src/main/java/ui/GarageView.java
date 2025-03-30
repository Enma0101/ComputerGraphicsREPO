package ui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

    public GarageView(String EquipoSeleccionado) {

        this.setSize(1200,750);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().setBackground(ColorMain);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setResizable(true); 
        this.setLayout(new BorderLayout());

        // Panel Título (Encabezado)
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTitulo.setBackground(ColorMain);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(50, 0, 20, 0));  // Separación del borde superior

        label = new JLabel("Garage  Team  " + EquipoSeleccionado);
        label.setForeground(ColorSen);
        label.setFont(Main.GLOBAL_FONT);
        panelTitulo.add(label);
        this.add(panelTitulo, BorderLayout.NORTH);

        // Panel de Botones
        JPanel panelBotones = new JPanel(new GridBagLayout());
        panelBotones.setBackground(ColorMain);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 200, 40, 200); 
        
        // Botón "Back"
        buttonBack = new JButton("Back menu principal");
        buttonBack.setBackground(ColorSen);
        buttonBack.setForeground(ColorMain);
        buttonBack.setFont(Main.GLOBAL_FONT2);
        buttonBack.setFocusPainted(false);
        buttonBack.setContentAreaFilled(false);
        buttonBack.setOpaque(true);
        buttonBack.setFocusable(false);
        buttonBack.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        buttonBack.setBorder(BorderFactory.createCompoundBorder(
                buttonBack.getBorder(),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        buttonBack.addMouseListener(this);

        // Botón "Modificar Auto"
        buttonModificar = new JButton("Modificar Auto");
        buttonModificar.setBackground(ColorSen);
        buttonModificar.setForeground(ColorMain);
        buttonModificar.setFont(Main.GLOBAL_FONT2);
        buttonModificar.setFocusPainted(false);
        buttonModificar.setContentAreaFilled(false);
        buttonModificar.setOpaque(true);
        buttonModificar.setFocusable(false);
        buttonModificar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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

        this.add(panelBotones, BorderLayout.SOUTH);

        // Panel Central
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));  
        panelCentral.setBackground(ColorMain);

        // Imagen del auto
        label2 = new JLabel();
        URL imageUrl = getClass().getResource("/resources/Imagens/" + "Ferrari" + "/Ferrari_Car.jpg");
        ImageIcon imageIcon = new ImageIcon(imageUrl);
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(500, 400, Image.SCALE_SMOOTH);
        label2.setIcon(new ImageIcon(scaledImage));

        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(Box.createVerticalGlue());
        panelCentral.add(label2);
        panelCentral.add(Box.createVerticalGlue());

        this.add(panelCentral, BorderLayout.CENTER);

        // Panel de Estadísticas
        JPanel panelStats = new JPanel();
        panelStats.setLayout(new BoxLayout(panelStats, BoxLayout.Y_AXIS));
        panelStats.setBackground(ColorMain);
        panelStats.setBorder(BorderFactory.createEmptyBorder(100, 100, 0, 50)); // Ajustamos los márgenes

        stad = new JLabel("      ESTADISTICAS - VEHICULO ");
        Agarre = new JLabel("      Agarre :       Excelente");
        VelocidadMAX = new JLabel("    Velocidad Max :  350 km/h");
        Potencia = new JLabel("      Potencia :     1000 HP");
        Peso = new JLabel("       Peso : 	       740 kg");

        // Fuente para las estadísticas
        Font fontStats = Main.GLOBAL_FONT2;
        stad.setFont(fontStats);
        stad.setForeground(Color.white);
        stad.setAlignmentX(Component.LEFT_ALIGNMENT);  // Alineación de las etiquetas hacia la izquierda
        Agarre.setFont(fontStats);
        Agarre.setForeground(Color.white);
        Agarre.setAlignmentX(Component.LEFT_ALIGNMENT);
        VelocidadMAX.setFont(fontStats);
        VelocidadMAX.setForeground(Color.white);
        VelocidadMAX.setAlignmentX(Component.LEFT_ALIGNMENT);
        Potencia.setFont(fontStats);
        Potencia.setForeground(Color.white);
        Potencia.setAlignmentX(Component.LEFT_ALIGNMENT);
        Peso.setFont(fontStats);
        Peso.setForeground(Color.white);
        Peso.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Agregamos las etiquetas al panel
        panelStats.add(stad);
        panelStats.add(Box.createVerticalStrut(30));  // Espacio entre las etiquetas
        panelStats.add(Agarre);
        panelStats.add(Box.createVerticalStrut(20));
        panelStats.add(VelocidadMAX);
        panelStats.add(Box.createVerticalStrut(20));
        panelStats.add(Potencia);
        panelStats.add(Box.createVerticalStrut(20));
        panelStats.add(Peso);

        // Ajustamos el layout del panel central
        panelCentral.setLayout(new BorderLayout());
        panelCentral.add(panelStats, BorderLayout.WEST);
        panelCentral.add(label2, BorderLayout.CENTER);

        this.setVisible(true);
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
