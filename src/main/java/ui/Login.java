package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import database.UsuarioDAO;
import main.Main;
import ui.GarageView;
//Selección de equipo y autenticación
import utils.MusicaFondo;

public class Login extends JFrame {
    private JTextField usernameField;
    private UsuarioDAO usuarioDAO;
	public MusicaFondo MusicaFondo;

    public Login(MusicaFondo musicaFondo) {
        this.usuarioDAO = new UsuarioDAO();
        this.setSize(1200,750);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Login");
        this.setLayout(new BorderLayout());
        this.MusicaFondo = musicaFondo;
       	


        // Panel principal con fondo negro
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
        mainPanel.setBackground(Color.BLACK);

        // Título
        JLabel titleLabel = new JLabel("SIGN IN");
        titleLabel.setFont(Main.GLOBAL_FONT);
        titleLabel.setForeground(new Color(255, 215, 0)); 
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Campo de usuario
        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(400, 50));
        usernameField.setFont(Main.GLOBAL_FONT.deriveFont(15f));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setBackground(Color.DARK_GRAY);
        usernameField.setForeground(Color.WHITE);
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(250, 60, 4), 3));

        // Botón de iniciar sesión
        JButton loginButton = createMenuButton("LOGIN");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();

                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese un nombre de usuario.");
                    return;
                }

                if (!usuarioDAO.esUsuarioValido(username)) {
                    JOptionPane.showMessageDialog(null, "El nombre de usuario debe tener entre 3 y 15 caracteres, sin espacios ni símbolos.");
                    return;
                }

                // Verificar si el usuario existe
                if (usuarioDAO.usuarioExiste(username)) {
                    String equipo = usuarioDAO.obtenerEquipoUsuario(username);

                    // Si tiene equipo asignado, lo lleva directamente al menú principal
                    if (equipo != null && !equipo.isEmpty()) {
                        new MenuPrincipal(username,MusicaFondo).setVisible(true);
                    } else {
                        // Si no tiene equipo asignado, lo lleva a la selección de equipo
                        new SeleccionEquipo(username , MusicaFondo).setVisible(true);
                    }
                } else {
                    // Si el usuario no existe, se registra
                    usuarioDAO.crearUsuario(username);
                    JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente.");

                    // Luego lo redirigimos a la selección de equipo
                    new SeleccionEquipo(username,MusicaFondo).setVisible(true);
                }

                dispose(); // Cerrar la ventana de login
            }
        });


        // Imagen 
        JLabel imageLabel = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/login.png"));
        Image img = icon.getImage().getScaledInstance(500, 250, Image.SCALE_SMOOTH); 
        imageLabel.setIcon(new ImageIcon(img));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
  
        // Agregar componentes al panel principal
        mainPanel.add(imageLabel);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(usernameField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(loginButton);
        
        this.add(mainPanel, BorderLayout.CENTER);
    }

    // Método para crear botones con estilo
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Press Start 2P", Font.PLAIN, 24));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(new Color(250, 60, 4), 4, true));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(400, 70));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);

        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(255, 215, 0)); 
                button.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 4, true));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createLineBorder(new Color(250, 60, 4), 4, true));
            }
        });

        return button;
    }
}