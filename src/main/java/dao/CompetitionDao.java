package dao;

import model.Competition;

import java.sql.SQLException;
import java.util.List;

public interface CompetitionDao {
    Competition selectCompetitionByName(String competitionName);
    boolean checkCompetition(String competitionName);
    void addCompetition(Competition competition) throws SQLException;
    List<Competition> selectAvailableCompetitions();
    List<Competition> selectCompetitionsByDateAndLocation(String date, String country);
}
