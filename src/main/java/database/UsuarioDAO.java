package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Métodos para manejar usuarios en la BD (Data Access Object).

public class UsuarioDAO {
    private Connection conexion;
    
    // Constructor 
    public UsuarioDAO() {
        this.conexion = DatabaseConnection.getConnection(); 
    }
    
    // Validación de usuario
    public boolean esUsuarioValido(String nombreUsuario) {
        return nombreUsuario.matches("^[a-zA-Z0-9]{4,15}$"); // Regex para letras y números, minimo 4 máximo 15 caracteres
    }
    
    // Verificar si el usuario existe
    public boolean usuarioExiste(String nombreUsuario) {
        String query = "SELECT COUNT(*) FROM Usuarios WHERE nombre_usuario = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, nombreUsuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Obtener equipo del usuario
    public String obtenerEquipoUsuario(String username) {
        String equipo = null;
        String sql = "SELECT id_equipo FROM Usuarios WHERE nombre_usuario = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                equipo = rs.getString("id_equipo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return equipo;
    }
    
    // Crear usuario
    public boolean crearUsuario(String nombreUsuario) {
        if (!esUsuarioValido(nombreUsuario)) {
            System.out.println("Nombre de usuario inválido. Debe contener solo letras y números (máx. 15 caracteres).");
            return false;
        }
        if (usuarioExiste(nombreUsuario)) {
            System.out.println("El usuario ya existe.");
            return false;
        }

        String query = "INSERT INTO Usuarios (nombre_usuario) VALUES (?)";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, nombreUsuario);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Asignar equipo a un usuario
    public boolean asignarEquipoUsuario(String nombreUsuario, String equipo) {
        String query = "UPDATE Usuarios SET id_equipo = ? WHERE nombre_usuario = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, equipo);
            stmt.setString(2, nombreUsuario);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
