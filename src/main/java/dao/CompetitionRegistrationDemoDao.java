package dao;

import model.Registration;

import java.util.ArrayList;
import java.util.List;

public class CompetitionRegistrationDemoDao implements CompetitionRegistrationDao {
    private static CompetitionRegistrationDemoDao instance = null;
    private final List<Registration> competitionRegistrationList = new ArrayList<>();

    public static synchronized CompetitionRegistrationDemoDao getInstance(){
        if(instance == null){
            instance = new CompetitionRegistrationDemoDao();
        }
        return instance;
    }

    @Override
    public void addCompetitionRegistration(Registration competitionRegistration) {
        this.competitionRegistrationList.add(competitionRegistration);
    }

    @Override
    public Registration selectCompetitionRegistrationById(int id) {
        for(Registration competitionRegistration : competitionRegistrationList){
            if(competitionRegistration.getRegistrationId() == id){
                return competitionRegistration;
            }
        }
        return null;
    }
}
