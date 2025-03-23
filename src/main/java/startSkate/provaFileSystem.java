package startSkate;

import login.Role;
import login.User;
import model.Customer;
import model.Organizer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class provaFileSystem {
    public void saveUserToFile(User user, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
              //  String profileType =  user.getProfile().getProfileType() == Role.COSTUMER ? "costumer" : "organizer";
                //writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getProfile().getName() + "," +profileType );
                //if(profileType.equals("costumer")){
                    // saveCostumerToFile((Customer)user.getProfile(), "src/main/resources/csv/profiles.csv");
                //}else{
                    //saveOrganizerToFile((Organizer)user.getProfile(), "src/main/resources/csv/profiles.csv");
                //}
                writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCostumerToFile(Customer costumer, String filename){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            //writer.write(costumer.getName() + "," + costumer.getCoins() + "," +"costumer");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveOrganizerToFile(Organizer organizer, String filename){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
           // writer.write(organizer.getName() + "," + organizer.getCoins() + "," +"costumer");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Organizer retriveOrganizerFromFile(String filename, String nameProfile){
        Organizer profile = null;
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            while((line = reader.readLine()) != null){
                String[] data = line.split(",");
                if(data.length == 3){
                    if(nameProfile.equals(data[0])){
                      //  profile = new Organizer(data[0], parseInt(data[1]));
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return profile;
    }


    public Customer retriveCostumerFromFile(String filename, String nameProfile){
        Customer profile = null;
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            while((line = reader.readLine()) != null){
                String[] data = line.split(",");
                if(data.length == 3){
                    if(nameProfile.equals(data[0])){
                        //profile = new Customer(data[0], parseInt(data[1]));
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return profile;
    }

    public List<User> retrieveUserFromFile(String filename) {
        List<User> userList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                   // Profile profile;
                    if(data[3].equals("costumer")){
                        //profile = retriveCostumerFromFile("src/main/resources/csv/profiles.csv", data[2]);
                    }else{
                        //profile = retriveOrganizerFromFile("src/main/resources/csv/profiles.csv", data[2]);
                    }
                    //User user = new User(data[0], data[1]);
                    //user.setProfile(profile);
                    //userList.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userList;
    }

}
