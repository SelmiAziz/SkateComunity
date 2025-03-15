package dao;

import model.Competition;

import java.util.List;

public interface CompetitionDao {
    Competition selectCompetitionByName(String competitionName);
    List<Competition> selectAvailableCompetitions();
    List<Competition> selectFreeCompetitions();
    List<Competition> selectCompetitionByCategory();
    boolean checkCompetition(String competitionName);
    void removeCompetition(Competition competition);
    void addCompetition(Competition competition);
}
