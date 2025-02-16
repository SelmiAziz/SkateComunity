package utils;

import java.sql.*;
import java.sql.*;

public class DbsConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/prova";
    private static final String USER = "root";
    private static final String PASSWORD = "1515";

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

    // Metodo per ottenere la connessione
    public Connection getConnection() {
        return connection;
    }

    // Metodo per selezionare tutti gli utenti
    public void selectAllUsers() {
        String query = "SELECT name, password FROM loginTable";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("Lista Utenti:");
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String pass = resultSet.getString("password");
                System.out.println("User: " + name + " - Password: " + pass);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metodo per inserire un nuovo utente
    public void insertUser(String name, String pass) {
        String query = "INSERT INTO loginTable (name, password) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, pass);
            preparedStatement.executeUpdate();
            System.out.println("Utente aggiunto: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
