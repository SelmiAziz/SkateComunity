package startSkate;

import login.Account;
import login.AccountType;
import login.SessionManager;
import model.Costumer;

public class MainProva3 {
    public static void main(String[] args) {
        Costumer user = new Costumer("mmamma", "nicola", new Account("fjldfj", "hfdhfh", AccountType.USER));
        SessionManager sessionMager = SessionManager.getInstance();


    }
}
