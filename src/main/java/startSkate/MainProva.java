package startSkate;

import login.User;
import model.Costumer;
import model.Organizer;
import utils.DbsConnector;

import java.util.List;

public class MainProva {
    public static void main(String[] args) {
        //DbsConnector.getInstance().insertUser("messi", "ullo", "skate");
        //DbsConnector.getInstance().insertProfile("skate",30,"costumer");
        //DbsConnector.getInstance().insertProfile("BRo", 2, "costumer");
        provaFileSystem ps = new provaFileSystem();
        User user = new User("Luchino", "Simesso");
        User user2 = new User("marko","brocco");
        Costumer costumer = new Costumer("mmmim", 2);
        Organizer organizer = new Organizer("siss", 0);
        user.setProfile(costumer);
        user2.setProfile(organizer);
        ps.saveUserToFile(user, "src/main/resources/csv/users.csv");
        ps.saveUserToFile(user2,"src/main/resources/csv/users.csv");
        List<User> lUser =  ps.retrieveUserFromFile("src/main/resources/csv/users.csv");
        for(User us:lUser){
            System.out.println(us.getUsername() + us.getProfile().getProfileType());
        }

    }
}
