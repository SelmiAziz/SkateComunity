package dao;

import exceptions.DataAccessException;
import model.Registration;

import java.io.IOException;

public interface RegistrationDao {
    Registration selectRegistrationById(int id) throws DataAccessException;
    void addRegistration(Registration competitionRegistration) throws DataAccessException;
}
