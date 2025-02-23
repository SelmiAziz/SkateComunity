package startSkate;

import utils.DbsConnector;

public class MainProva
{
    public static void main(String[] args) {
        DbsConnector db = DbsConnector.getInstance();

        // Esegui operazioni sul database
        db.selectAllUsers();
        //db.insertUser("Aziz", "password123");
        //db.selectAllUsers();
    }
}
