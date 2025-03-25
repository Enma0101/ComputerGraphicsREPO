package ui;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import main.Main;

public class GarageView extends JFrame {
    
    String ColorPrincipal = "188, 24, 35";
    String Colorsecundario = "255, 242, 0";
    String EquipoSeleccionado = "Ferrari";
    Color ColorMain = stringToColor(ColorPrincipal);
    Color ColorSen = stringToColor(Colorsecundario);
    JLabel label;
    JButton button;

    public GarageView() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(ColorMain);
        this.setResizable(false);
        this.setLayout(null);
        
        // Labels
        label = new JLabel("Garage - Team - " + EquipoSeleccionado);
        label.setBackground(ColorSen);
        label.setForeground(ColorSen);
        label.setFont(Main.GLOBAL_FONT);
        label.setBounds(500, 10, 800, 100);
        this.add(label);
        
        // Botón
        button = new JButton("Menu principal");
        button.setBackground(ColorSen);  
        button.setForeground(ColorMain); 
        button.setFont(Main.GLOBAL_FONT_botton);
        button.setFocusPainted(false);  
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK));  

        // Redondear las esquinas utilizando un Border
        button.setBorder(BorderFactory.createCompoundBorder(
            button.getBorder(),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        //button.setContentAreaFilled(false);
        button.setOpaque(true);


        
        button.setBounds(700, 750, 200, 50);
        
        this.add(button);
    }

    private Color stringToColor(String rgb) {
        String[] rgbValues = rgb.split(","); 
        int red = Integer.parseInt(rgbValues[0].trim()); 
        int green = Integer.parseInt(rgbValues[1].trim()); 
        int blue = Integer.parseInt(rgbValues[2].trim()); 
        return new Color(red, green, blue); 
    }

}
