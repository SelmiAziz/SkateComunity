package dao;

import model.Registration;

public interface RegistrationDao {
    Registration selectRegistrationById(int id);
    void addRegistration(Registration competitionRegistration);
}
