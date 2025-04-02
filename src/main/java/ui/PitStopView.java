package ui;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.*;

import database.DatabaseConnection;
import database.UsuarioDAO;
import graphics.CocheF1;
import main.Main;



public class PitStopView extends JPanel {
    private final Color colorPrimary;
    private Color colorSecondary;
    private final String EquipoSeleccionado;
    private final int idEquipo;
    private final int idUsuario;
    private String username;
    private String IdSeleccionado;
    private UsuarioDAO usuarioDAO;
    
    // Datos del vehículo actual
    private int idVehiculo;
    private int idMotor;
    private int idRuedas;
    private int idAleron;
    private double agarre;
    private double potencia;
    private double velocidadMax;
    private double peso;
    
    // Componentes de selección
    private JComboBox<String> engineCombo, tiresCombo, wingCombo;
    private JLabel gripStat, speedStat, powerStat, weightStat;
    private JButton saveButton, exitButton;
    private JLabel engineImage, tiresImage, wingImage, statsTitle;
    
    // Mapeo de nombres de partes a IDs en la base de datos
    private MotoresMap motoresMap;
    private NeumaticosMap neumaticosMap;
    private AleronesMap aleronesMap;

    public PitStopView(Color colorMain, String equipo, Color colorSen, int idUsuario, int idEquipo) {
        this.colorPrimary = colorMain;
        this.colorSecondary = colorSen;
        this.EquipoSeleccionado = equipo;
        this.idUsuario = idUsuario;
        this.idEquipo = idEquipo;
        this.usuarioDAO = new UsuarioDAO();
        
        
        if(EquipoSeleccionado.equals("McLaren")) {
        	colorSecondary = Color.white;
        	
        }
        // Inicializar mapeos
        this.motoresMap = new MotoresMap();
        this.neumaticosMap = new NeumaticosMap();
        this.aleronesMap = new AleronesMap();
        
        // Cargar mapeos de la base de datos
        cargarMapeos();
        
        // Cargar configuración inicial del vehículo
        cargarConfiguracionVehiculo();
        
        setLayout(new BorderLayout());
        setOpaque(false);
        initUI();
    }

    private void cargarMapeos() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Cargar mapeo de motores
            PreparedStatement pstmtMotores = conn.prepareStatement("SELECT id_motor, nombre FROM Motores");
            ResultSet rsMotores = pstmtMotores.executeQuery();
            while (rsMotores.next()) {
                motoresMap.agregarMotor(rsMotores.getInt("id_motor"), rsMotores.getString("nombre"));
            }
            
            // Cargar mapeo de neumáticos
            PreparedStatement pstmtNeumaticos = conn.prepareStatement("SELECT id_ruedas, nombre FROM Neumaticos");
            ResultSet rsNeumaticos = pstmtNeumaticos.executeQuery();
            while (rsNeumaticos.next()) {
                neumaticosMap.agregarNeumatico(rsNeumaticos.getInt("id_ruedas"), rsNeumaticos.getString("nombre"));
            }
            
