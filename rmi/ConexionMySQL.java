package rmi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://switchback.proxy.rlwy.net:23308/railway?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "mDOEwmcZLqchMcNYkyWdIBRIIFIioHIc";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
            System.out.println("¡Conectado a la base de datos Railway!");
            conn.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver JDBC");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos");
            e.printStackTrace();
        }
    }
}

