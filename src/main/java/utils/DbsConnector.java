package utils;


import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;


public class DbsConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/progettoSoftware";  // URL del database
    private static final String USER = "root";  // Username per il DB
    private static final String PASSWORD = "1515";  // Password per il DB

    private static DbsConnector instance;
    private Connection connection;
    Properties p = new Properties();
    String connectionUrl;
    String user;
    String password;

    // Costruttore privato per evitare istanze multiple
    private DbsConnector() {


        try(FileInputStream databaseConfFile = new FileInputStream("src/main/resources/databasConf.properties")){
            p.load(databaseConfFile);
            this.connectionUrl = p.getProperty("url");
            this.user = p.getProperty("user");
            this.password = p.getProperty("password");
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(connectionUrl, user, password);
        }catch(RuntimeException e){
            //
        }catch(SQLException e){
            //
        }catch(ClassNotFoundException e){
            //
        }catch(IOException e){
            //
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
                connection = DriverManager.getConnection(connectionUrl, user, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.connection;
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

