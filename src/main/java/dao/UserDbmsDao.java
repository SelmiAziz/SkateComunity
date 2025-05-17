package dao;


import login.Role;
import login.User;
import utils.DbsConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDbmsDao implements UserDao {
    private static UserDbmsDao instance;
    private final List<User> userList = new ArrayList<>();

    public static synchronized  UserDbmsDao getInstance(){
        if(instance == null){
            instance = new UserDbmsDao();
        }
        return instance;
    }

    @Override
    public User selectUserByUsername(String username) {
        for(User user:userList){
            if(user.getUsername().equals(username)){
                return user;
            }
        }

        String query = "SELECT username, password, dateOfBirth, role FROM users WHERE username = ?";
        Connection connection = DbsConnector.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String uName = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String dateOfBirth = resultSet.getString("dateOfBirth");
                    String role = resultSet.getString("role");
                    User user = new User(uName,password,dateOfBirth, role.equals("Organizer") ? Role.ORGANIZER: Role.COSTUMER);
                    userList.add(user);
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean checkUserByUsernameAndPassword(String username, String password){
        for(User user: userList){
            if(user.getUsername().equals(username) && user.getPassword().equals(password)  ){
                return true;
            }
        }
        Connection connection = DbsConnector.getInstance().getConnection();
        String query = "SELECT 1 FROM users WHERE username = ? AND password = ? LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public boolean checkUserByUsername(String username){
        for(User user: userList){
            if(user.getUsername().equals(username)){
                return true;
            }
        }
        Connection connection = DbsConnector.getInstance().getConnection();
        String query = "SELECT 1 FROM users WHERE username = ?  LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public void addUser(User user){
        userList.add(user);

        String query = "INSERT INTO users (username, password, dateOfBirth,role) VALUES (?, ?, ?, ?)";
        Connection connection = DbsConnector.getInstance().getConnection();
        String username = user.getUsername();
        String password = user.getPassword();
        String dateOfBirth = user.getDateOfBirth();
        String role = user.getRole()== Role.COSTUMER ? "Customer": "Organizer";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, dateOfBirth);
            preparedStatement.setString(4,role);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
