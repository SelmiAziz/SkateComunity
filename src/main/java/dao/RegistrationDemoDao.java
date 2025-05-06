package dao;

import model.Registration;

import java.util.ArrayList;
import java.util.List;

public class RegistrationDemoDao implements RegistrationDao {
    private static RegistrationDemoDao instance = null;
    private final List<Registration> competitionRegistrationList = new ArrayList<>();

    public static synchronized RegistrationDemoDao getInstance(){
        if(instance == null){
            instance = new RegistrationDemoDao();
        }
        return instance;
    }

    @Override
    public void addRegistration(Registration competitionRegistration) {
        this.competitionRegistrationList.add(competitionRegistration);
    }

    @Override
    public Registration selectRegistrationById(int id) {
        for(Registration competitionRegistration : competitionRegistrationList){
            if(competitionRegistration.getRegistrationId() == id){
                return competitionRegistration;
            }
        }
        return null;
    }
}
