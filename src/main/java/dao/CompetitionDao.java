package dao;

import model.Competition;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface CompetitionDao {
    Competition selectCompetitionByName(String competitionName) throws IOException;
    boolean checkCompetition(String competitionName);
    void addCompetition(Competition competition) throws SQLException;
    List<Competition> selectAvailableCompetitions() throws IOException;
    List<Competition> selectCompetitionsByDateAndLocation(String date, String country) throws IOException;
}
