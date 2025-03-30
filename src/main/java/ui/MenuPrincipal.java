package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import database.UsuarioDAO;

//Opciones del juego.

public class MenuPrincipal extends JFrame {
	private String username;
	private UsuarioDAO usuarioDAO;
	
    public MenuPrincipal(String username) {
    	this.username = username;
    	this.usuarioDAO = new UsuarioDAO();
    	
        // Configuración de la ventana
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Menu");
        this.setLayout(new BorderLayout());
        
        // Panel principal con fondo negro
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                // Gradient paint de negro a gris
                g2d.setPaint(new GradientPaint(0, 0, Color.BLACK, getWidth(), getHeight(), new Color(30, 30, 30)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        // Box layout vertical
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); 
        mainPanel.setBackground(Color.BLACK);
        
        // Título 
        JLabel titleLabel = new JLabel("RETRO RACER");
        titleLabel.setFont(new Font("Press Start 2P", Font.BOLD, 60));
        titleLabel.setForeground(new Color(255, 215, 0)); // Dorado
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(50, 0, 50, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Constante para alinear en centro puq no deja
        
        // Botones 
        JButton carreraButton = createMenuButton("CARRERA");
        JButton garageButton = createMenuButton("GARAGE");
        JButton historialButton = createMenuButton("HISTORIAL");
        JButton configButton = createMenuButton("CONFIGURACIÓN");
        JButton salirButton = createMenuButton("SALIR");
        

        // Abrir el garage con el equipo del usuario
        garageButton.addActionListener(e -> {
            String equipo = usuarioDAO.obtenerEquipoUsuario(username);
            if (equipo != null && !equipo.isEmpty()) {
                new GarageView(equipo).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "No tienes un equipo asignado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Acción para salir
        salirButton.addActionListener(e -> System.exit(0));
        
        // Componentes
        mainPanel.add(titleLabel);
        mainPanel.add(carreraButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(garageButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(historialButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(configButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(salirButton);
        
        this.add(mainPanel, BorderLayout.CENTER);
    }
    
    	// Estilo de los botones
    	private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Press Start 2P", Font.PLAIN, 24));
        button.setForeground(Color.WHITE);
        button.setBorder(new LineBorder(new Color(200, 0, 0), 4, true));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(400, 70));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(255, 215, 0));
                button.setBorder(new LineBorder(new Color(255, 215, 0), 4, true));
            }
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE);
                button.setBorder(new LineBorder(new Color(200, 0, 0), 4, true));
            }
        });
        
        return button;
    }
}
