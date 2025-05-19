package dao;

import model.Registration;

import java.io.IOException;

public interface RegistrationDao {
    Registration selectRegistrationById(int id) throws IOException;
    void addRegistration(Registration competitionRegistration);
}
