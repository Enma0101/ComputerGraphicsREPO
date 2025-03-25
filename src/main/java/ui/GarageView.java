package ui;

import java.awt.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import main.Main;

public class GarageView extends JFrame implements MouseListener {

    String ColorPrincipal = "188, 24, 35";
    String Colorsecundario = "255, 242, 0";
    String EquipoSeleccionado = "Ferrari";
    Color ColorMain = stringToColor(ColorPrincipal);
    Color ColorSen = stringToColor(Colorsecundario);
    JLabel label;
    JButton buttonBack, buttonModificar;

    public GarageView() {

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.getContentPane().setBackground(ColorMain);
        this.setResizable(true); // Cambiar a 'true' para que la ventana sea redimensionable
        this.setLayout(new BorderLayout());

        // Panel de título
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTitulo.setBackground(ColorMain);

        label = new JLabel("Garage - Team - " + EquipoSeleccionado);
        label.setForeground(ColorSen);
        label.setFont(Main.GLOBAL_FONT);
        panelTitulo.add(label);
        this.add(panelTitulo, BorderLayout.NORTH);

        // Panel de botones (modificado para GridBagLayout)
        JPanel panelBotones = new JPanel(new GridBagLayout());
        panelBotones.setBackground(ColorMain);

        // Establecer restricciones para los botones
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 500, 50, 500); // Margen entre los botones

        // Botón "Back menu principal"
        buttonBack = new JButton("Back menu principal");
        buttonBack.setBackground(ColorSen);
        buttonBack.setForeground(ColorMain);
        buttonBack.setFont(Main.GLOBAL_FONT_botton);
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
        buttonModificar.setFont(Main.GLOBAL_FONT_botton);
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

        // Añadir los botones a la cuadrícula con restricciones
        gbc.gridx = 1;
        gbc.gridy = 0;
        panelBotones.add(buttonBack, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelBotones.add(buttonModificar, gbc);

        this.add(panelBotones, BorderLayout.SOUTH);

        // Panel Central con margen
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BorderLayout());
        panelCentral.setBackground(ColorMain);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(50, 300, 50, 300));  // Margen en el panel central

        JPanel panelImagen = new JPanel();
        panelImagen.setBackground(Color.darkGray);
        panelImagen.setLayout(new BorderLayout());
        panelCentral.add(panelImagen, BorderLayout.CENTER);

        this.add(panelCentral, BorderLayout.CENTER);

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
            buttonBack.setFont(new Font(buttonBack.getFont().getName(), Font.BOLD, 30));
            buttonBack.setPreferredSize(new Dimension(350, 70));
        } else if (e.getSource() == buttonModificar) {
            buttonModificar.setFont(new Font(buttonModificar.getFont().getName(), Font.BOLD, 30));
            buttonModificar.setPreferredSize(new Dimension(350, 70));
        }
        e.getComponent().revalidate();
        e.getComponent().repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == buttonBack) {
            buttonBack.setFont(new Font(buttonBack.getFont().getName(), Font.BOLD, 20));
            buttonBack.setPreferredSize(new Dimension(250, 50));
        } else if (e.getSource() == buttonModificar) {
            buttonModificar.setFont(new Font(buttonModificar.getFont().getName(), Font.BOLD, 20));
            buttonModificar.setPreferredSize(new Dimension(250, 50));
        }
        e.getComponent().revalidate();
        e.getComponent().repaint();
    }
}
