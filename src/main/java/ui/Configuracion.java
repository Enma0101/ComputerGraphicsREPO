package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import database.UsuarioDAO;
import main.Main;
import utils.MusicaFondo;

//Ajustes del juego.

public class Configuracion extends JDialog {
    private JComboBox<String> equipoComboBox;
    private JButton guardarButton;
    private JButton cambiarEquipoButton;  
    private JCheckBox sonidoCheckBox;  
    private UsuarioDAO usuarioDAO;
    private String username;
    private MusicaFondo MusicaFondo;

    public Configuracion(JFrame parent, String username,  MusicaFondo musicaFondo) {
        super(parent, "Configuración", true);
        this.username = username;
        this.usuarioDAO = new UsuarioDAO();
        this.MusicaFondo = musicaFondo;
        
        setSize(1200, 750);
        setLocationRelativeTo(parent);
        setUndecorated(true);
        
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, Color.BLACK, getWidth(), getHeight(), new Color(30, 30, 30)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("CONFIGURACIÓN");
        titleLabel.setFont(Main.GLOBAL_FONT.deriveFont(15f));
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Checkbox para habilitar/deshabilitar sonido
        sonidoCheckBox = new JCheckBox("Habilitar sonido");
        sonidoCheckBox.setFont(Main.GLOBAL_FONT.deriveFont(15f));
        sonidoCheckBox.setForeground(Color.WHITE);
        sonidoCheckBox.setOpaque(false);
        sonidoCheckBox.setSelected(MusicaFondo.isPlaying());
        sonidoCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        cambiarEquipoButton = createStyledButton("CAMBIAR EQUIPO");
        cambiarEquipoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    SeleccionEquipo seleccionEquipo = new SeleccionEquipo(username, true, MusicaFondo ); // Pasamos true para indicar cambio de equipo
                    seleccionEquipo.setVisible(true);
                });
                dispose();
            }
        });
        guardarButton = createStyledButton("GUARDAR");
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarConfiguracion();
            }
        });
        
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(sonidoCheckBox);  // Checkbox para sonido
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(cambiarEquipoButton);  // Botón para cambiar equipo
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(guardarButton);
        
        add(mainPanel);
    }
    
    
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(Main.GLOBAL_FONT.deriveFont(15f));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(new Color(200, 0, 0), 4, true));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 50));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(255, 215, 0));
                button.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 4, true));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createLineBorder(new Color(200, 0, 0), 4, true));
            }
        });
        return button;
    }
    
    private void guardarConfiguracion() {
        boolean sonidoHabilitado = sonidoCheckBox.isSelected();
        
        if (sonidoHabilitado) {
            // Enable sound - play menu music
            MusicaFondo.reproducirMusicaMenu();
        } else {
            // Disable sound - stop all music
            MusicaFondo.detenerMusica();
        }
  
        
        JOptionPane.showMessageDialog(this, "Configuración guardada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}