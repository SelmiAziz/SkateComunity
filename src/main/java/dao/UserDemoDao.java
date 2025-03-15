package dao;

import dao.patternAbstractFactory.DaoFactory;
import login.ProfileType;
import login.User;
import model.Costumer;
import model.Organizer;

import java.util.ArrayList;
import java.util.List;

public class UserDemoDao implements UserDao {
    private static UserDemoDao instance;
    private final OrganizerDao organizerDao = DaoFactory.getInstance().createOrganizerDao();
    private final CostumerDao costumerDao = DaoFactory.getInstance().createCostumerDao();
    private final List<User> userList;

    public UserDemoDao() {
        this.userList = new ArrayList<>();

    }

    public synchronized static UserDemoDao getInstance() {
        if (instance == null) {
            instance = new UserDemoDao();
        }
        return instance;
    }


    public void addUser(User user){
        this.userList.add(user);
        if(user.getProfile().getProfileType() == ProfileType.ORGANIZER){
            organizerDao.addOrganizer((Organizer)user.getProfile());
        }else if(user.getProfile().getProfileType() == ProfileType.COSTUMER){
            costumerDao.addCostumer((Costumer)user.getProfile());
        }
    }


    public User selectUserByUsername(String username){
        for( User user: this.userList){
            if(user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }


    @Override
    public boolean checkUser(String username, String password) {
        for(User user: userList){
            if(user.getUsername().equals(username) && user.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }

    public List<User> getUserList() {
        return userList;
    }
}
