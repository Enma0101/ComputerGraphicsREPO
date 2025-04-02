package database;

import java.sql.*;

public class UsuarioDAO {
    private Connection connection;
    private final String URL = "jdbc:sqlite:src/main/java/database/DBF1.db"; // Ajustar a tu ruta

    
    public UsuarioDAO() {
       
    }

    // Método para obtener conexión
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Método para cerrar recursos
    private void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar recursos: " + e.getMessage());
        }
    }

  
    public boolean esUsuarioValido(String nombreUsuario) {
        return nombreUsuario.matches("^[a-zA-Z0-9]{4,15}$"); 
    }

    
    public boolean crearUsuario(String nombreUsuario) {
        // Validación antes de intentar crear el usuario
        if (!esUsuarioValido(nombreUsuario)) {
            System.out.println("Nombre de usuario inválido. Debe contener solo letras y números (máx. 15 caracteres).");
            return false;
        }
        
    
        
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;
        
        try {
            conn = getConnection();
            String query = "INSERT INTO Usuarios (nombre_usuario) VALUES (?)";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, nombreUsuario);
            
            int filasAfectadas = stmt.executeUpdate();
            exito = filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, null);
        }
        
        return exito;
    }

   
    public String obtenerEquipoUsuario(String nombreUsuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String equipo = null;
        
        try {
            conn = getConnection();
            
            // Usando JOIN para obtener el nombre del equipo directamente
            String query = "SELECT e.nombre_equipo FROM Usuarios u " +
                          "JOIN Equipos e ON u.id_equipo = e.id_equipo " +
                          "WHERE u.nombre_usuario = ?";
                          
            stmt = conn.prepareStatement(query);
            stmt.setString(1, nombreUsuario);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                equipo = rs.getString("nombre_equipo");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener equipo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return equipo;
    }

    
    
    
    public int obtenerIdUsuario(String nombreUsuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int idUsuario = -1; // Valor por defecto en caso de error o si no se encuentra
        
        try {
            conn = getConnection();
            String query = "SELECT id_usuario FROM Usuarios WHERE nombre_usuario = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, nombreUsuario);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                idUsuario = rs.getInt("id_usuario");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener ID de usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return idUsuario;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    public String obtenerIdEquipoUsuario(String nombreUsuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String idEquipo = null;
        
        try {
            conn = getConnection();
            String query = "SELECT id_equipo FROM Usuarios WHERE nombre_usuario = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, nombreUsuario);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                idEquipo = rs.getString("id_equipo");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener ID de equipo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return idEquipo;
    }

    // Método para asignar equipo a usuario
    public boolean asignarEquipoUsuario(String nombreUsuario, String nombreEquipo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean exito = false;
        
        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Iniciar transacción
            
            // Primero obtenemos el ID del equipo
            int idEquipo = 0;
            String queryEquipo = "SELECT id_equipo FROM Equipos WHERE nombre_equipo = ?";
            stmt = conn.prepareStatement(queryEquipo);
            stmt.setString(1, nombreEquipo);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                idEquipo = rs.getInt("id_equipo");
            } else {
                // Si no existe el equipo, hacemos rollback
                conn.rollback();
                return false;
            }
            
            // Cerramos el ResultSet y PreparedStatement antes de reutilizarlos
            rs.close();
            stmt.close();
            
            // Ahora actualizamos el usuario
            String queryUpdate = "UPDATE Usuarios SET id_equipo = ? WHERE nombre_usuario = ?";
            stmt = conn.prepareStatement(queryUpdate);
            stmt.setInt(1, idEquipo);
            stmt.setString(2, nombreUsuario);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                conn.commit(); // Confirmar los cambios
                exito = true;
            } else {
                conn.rollback(); // Revertir los cambios
            }
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.err.println("Error al asignar equipo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true); // Restaurar autocommit
            } catch (SQLException e) {
                System.err.println("Error al restaurar autocommit: " + e.getMessage());
            }
            closeResources(conn, stmt, rs);
        }
        
        return exito;
    }
    
    // Método simplificado para asignar equipo usando directamente el ID
    public boolean asignarEquipoUsuarioPorId(String nombreUsuario, String idEquipo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;
        
        try {
            conn = getConnection();
            String query = "UPDATE Usuarios SET id_equipo = ? WHERE nombre_usuario = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, idEquipo);
            stmt.setString(2, nombreUsuario);
            
            int filasAfectadas = stmt.executeUpdate();
            exito = filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al asignar equipo por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, null);
        }
        
        return exito;
    }
    
    // Método para verificar si un usuario existe
    public boolean existeUsuario(String nombreUsuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean existe = false;
        
        try {
            conn = getConnection();
            String query = "SELECT COUNT(*) AS count FROM Usuarios WHERE nombre_usuario = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, nombreUsuario);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                existe = rs.getInt("count") > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return existe;
    }
    
    
    public String obtenerColorPrincipalEquipo(String idEquipo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String colorPrincipal = null;
        
        try {
            conn = getConnection();
            String query = "SELECT ColoresMain FROM Equipos WHERE id_equipo = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, idEquipo);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                colorPrincipal = rs.getString("ColoresMain");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener color principal: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return colorPrincipal;
    }
    
    // Método para obtener el color secundario de un equipo por ID
    public String obtenerColorSecundarioEquipo(String idEquipo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String colorSecundario = null;
        
        try {
            conn = getConnection();
            String query = "SELECT ColoresSenc FROM Equipos WHERE id_equipo = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, idEquipo);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                colorSecundario = rs.getString("ColoresSenc");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener color secundario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return colorSecundario;
    }
    
    // Método alias para compatibilidad con código antiguo
    public boolean usuarioExiste(String nombreUsuario) {
        return existeUsuario(nombreUsuario);
    }
}