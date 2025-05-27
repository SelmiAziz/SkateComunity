package dao;

import exceptions.DataAccessException;
import model.Competition;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface CompetitionDao {
    Competition selectCompetitionByName(String competitionName) throws DataAccessException;
    boolean checkCompetition(String competitionName) throws DataAccessException;
    void addCompetition(Competition competition) throws DataAccessException;
    List<Competition> selectAvailableCompetitions() throws DataAccessException;
    List<Competition> selectCompetitionsByDateAndLocation(String date, String country) throws DataAccessException;
}
