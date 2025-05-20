package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL = "jdbc:mysql://localhost:3306/ArkanoidDB";
    private static final String USER = "root"; // Cambia esto si tienes otro user
    private static final String PASSWORD = "mysql"; // Pon tu password si tienes

    public static Connection conectar() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Este ya está OK
        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró el driver JDBC");
            e.printStackTrace();
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}