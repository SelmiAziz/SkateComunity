package utils;

import java.sql.*;


public class DbsConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/progettoSoftware";  // URL del database
    private static final String USER = "root";  // Username per il DB
    private static final String PASSWORD = "1515";  // Password per il DB

    private static DbsConnector instance;
    private Connection connection;

    // Costruttore privato per evitare istanze multiple
    private DbsConnector() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException("Errore nella connessione al database", e);
        }
    }

    // Metodo statico per ottenere l'istanza unica
    public static synchronized DbsConnector getInstance() {
        if (instance == null) {
            instance = new DbsConnector();
        }
        return instance;
    }

 public synchronized Connection getConnection() {
        try {
            if ( connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }



    // Metodo per chiudere la connessione
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Connessione chiusa.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

