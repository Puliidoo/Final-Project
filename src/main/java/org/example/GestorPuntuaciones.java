package org.example;

import org.example.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestorPuntuaciones {

    public static void guardarPuntuacion(String nombre, int puntuacion) {
        String sql = "INSERT INTO puntuaciones (nombre_jugador, puntuacion) VALUES (?, ?)";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setInt(2, puntuacion);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> obtenerTopPuntuaciones() {
        List<String> top = new ArrayList<>();
        String sql = "SELECT nombre_jugador, puntuacion FROM puntuaciones ORDER BY puntuacion DESC LIMIT 10";

        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String jugador = rs.getString("nombre_jugador");
                int puntuacion = rs.getInt("puntuacion");
                top.add(jugador + " - " + puntuacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return top;
    }
}
