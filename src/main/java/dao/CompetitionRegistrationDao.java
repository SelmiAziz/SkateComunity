package dao;

import model.Registration;

public interface CompetitionRegistrationDao {
    Registration selectCompetitionRegistrationById(int id);
    void addCompetitionRegistration(Registration competitionRegistration);
}
