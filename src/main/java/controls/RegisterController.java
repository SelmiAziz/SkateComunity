package controls;

import utils.DbsConnector;

public class RegisterController {
    private final DbsConnector db = DbsConnector.getInstance();

    public boolean registerUser(String username, String password){
        if(username.length() < 3 || password.length() < 3){
            System.out.println("Errore");
            return false;
        }

        try{
            db.insertUser(username, password);
            return true;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }


}
