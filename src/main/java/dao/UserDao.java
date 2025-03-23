package dao;

import login.User;
import login.Role;

public interface UserDao {
     User selectUserByUsername(String username);
     void addUser(User user);
     boolean checkUserByUsernameAndPassword(String username, String password);
     boolean checkUserByUsername(String username);
}
