package dao;


import dao.patternAbstractFactory.DaoFactory;
import login.ProfileType;
import login.User;
import model.Costumer;
import model.Organizer;
import utils.DbsConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDbmsDao implements UserDao {
    private static UserDbmsDao instance;
    private final List<User> userList = new ArrayList<>();
    private final CostumerDao costumerDao = DaoFactory.getInstance().createCostumerDao();
    private final OrganizerDao organizerDao = DaoFactory.getInstance().createOrganizerDao();

    public synchronized static UserDbmsDao getInstance(){
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

        String query = "SELECT username, password, profileName, profileType FROM users WHERE username = ?";
        Connection connection = DbsConnector.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String uName = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String profileName = resultSet.getString("profileName");
                    String profileType = resultSet.getString("profileType");
                    User user = new User(uName,password);
                    if(profileType.equals("costumer")){
                        Costumer costumer = costumerDao.selectCostumerByCostumerName(profileName);
                        user.setProfile(costumer);
                    }else{
                        Organizer organizer = organizerDao.selectOrganizerByOrganizerName(profileName);
                        user.setProfile(organizer);
                    }
                    userList.add(user);
                    return user;
                } else {
                    System.out.println("Utente non trovato.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean checkUser(String username, String password){
        for(User user: userList){
            if(user.getUsername().equals(username) && user.getPassword().equals(password)){
                return true;
            }
        }
        Connection connection = DbsConnector.getInstance().getConnection();
        String query = "SELECT 1 FROM users WHERE username = ? AND password = ? LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            System.out.println("Usando databasePer cercare");
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public void addUser(User user){
        //first I put everything in memory
        userList.add(user);
        if (user.getProfile().getProfileType() == ProfileType.COSTUMER) {
            costumerDao.addCostumer((Costumer)user.getProfile());
        }else if(user.getProfile().getProfileType() == ProfileType.ORGANIZER){
            organizerDao.addOrganizer((Organizer)user.getProfile());
        }

        String query = "INSERT INTO users (username, password, profileName,profileType) VALUES (?, ?, ?,?)";
        Connection connection = DbsConnector.getInstance().getConnection();
        String username = user.getUsername();
        String password = user.getPassword();
        String profileName = user.getProfile().getName();
        String profileType = user.getProfile().getProfileType() == ProfileType.COSTUMER ? "costumer": "organizer";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, profileName);
            preparedStatement.setString(4,profileType);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
