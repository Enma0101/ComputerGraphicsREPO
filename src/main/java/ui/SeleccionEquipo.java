package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import database.UsuarioDAO;

public class SeleccionEquipo extends JFrame {
    private String username;
    private UsuarioDAO usuarioDAO;
    private String[] equipos = {"Ferrari", "Red Bull", "Mercedes", "McLaren", "Aston Martin"};

    public SeleccionEquipo(String username) {
        this.username = username;
        this.usuarioDAO = new UsuarioDAO();
        
        setTitle("Seleccionar Equipo");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

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

        JLabel titleLabel = new JLabel("Selecciona tu equipo");
        titleLabel.setFont(new Font("Press Start 2P", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Crear botones para seleccionar equipo
        for (String equipo : equipos) {
            JButton teamButton = createTeamButton(equipo);
            mainPanel.add(teamButton);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createTeamButton(String equipo) {
        JButton button = new JButton(equipo);
        button.setFont(new Font("Press Start 2P", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(Color.DARK_GRAY);
        button.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(300, 50));
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usuarioDAO.asignarEquipoUsuario(username, equipo);
                JOptionPane.showMessageDialog(null, "Has seleccionado " + equipo);
                new GarageView(equipo).setVisible(true);
                dispose();
            }
        });
        
        return button;
    }
}

