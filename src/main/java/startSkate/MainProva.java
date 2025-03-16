package startSkate;

import login.User;
import model.Costumer;
import model.Organizer;
import utils.DbsConnector;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class MainProva {
    public static void main(String[] args) {
        //DbsConnector.getInstance().insertUser("messi", "ullo", "skate");
        //DbsConnector.getInstance().insertProfile("skate",30,"costumer");
        //DbsConnector.getInstance().insertProfile("BRo", 2, "costumer");

        Properties p = new Properties();
        try(FileInputStream databaseConfFile = new FileInputStream("src/main/resources/databasConf.properties")){
            p.load(databaseConfFile);
            String connectionUrl = p.getProperty("url");
            String user = p.getProperty("user");
            String password = p.getProperty("password");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("HEllo");
            //this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("bYE");
        }catch(RuntimeException e){
            //
        }catch(ClassNotFoundException e){
            //
        }catch(IOException e){
            //
        }

    }
}
