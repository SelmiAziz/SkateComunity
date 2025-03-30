package startSkate;

import dao.UserFileSystemDao;
import login.Role;
import login.User;

public class prove {
    public static void main(String[] args) {
        User user = new User("Marco", "ff", "Fria", Role.COSTUMER);
        User newUser = new User("Riccardo", "marco", "03/05/2003", Role.ORGANIZER);
        User provaUser = new User("Aziz", "fj", "fdlf", Role.COSTUMER);
        UserFileSystemDao p = UserFileSystemDao.getInstance();
        p.addUser(user);
        p.addUser(newUser);
        p.addUser(provaUser);
        User userTrovato = p.selectUserByUsername("Aziz");
        if(userTrovato != null){
            System.out.println(userTrovato.getUsername() + " " + userTrovato.getPassword() + " " + userTrovato.getRole()+" "+userTrovato.getRole());
        }
        p.selectUserByUsername(user.getUsername());
    }
}
