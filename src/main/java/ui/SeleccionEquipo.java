package ui;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import database.UsuarioDAO;

public class SeleccionEquipo extends JFrame {
    private String username;
    private UsuarioDAO usuarioDAO;
    private String[] equipos = {"Ferrari", "Red Bull", "Mercedes", "McLaren", "Aston Martin"};
    
    public SeleccionEquipo(String username) {
        this.username = username;
        this.usuarioDAO = new UsuarioDAO();
        
        // Verificar si el usuario ya tiene un equipo asignado
        String equipoActual = usuarioDAO.obtenerEquipoUsuario(username);
      
        // Si ya tiene un equipo, ir directamente al menú principal
        if (equipoActual != null && !equipoActual.isEmpty()) {
        	  System.out.print(equipoActual);
            new MenuPrincipal(username).setVisible(true);
            dispose();
            return;
        }
        
        // Configuración básica del JFrame
        this.setSize(1200, 750);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setUndecorated(true); 
        this.setLayout(new BorderLayout());
        
        // Panel principal con fondo degradado
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
        mainPanel.setBorder(new EmptyBorder(50, 0, 50, 0));
        
        // Título con estilo coherente con MenuPrincipal y Login
        JLabel titleLabel = new JLabel("SELECCIONA TU EQUIPO");
        titleLabel.setFont(new Font("Press Start 2P", Font.BOLD, 40));
        titleLabel.setForeground(new Color(255, 215, 0)); // Dorado como en MenuPrincipal
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(30, 0, 50, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        mainPanel.add(titleLabel);
        
        // Panel para los botones de equipos
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        // Crear botones para cada equipo
        for (String equipo : equipos) {
            JButton teamButton = createTeamButton(equipo);
            buttonPanel.add(teamButton);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        
        mainPanel.add(buttonPanel);
        
        // Agregar botón para volver atrás
        JButton backButton = createMenuButton("VOLVER");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login().setVisible(true);
                dispose();
            }
        });
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(backButton);
        
        this.add(mainPanel, BorderLayout.CENTER);
    }
    
    private JButton createTeamButton(String equipo) {
        JButton button = new JButton(equipo) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, new Color(50, 50, 50), getWidth(), getHeight(), new Color(20, 20, 20)));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Press Start 2P", Font.PLAIN, 20));
        button.setForeground(Color.WHITE);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(400, 60));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        
        // Determinar color según el equipo
        Color teamColor;
        switch(equipo) {
            case "Ferrari":
                teamColor = new Color(220, 0, 0); // Rojo Ferrari
                break;
            case "Red Bull":
                teamColor = new Color(6, 0, 239); // Azul Red Bull
                break;
            case "Mercedes":
                teamColor = new Color(0, 210, 190); // Turquesa Mercedes
                break;
            case "McLaren":
                teamColor = new Color(255, 135, 0); // Naranja McLaren
                break;
            case "Aston Martin":
                teamColor = new Color(0, 111, 60); // Verde Aston Martin
                break;
            default:
                teamColor = new Color(200, 0, 0); // Rojo por defecto
        }
        
        button.setBorder(BorderFactory.createLineBorder(teamColor, 4, true));
        
        // Efecto hover similar al MenuPrincipal
        final Color finalTeamColor = teamColor;
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(255, 215, 0)); // Dorado
                button.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 4, true));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createLineBorder(finalTeamColor, 4, true));
            }
        });
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usuarioDAO.asignarEquipoUsuario(username, equipo);
                JOptionPane.showMessageDialog(null, "Has seleccionado " + equipo, "Equipo Asignado", JOptionPane.INFORMATION_MESSAGE);
                new MenuPrincipal(username).setVisible(true);
                dispose();
            }
        });
        
        return button;
    }
    
    // Método para crear botón de volver, con estilo consistente con MenuPrincipal
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, new Color(50, 50, 50), getWidth(), getHeight(), new Color(20, 20, 20)));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Press Start 2P", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(new Color(200, 0, 0), 4, true));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(300, 50));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        
        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(255, 215, 0)); // Dorado
                button.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 4, true));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createLineBorder(new Color(200, 0, 0), 4, true));
            }
        });
        
        return button;
    }
}