package ui;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.*;
import main.Main;

public class PitStopView extends JPanel {
    private final Color colorPrimary;
    private final Color colorSecondary;
    private final String EquipoSeleccionado;
    
    // Componentes de selección
    private JComboBox<String> engineCombo, tiresCombo, wingCombo;
    private JLabel gripStat, speedStat, powerStat, weightStat;
    private JButton saveButton, exitButton;
    private JLabel engineImage, tiresImage, wingImage,statsTitle;

    public PitStopView(String primaryColor,String equipo ,String secondaryColor) {
        this.colorPrimary = stringToColor(primaryColor);
        this.colorSecondary = stringToColor(secondaryColor);
        this.EquipoSeleccionado = equipo;
        setLayout(new BorderLayout());
        setOpaque(false);
        initUI();
    }

    private void initUI() {
      
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBackground(g);
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(createHeader(), BorderLayout.NORTH);
        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    private JPanel createHeader() {
        JLabel title = new JLabel("PIT STOP - CONFIGURACION DEL AUTO", JLabel.CENTER);
        title.setFont(Main.GLOBAL_FONT.deriveFont(Font.BOLD, 28f));
        title.setForeground(colorSecondary);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(title);
        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
       
        // Columnas de selección
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(createPartPanel("MOTOR", getEngineOptions(), "/resources/Imagens/Engines/"), gbc);
        
        gbc.gridx = 1;
        centerPanel.add(createPartPanel("NEUMÁTICOS", getTireOptions(), "/resources/Imagens/Tires/"), gbc);
        
        gbc.gridx = 2;
        centerPanel.add(createPartPanel("ALERÓN", getWingOptions(), "/resources/Imagens/Wings/"), gbc);
        
        // Fila de estadísticas
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 3;
        centerPanel.add(createStatsPanel(), gbc);
        
        return centerPanel;
    }

    private JPanel createPartPanel(String title, String[] options, String imgPath) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(createStyledBorder());
        
        // Título
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(Main.GLOBAL_FONT2.deriveFont(Font.BOLD, 18f));
        titleLabel.setForeground(colorSecondary);
        
        // ComboBox
        JComboBox<String> combo = new JComboBox<>(options);
        configureComboBox(combo);
        
        // Imagen
        JLabel partImage = createPartImage(imgPath + options[0] + ".png");
        
        // Eventos
        combo.addActionListener(e -> {
            String selected = (String) combo.getSelectedItem();
            updatePartImage(partImage, imgPath + selected + ".png");
            updateStats();
        });
        
        // Organización
        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setOpaque(false);
        content.add(combo, BorderLayout.NORTH);
        content.add(partImage, BorderLayout.CENTER);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        
        // Guardar referencia
        switch(title) {
            case "MOTOR": engineCombo = combo; engineImage = partImage; break;
            case "NEUMÁTICOS": tiresCombo = combo; tiresImage = partImage; break;
            case "ALERÓN": wingCombo = combo; wingImage = partImage; break;
        }
        
        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(2, 0, 0, 0, colorSecondary),
            new EmptyBorder(20, 20, 10, 20)
        ));
        
        panel.add(createStatsTextPanel());
        panel.add(createCarPreviewPanel());
        updateStats();
        
        return panel;
    }

    private JPanel createStatsTextPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 0, 10));
        panel.setOpaque(false);
        
        statsTitle = new JLabel("ESTADÍSTICAS ACTUALES", JLabel.CENTER);
        statsTitle.setFont(Main.GLOBAL_FONT2.deriveFont(Font.BOLD, 20f));
        statsTitle.setForeground(colorSecondary);
        
        gripStat = createStatLabel("Agarre: ");
        speedStat = createStatLabel("Velocidad Máx: ");
        powerStat = createStatLabel("Potencia: ");
        weightStat = createStatLabel("Peso: ");
        
        panel.add(statsTitle);
        panel.add(gripStat);
        panel.add(speedStat);
        panel.add(powerStat);
        panel.add(weightStat);
        
        return panel;
    }

    private JPanel createCarPreviewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        JLabel title = new JLabel("VISTA PREVIA", JLabel.CENTER);
        title.setFont(Main.GLOBAL_FONT2.deriveFont(Font.BOLD, 20f));
        title.setForeground(colorSecondary);
        
        JLabel carImage = new JLabel();
        carImage.setHorizontalAlignment(JLabel.CENTER);
        try {
            URL imageUrl = getClass().getResource("/resources/Imagens/"+ EquipoSeleccionado +"/Car.png");
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                Image scaled = icon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
                carImage.setIcon(new ImageIcon(scaled));
            }
        } catch (Exception e) {
            carImage.setText("Imagen no disponible");
        }
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(carImage, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
        panel.setOpaque(false);
        
        saveButton = createActionButton("GUARDAR Y VOLVER");
        exitButton = createActionButton("SALIR SIN GUARDAR");
        
        panel.add(saveButton);
        panel.add(exitButton);
        
        return panel;
    }
    
    

    // Métodos utilitarios
    private Color stringToColor(String rgb) {
        String[] parts = rgb.split(",");
        return new Color(
            Integer.parseInt(parts[0].trim()),
            Integer.parseInt(parts[1].trim()),
            Integer.parseInt(parts[2].trim())
        );
    }

    private void drawBackground(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(20, 20, 20),
            getWidth(), getHeight(), new Color(40, 40, 40)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Añadir herramientas de garaje opcionales
    }

    private Border createStyledBorder() {
        return BorderFactory.createCompoundBorder(
            new LineBorder(colorSecondary, 2, true),
            new EmptyBorder(15, 15, 15, 15)
        );
    }

    private JButton createActionButton(String text) {
        JButton button = new JButton(text);
        button.setFont(Main.GLOBAL_FONT2.deriveFont(Font.BOLD, 16f));
        button.setForeground(colorPrimary);
        button.setBackground(colorSecondary);
        button.setFocusPainted(false);
      
        button.setBorder(createStyledBorder());
       
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(colorPrimary);
                button.setForeground(colorSecondary);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(colorSecondary);
                button.setForeground(colorPrimary);
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
            
                Window window = SwingUtilities.getWindowAncestor(PitStopView.this);
                if (window != null) {
                    window.dispose();
                }
            }
        });
        
     
        
        return button;
    }

    private void configureComboBox(JComboBox<String> combo) {
        combo.setFont(Main.GLOBAL_FONT2);
        combo.setBackground(colorSecondary);
        combo.setForeground(colorPrimary);
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setHorizontalAlignment(CENTER);
                return this;
            }
        });
    }

    private JLabel createPartImage(String imagePath) {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setPreferredSize(new Dimension(200, 150));
        
        try {
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                Image scaled = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaled));
            } else {
                label.setText("Imagen no encontrada");
            }
        } catch (Exception e) {
            label.setText("Error al cargar");
        }
        
        return label;
    }

    private JLabel createStatLabel(String text) {
        JLabel label = new JLabel(text, JLabel.LEFT);
        label.setFont(Main.GLOBAL_FONT2.deriveFont(Font.PLAIN, 18f));
        label.setForeground(Color.WHITE);
        return label;
    }

    private void updatePartImage(JLabel label, String imagePath) {
        try {
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                Image scaled = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaled));
            }
        } catch (Exception e) {
            label.setText("Error al cargar");
        }
    }

    private void updateStats() {
        // Ejemplo de cálculo (ajusta según tu lógica)
        String engine = (String) engineCombo.getSelectedItem();
        String tires = (String) tiresCombo.getSelectedItem();
        String wing = (String) wingCombo.getSelectedItem();
        
        int speed = calculateSpeed(engine, tires, wing);
        int power = calculatePower(engine);
        int grip = calculateGrip(tires, wing);
        int weight = calculateWeight(engine, tires, wing);
        
        speedStat.setText("Velocidad Máx: " + speed + " km/h");
        powerStat.setText("Potencia: " + power + " HP");
        gripStat.setText("Agarre: " + getStarRating(grip));
        weightStat.setText("Peso: " + weight + " kg");
    }

    // Métodos de cálculo de estadísticas (implementa según tu lógica)
    private int calculateSpeed(String engine, String tires, String wing) {
        int speed = 300;
        if (engine.contains("Turbo")) speed += 30;
        if (tires.contains("Blandos")) speed -= 10;
        if (wing.contains("Alto")) speed -= 15;
        return speed;
    }

    private int calculatePower(String engine) {
        if (engine.contains("V8")) return 1000;
        if (engine.contains("V6")) return 850;
        return 700;
    }

    private int calculateGrip(String tires, String wing) {
        int grip = 70;
        if (tires.contains("Blandos")) grip += 20;
        if (wing.contains("Ajustable")) grip += 15;
        return grip;
    }

    private int calculateWeight(String engine, String tires, String wing) {
        int weight = 700;
        if (engine.contains("V8")) weight += 100;
        if (wing.contains("Carrera")) weight += 50;
        return weight;
    }

    private String getStarRating(int value) {
        int stars = Math.min(5, value / 20); // 20 puntos = 1 estrella
        return "★".repeat(stars) + "☆".repeat(5 - stars);
    }

    private String[] getEngineOptions() {
        return new String[]{"V6 Turbo Híbrido", "Honda RBPT V6", "Mercedes-AMG F1", "Mercedes-AMG M14 EG2", "Mercedes-AMG EGP"};
    }

    private String[] getTireOptions() {
        return new String[]{"Soft C5", "Medium C3", "Full Wet", "Hard C1", "Intermediate"};
    }

    private String[] getWingOptions() {
        return new String[]{"Prancing Horse Wings", "AeroBull Vortex", "Silver Arrow Glide", "Papaya Storm", "Green Phantom Blade"};
    }
    
    
  

    // Getters para los botones
    public JButton getSaveButton() { return saveButton; }
    public JButton getExitButton() { return exitButton; }
}