            // Cargar mapeo de alerones
            PreparedStatement pstmtAlerones = conn.prepareStatement("SELECT id_aleron, nombre FROM Alerones");
            ResultSet rsAlerones = pstmtAlerones.executeQuery();
            while (rsAlerones.next()) {
                aleronesMap.agregarAleron(rsAlerones.getInt("id_aleron"), rsAlerones.getString("nombre"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos de piezas: " + e.getMessage(), 
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cargarConfiguracionVehiculo() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Primero verificamos si el usuario ya tiene una configuración guardada
            PreparedStatement pstmtConfig = conn.prepareStatement(
                    "SELECT * FROM Configuracion_Vehiculos WHERE id_usuario = ? AND id_vehiculo = " +
                    "(SELECT id_vehiculo FROM Vehiculos WHERE id_equipo = ?)");
            pstmtConfig.setInt(1, idUsuario);
            pstmtConfig.setInt(2, idEquipo);
            ResultSet rsConfig = pstmtConfig.executeQuery();
            
            if (rsConfig.next()) {
                // Si existe configuración del usuario, la cargamos
                idVehiculo = rsConfig.getInt("id_vehiculo");
                idMotor = rsConfig.getInt("id_motor");
                idRuedas = rsConfig.getInt("id_ruedas");
                idAleron = rsConfig.getInt("id_aleron");
                agarre = rsConfig.getDouble("agarre");
                potencia = rsConfig.getDouble("potencia");
                velocidadMax = rsConfig.getDouble("velocidad_max");
                peso = rsConfig.getDouble("peso");
            } else {
                // Si no existe, cargamos la configuración predeterminada del vehículo del equipo
                PreparedStatement pstmtVehiculo = conn.prepareStatement(
                        "SELECT * FROM Vehiculos WHERE id_equipo = ?");
                pstmtVehiculo.setInt(1, idEquipo);
                ResultSet rsVehiculo = pstmtVehiculo.executeQuery();
                
                if (rsVehiculo.next()) {
                    idVehiculo = rsVehiculo.getInt("id_vehiculo");
                    idMotor = rsVehiculo.getInt("id_motor");
                    idRuedas = rsVehiculo.getInt("id_ruedas");
                    idAleron = rsVehiculo.getInt("id_aleron");
                    agarre = rsVehiculo.getDouble("agarre");
                    potencia = rsVehiculo.getDouble("potencia");
                    velocidadMax = rsVehiculo.getDouble("velocidad_max");
                    peso = rsVehiculo.getDouble("peso");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar configuración del vehículo: " + e.getMessage(), 
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
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
        
        // Seleccionar las opciones actuales en los combos
        seleccionarOpcionesActuales();
        
        return centerPanel;
    }
    
    private void seleccionarOpcionesActuales() {
        // Seleccionar el motor actual
        String nombreMotor = motoresMap.getNombreMotor(idMotor);
        if (nombreMotor != null) {
            engineCombo.setSelectedItem(nombreMotor);
        }
        
        // Seleccionar los neumáticos actuales
        String nombreNeumaticos = neumaticosMap.getNombreNeumatico(idRuedas);
        if (nombreNeumaticos != null) {
            tiresCombo.setSelectedItem(nombreNeumaticos);
        }
        
        // Seleccionar el alerón actual
        String nombreAleron = aleronesMap.getNombreAleron(idAleron);
        if (nombreAleron != null) {
            wingCombo.setSelectedItem(nombreAleron);
        }
        
        // Actualizar estadísticas
        updateStats();
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
            
            // Actualizar IDs según la selección
            if (title.equals("MOTOR")) {
                idMotor = motoresMap.getIdMotor(selected);
            } else if (title.equals("NEUMÁTICOS")) {
                idRuedas = neumaticosMap.getIdNeumatico(selected);
            } else if (title.equals("ALERÓN")) {
                idAleron = aleronesMap.getIdAleron(selected);
            }
            
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
        
       
        saveButton.addActionListener(e -> guardarConfiguracion());
        
        panel.add(saveButton);
        panel.add(exitButton);
        
        return panel;
    }
    
    private void guardarConfiguracion() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Verificar si ya existe una configuración para este usuario y vehículo
            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT id_Configuracion_Vehiculos FROM Configuracion_Vehiculos " +
                    "WHERE id_usuario = ? AND id_vehiculo = ?");
            checkStmt.setInt(1, idUsuario);
            checkStmt.setInt(2, idVehiculo);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                // Actualizar configuración existente
                int idConfig = rs.getInt("id_Configuracion_Vehiculos");
                PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE Configuracion_Vehiculos SET " +
                        "agarre = ?, potencia = ?, velocidad_max = ?, peso = ?, " +
                        "id_motor = ?, id_ruedas = ?, id_aleron = ? " +
                        "WHERE id_Configuracion_Vehiculos = ?");
                updateStmt.setDouble(1, agarre);
                updateStmt.setDouble(2, potencia);
                updateStmt.setDouble(3, velocidadMax);
                updateStmt.setDouble(4, peso);
                updateStmt.setInt(5, idMotor);
                updateStmt.setInt(6, idRuedas);
                updateStmt.setInt(7, idAleron);
                updateStmt.setInt(8, idConfig);
                updateStmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Configuración actualizada correctamente", 
                        "Configuración Guardada", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Insertar nueva configuración
                PreparedStatement insertStmt = conn.prepareStatement(
                        "INSERT INTO Configuracion_Vehiculos " +
                        "(id_vehiculo, id_usuario, agarre, potencia, velocidad_max, peso, " +
                        "id_motor, id_ruedas, id_aleron) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                insertStmt.setInt(1, idVehiculo);
                insertStmt.setInt(2, idUsuario);
                insertStmt.setDouble(3, agarre);
                insertStmt.setDouble(4, potencia);
                insertStmt.setDouble(5, velocidadMax);
                insertStmt.setDouble(6, peso);
                insertStmt.setInt(7, idMotor);
                insertStmt.setInt(8, idRuedas);
                insertStmt.setInt(9, idAleron);
                insertStmt.executeUpdate();
                CocheF1.ActualizarSt(potencia,velocidadMax,agarre);
                JOptionPane.showMessageDialog(this, "Nueva configuración guardada correctamente", 
                        "Configuración Guardada", JOptionPane.INFORMATION_MESSAGE);
            }
            
            // Cerrar ventana después de guardar
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar configuración: " + e.getMessage(), 
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
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
                if (button == exitButton) {
                    Window window = SwingUtilities.getWindowAncestor(PitStopView.this);
                    if (window != null) {
                        window.dispose();
                    }
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
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Obtener estadísticas del motor
            PreparedStatement pstmtMotor = conn.prepareStatement(
                    "SELECT potencia,velocidad_max FROM Motores WHERE id_motor = ?");
            pstmtMotor.setInt(1, idMotor);
            ResultSet rsMotor = pstmtMotor.executeQuery();
            if (rsMotor.next()) {
                potencia = rsMotor.getDouble("potencia");
                velocidadMax = rsMotor.getDouble("velocidad_max");
            }
            
            // Obtener estadísticas de los neumáticos
            PreparedStatement pstmtRuedas = conn.prepareStatement(
                    "SELECT agarre FROM Neumaticos WHERE id_ruedas = ?");
            pstmtRuedas.setInt(1, idRuedas);
            ResultSet rsRuedas = pstmtRuedas.executeQuery();
            if (rsRuedas.next()) {
                agarre = rsRuedas.getDouble("agarre");
            }
            
            // Obtener estadísticas del alerón
            PreparedStatement pstmtAleron = conn.prepareStatement(
                    "SELECT peso FROM Alerones WHERE id_aleron = ?");
            pstmtAleron.setInt(1, idAleron);
            ResultSet rsAleron = pstmtAleron.executeQuery();
            if (rsAleron.next()) {
                peso = rsAleron.getDouble("peso");
                
            }
            
            // Ajustar velocidad máxima según combinación de partes
            if (idMotor == 1 && idRuedas == 1) velocidadMax += 5;
            if (idMotor == 2 && idAleron == 2) velocidadMax += 10;
            
            // Ajustar agarre según combinación de partes
            if (idRuedas == 1 && idAleron == 1) agarre += 5;
            
            // Actualizar las etiquetas de estadísticas
            speedStat.setText("Velocidad Máx: " + Math.round(velocidadMax) + " km/h");
            powerStat.setText("Potencia: " + Math.round(potencia) + " HP");
            gripStat.setText("Agarre: " + getStarRating(agarre));
            weightStat.setText("Peso: " + Math.round(peso) + " kg");
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al calcular estadísticas: " + e.getMessage(),
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getStarRating(double value) {
        int stars = Math.min(5, (int)(value / 20)); // 20 puntos = 1 estrella
        return "★".repeat(stars) + "☆".repeat(5 - stars);
    }

    private String[] getEngineOptions() {
        return motoresMap.getNombresMotores();
    }

    private String[] getTireOptions() {
        return neumaticosMap.getNombresNeumaticos();
    }

    private String[] getWingOptions() {
        return aleronesMap.getNombresAlerones();
    }

    // Getters para los botones
    public JButton getSaveButton() { return saveButton; }
    public JButton getExitButton() { return exitButton; }
    
    // Clases para mapeo de IDs a nombres
    private class MotoresMap {
        private java.util.Map<Integer, String> idToNombre = new java.util.HashMap<>();
        private java.util.Map<String, Integer> nombreToId = new java.util.HashMap<>();
        
        public void agregarMotor(int id, String nombre) {
            idToNombre.put(id, nombre);
            nombreToId.put(nombre, id);
        }
        
        public String getNombreMotor(int id) {
            return idToNombre.get(id);
        }
        
        public int getIdMotor(String nombre) {
            return nombreToId.getOrDefault(nombre, 1); // Valor predeterminado 1 si no se encuentra
        }
        
        public String[] getNombresMotores() {
            return nombreToId.keySet().toArray(new String[0]);
        }
    }
    
    private class NeumaticosMap {
        private java.util.Map<Integer, String> idToNombre = new java.util.HashMap<>();
        private java.util.Map<String, Integer> nombreToId = new java.util.HashMap<>();
        
        public void agregarNeumatico(int id, String nombre) {
            idToNombre.put(id, nombre);
            nombreToId.put(nombre, id);
        }
        
        public String getNombreNeumatico(int id) {
            return idToNombre.get(id);
        }
        
        public int getIdNeumatico(String nombre) {
            return nombreToId.getOrDefault(nombre, 1); // Valor predeterminado 1 si no se encuentra
        }
        
        public String[] getNombresNeumaticos() {
            return nombreToId.keySet().toArray(new String[0]);
        }
    }
    
    private class AleronesMap {
        private java.util.Map<Integer, String> idToNombre = new java.util.HashMap<>();
        private java.util.Map<String, Integer> nombreToId = new java.util.HashMap<>();
        
        public void agregarAleron(int id, String nombre) {
            idToNombre.put(id, nombre);
            nombreToId.put(nombre, id);
        }
        
        public String getNombreAleron(int id) {
            return idToNombre.get(id);
        }
        
        public int getIdAleron(String nombre) {
            return nombreToId.getOrDefault(nombre, 1); // Valor predeterminado 1 si no se encuentra
        }
        
        public String[] getNombresAlerones() {
            return nombreToId.keySet().toArray(new String[0]);
        }
    }

}