package dao;

import model.CompetitionRegistration;

import java.util.ArrayList;
import java.util.List;

public class CompetitionRegistrationDemoDao implements CompetitionRegistrationDao {
    private static CompetitionRegistrationDemoDao instance = null;
    private final List<CompetitionRegistration> competitionRegistrationList = new ArrayList<>();

    public static synchronized CompetitionRegistrationDemoDao getInstance(){
        if(instance == null){
            instance = new CompetitionRegistrationDemoDao();
        }
        return instance;
    }

    @Override
    public void addCompetitionRegistration(CompetitionRegistration competitionRegistration) {
        this.competitionRegistrationList.add(competitionRegistration);
    }

    @Override
    public CompetitionRegistration selectCompetitionRegistrationById(int id) {
        for(CompetitionRegistration competitionRegistration : competitionRegistrationList){
            if(competitionRegistration.getRegistrationId() == id){
                return competitionRegistration;
            }
        }
        return null;
    }
}
