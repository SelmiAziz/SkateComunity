package dao;

import model.Competition;

import java.util.ArrayList;
import java.util.List;

public class CompetitionDemoDao implements CompetitionDao {
    private static CompetitionDemoDao instance = null;
    private final List<Competition> competitionList = new ArrayList<>();

    public static synchronized CompetitionDemoDao getInstance(){
        if(instance == null){
            instance = new CompetitionDemoDao();
        }
        return instance;
    }

    public List<Competition> selectCompetitionsByDateAndLocation(String date, String location) {
        List<Competition> newCompetitionList = new ArrayList<>();
        for(Competition competition: this.competitionList){
            if(competition.getDate().equals(date) && competition.getLocation().equals(location)){
                newCompetitionList.add(competition);
            }
        }
        return newCompetitionList;
    }

    @Override
    public boolean checkCompetition(String competitionName) {
        for(Competition competition: competitionList){
            if(competition.getName().equals(competitionName)){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Competition> selectAvailableCompetitions() {
        List<Competition> competitionList = new ArrayList<>();
        for(Competition competition: this.competitionList){
            if(competition.getRegistrationsNumber() < competition.getMaxRegistrations()){
                competitionList.add(competition);
            }
        }
        return competitionList;
    }

    public Competition selectCompetitionByName(String name){
        for(Competition competition: competitionList){
            if(competition.getName().equals(name)){
                return competition;
            }
        }
        return null;
    }

    public void addCompetition(Competition competition){
        competitionList.add(competition);
    }
}
