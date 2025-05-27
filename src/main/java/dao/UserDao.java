package dao;

import exceptions.DataAccessException;
import login.User;


public interface UserDao {
     User selectUserByUsername(String username) throws DataAccessException;
     void addUser(User user) throws DataAccessException;
     boolean checkUserByUsernameAndPassword(String username, String password) throws DataAccessException;
     boolean checkUserByUsername(String username) throws DataAccessException;
}
