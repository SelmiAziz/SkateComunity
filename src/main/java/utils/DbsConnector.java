package utils;


import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;


public class DbsConnector {

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
        }catch(RuntimeException | SQLException |ClassNotFoundException |IOException _){
            //
        }

    }

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



    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

