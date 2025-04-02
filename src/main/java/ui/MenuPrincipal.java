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
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import main.Main;
import utils.MusicaFondo;



public class MenuPrincipal extends JFrame {
	private JDialog dialog = null;
	private String username;
	public MusicaFondo MusicaFondo;
	

	 
    public MenuPrincipal(String username, MusicaFondo musicaFondo) {
    	this.username = username;
 this.setSize(1200,750);
    	this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setUndecorated(true);
        this.setLayout(new BorderLayout());
        this.MusicaFondo = musicaFondo;
        
  
      
       
        
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
        titleLabel.setFont(Main.GLOBAL_FONT.deriveFont(Font.BOLD, 60f));
        titleLabel.setForeground(new Color(255, 215, 0)); // Dorado
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(50, 0, 50, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Botones
        JButton[] buttons = {
            createMenuButton("CARRERA"),
            createMenuButton("GARAGE"),
            createMenuButton("CONFIGURACIÓN"),
            createMenuButton("SALIR")
        };
        
        // Añadir funcionalidad a los botones
        setupButtonActions(buttons);
        
        // Añadir componentes
        mainPanel.add(titleLabel);
        for (JButton button : buttons) {
            mainPanel.add(button);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }
        
        this.add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupButtonActions(JButton[] buttons) {

    	
    	buttons[0].addActionListener(e -> {
    	    SwingUtilities.invokeLater(() -> {
    	        try {
					openCircuitoF1();
						
				} catch (SQLException e1) {
					
					e1.printStackTrace();
				}
    	    });
    	});
    	
    	

       
    	 

        buttons[1].addActionListener(e -> {
            
            SwingUtilities.invokeLater(() -> {
            	openGarageView();
               
            });
        });
        
        
		buttons[2].addActionListener(e -> {
		            SwingUtilities.invokeLater(() -> {
		            	openConfiguraciones();
		           
		            });
		        });
        
     
        
        
        
        // Botón SALIR
        buttons[3].addActionListener(e -> System.exit(0));
    }
    
    // Estilo de los botones
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
      
        button.setFont(Main.GLOBAL_FONT.deriveFont(20f));
        button.setForeground(Color.WHITE);
        button.setBorder(new LineBorder(new Color(200, 0, 0), 4, true));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(400, 70));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        
        // Efecto hover
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(255, 215, 0)); // Dorado
                button.setBorder(new LineBorder(new Color(255, 215, 0), 4, true));
            }
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE);
                button.setBorder(new LineBorder(new Color(200, 0, 0), 4, true));
            }
        });
        
        return button;
    }
    
    
    private void openGarageView() {
        if (dialog == null || !dialog.isVisible()) {
            GarageView garageView = new GarageView(username,MusicaFondo);
            dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Pit Stop", true);
            dialog.setContentPane(garageView);
            dialog.setSize(1200, 750);
            dialog.setLocationRelativeTo(this);
            dialog.setUndecorated(true);
            dialog.setVisible(true);
        }
    }


    private void openCircuitoF1() throws SQLException {
    	
    	  MusicaFondo.detenerMusicaFondo();;
        CircuitoF1 circuitoF1 = new CircuitoF1(username,MusicaFondo);
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Carrera", true);
        dialog.setContentPane(circuitoF1);
      dialog.setSize(1200, 750);
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(true);
        dialog.setVisible(true);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
              
                dialog.dispose();
            }
        });
    }
        
    private void openConfiguraciones() {
        if (dialog == null || !dialog.isVisible()) {
            Configuracion configuracion = new Configuracion(this, username, MusicaFondo);
            configuracion.setSize(1200, 750); 
            configuracion.setLocationRelativeTo(this);
            configuracion.setVisible(true);
        }
    }

        
    }


    

    
  
    
    


