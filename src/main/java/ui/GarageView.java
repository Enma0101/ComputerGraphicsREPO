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
        this.setResizable(false);
        this.setLayout(new BorderLayout());

       
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTitulo.setBackground(ColorMain);

        label = new JLabel("Garage - Team - " + EquipoSeleccionado);
        label.setForeground(ColorSen);
        label.setFont(Main.GLOBAL_FONT);
        panelTitulo.add(label);
        this.add(panelTitulo, BorderLayout.NORTH);


        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(ColorMain);
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT, 100, 850));

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

        // Panel de botones a la derecha
        JPanel panelBotonDerecho = new JPanel();
        panelBotonDerecho.setBackground(ColorMain);
        panelBotonDerecho.setLayout(new FlowLayout(FlowLayout.RIGHT, 100, 850));

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

        panelBotones.add(buttonBack);
        panelBotonDerecho.add(buttonModificar);

        this.add(panelBotones, BorderLayout.EAST);
        this.add(panelBotonDerecho, BorderLayout.WEST);

        // Panel Central con margen
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BorderLayout());
        panelCentral.setBackground(ColorMain);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(50, 0, 200, 0));  // Margen en el panel central

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
    public void mouseClicked(MouseEvent e) {
    	
    }

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
        // Restaurar la apariencia del botón cuando el ratón sale
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

