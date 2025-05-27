package dao;

import exceptions.DataAccessException;
import login.Role;
import login.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserFileSystemDao  implements UserDao{
    private static UserFileSystemDao instance ;
    private final List<User> userList;
    private String fd = "src/main/resources/csv/users.csv";


    UserFileSystemDao(){
        this.userList = new ArrayList<>();
    }

    public static synchronized  UserFileSystemDao getInstance(){
        if(instance == null){
            instance = new UserFileSystemDao();
        }
        return instance;

    }

    @Override
    public User selectUserByUsername(String username) throws DataAccessException {
        for(User user:userList){
            if(user.getUsername().equals(username)){
                return user;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fd))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] arr = line.split(",");
                if(arr[0].equals(username)){
                    Role role = arr[3].equals("CUSTOMER") ?Role.COSTUMER:Role.ORGANIZER;
                    return new User(arr[0], arr[1],arr[2],role );
                }
            }

        } catch (IOException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

        @Override
    public void addUser(User user) throws DataAccessException {
        this.userList.add(user);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fd, true))) {
            writer.write(user.toCsvString());
            writer.newLine();
        } catch (IOException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


    @Override
    public boolean checkUserByUsername(String username) throws DataAccessException {
        for(User user:userList){
            if(user.getUsername().equals(username) ){
                return true;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fd))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] arr = line.split(",");
                if(arr[0].equals(username)){
                    return true;
                }
            }

        } catch (IOException e) {
            throw new DataAccessException(e.getMessage());
        }
        return false;
    }


    @Override
    public boolean checkUserByUsernameAndPassword(String username, String password) throws DataAccessException {
        for(User user:userList){
            if(user.getUsername().equals(username) && user.getPassword().equals(password)){
                return true;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fd))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] arr = line.split(",");
                if(arr[0].equals(username) && arr[1].equals(password)){
                    return true;
                }
            }

        }catch (IOException e) {
            throw new DataAccessException(e.getMessage());
        }
        return false;
    }
}
