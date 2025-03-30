package dao;

import model.CompetitionRegistration;

public interface CompetitionRegistrationDao {
    CompetitionRegistration selectCompetitionRegistrationById(int id);
    void addCompetitionRegistration(CompetitionRegistration competitionRegistration);
}
